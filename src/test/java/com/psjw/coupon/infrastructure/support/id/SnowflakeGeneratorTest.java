package com.psjw.coupon.infrastructure.support.id;

import com.psjw.coupon.common.exception.id.InvalidNodeIdException;
import com.psjw.coupon.common.exception.id.InvalidSystemClockException;
import com.psjw.coupon.common.exception.id.SnowflakeTimeoutException;
import com.psjw.coupon.common.time.InstantTimeProvider;
import com.psjw.coupon.common.time.TimeProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;


class SnowflakeGeneratorTest {
    private final static long validEpoch = 1749826800000L;

    @Test
    @DisplayName("SnowflakeGenerator를 통해 생성된 아이디는 항상 값이 존재한다.")
    void generate_whenCalled_thenReturnNotNullId() {
        //given
        long nodeId = 1L;
        SnowflakeProperties testProps = createTestProps(validEpoch, nodeId, 5000, 5);
        TimeProvider timeProvider = new InstantTimeProvider();
        SnowflakeGenerator generator = new SnowflakeGenerator(testProps, nodeId, timeProvider);
        //when
        Long generateId = generator.generate();
        //then
        Assertions.assertThat(generateId).isNotNull();
    }

    @Test
    @DisplayName("SnowflakeGenerator를 통해 각각 생성된 아이디는 항상 값이 다르다.")
    void generate_whenTwoCalled_thenReturnUniqueIds() {
        //given
        long nodeId = 1L;
        SnowflakeProperties testProps = createTestProps(validEpoch, nodeId, 5000, 5);
        TimeProvider timeProvider = new InstantTimeProvider();
        SnowflakeGenerator generator = new SnowflakeGenerator(testProps, nodeId, timeProvider);

        //when
        Long firstGenerateId = generator.generate();
        Long secondGenerateId = generator.generate();
        //then
        Assertions.assertThat(firstGenerateId).isNotEqualTo(secondGenerateId);

    }

    @Test
    @DisplayName("nodeId 범위가 초과할 경우 InvalidNodeIdException을 발생시킨다.")
    void generate_whenNodeIdTooLarge_throwsException() {
        //given
        long nodeId = 2000L;
        SnowflakeProperties testProps = createTestProps(validEpoch, nodeId, 5000, 5);
        TimeProvider timeProvider = new InstantTimeProvider();
        //when //then
        Assertions.assertThatThrownBy(() -> new SnowflakeGenerator(testProps, nodeId, timeProvider))
                .isInstanceOf(InvalidNodeIdException.class);
    }


    @Test
    @DisplayName("시스템 시간이 뒤로 이동했을 때 InvalidSystemClockException을 발생시킨다.")
    void generate_whenSystemClockMovesBackward_throwsException() {
        //given
        AtomicLong mockTime = new AtomicLong(100_000L); // 현재 시간
        TimeProvider mockTimeProvider = mockTime::get;

        SnowflakeProperties props = createTestProps(0L, 1L, 2000, 100);
        SnowflakeGenerator generator = new SnowflakeGenerator(props, 1L, mockTimeProvider);

        generator.generate(); // 정상 생성

        //when
        mockTime.set(50_000L); // 과거로 이동

        // then
        Assertions.assertThatThrownBy(generator::generate)
                .isInstanceOf(InvalidSystemClockException.class);
    }


    @Test
    @DisplayName("시퀀스가 모두 소진되고 시간도 고정되어 있으면 timeout 예외가 발생한다.")
    void generate_whenWaitExceeded_throwsException() throws InterruptedException {
        //given
        long nodeId = 1L;
        long timeoutMs = 100L;
        SnowflakeProperties testProps = createTestProps(validEpoch, nodeId, timeoutMs, 5);
        AtomicLong mockFixedTime = new AtomicLong(100_000L);
        TimeProvider timeProvider = mockFixedTime::get;
        SnowflakeGenerator generator = new SnowflakeGenerator(testProps, nodeId, timeProvider);

        //when
        for (int i = 0; i <= 4095; i++) {
            generator.generate();
        }

        //then
        Assertions.assertThatThrownBy(generator::generate).isInstanceOf(SnowflakeTimeoutException.class);

    }


    @Test
    @DisplayName("멀티 스레드 환경에서도 동시에 생성된 ID는 중복되지 않는다.")
    void generate_whenMultiThreadCalled_returnUniqueIds() throws InterruptedException {
        //given
        long nodeId = 1L;
        int threadCount = 100;
        int idsPerThread = 50;
        int totalCount = threadCount * idsPerThread;

        SnowflakeProperties testProps = createTestProps(validEpoch, nodeId, 5000, 5);
        TimeProvider timeProvider = new InstantTimeProvider();
        SnowflakeGenerator generator = new SnowflakeGenerator(testProps, nodeId, timeProvider);

        Set<Long> ids = ConcurrentHashMap.newKeySet();
        ExecutorService es = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        //when
        for (int i = 0; i < threadCount; i++) {
            es.submit(() -> {
                for (int j = 0; j < idsPerThread; j++) {
                    Long generateId = generator.generate();
                    ids.add(generateId);
                }
                latch.countDown();
            });
        }
        latch.await();
        es.shutdown();

        //then
        Assertions.assertThat(ids).hasSize(totalCount);

    }

    @Test
    @DisplayName("시간이 넘어가면 시퀀스는 0으로 초기화된다")
    void generate_whenNextMillis_returnResetMillis() throws InterruptedException {
        //given
        long nodeId = 1L;
        long maxSequenceBits = 12;
        long maxSequence = (1L << maxSequenceBits) - 1;

        AtomicLong mockFixedTime = new AtomicLong(validEpoch);
        SnowflakeProperties testProps = createTestProps(validEpoch, nodeId, 5000, 5);
        TimeProvider timeProvider = mockFixedTime::get;
        SnowflakeGenerator generator = new SnowflakeGenerator(testProps, nodeId, timeProvider);

        //when
        Long firstGenerateId = generator.generate();
        long firstGenerateSequence = firstGenerateId & maxSequence;
        mockFixedTime.set(validEpoch + 100);
        Long secondGenerateId = generator.generate();
        long secondGenerateSequence = secondGenerateId & maxSequence;

        //then
        Assertions.assertThat(firstGenerateSequence).isEqualTo(0L);
        Assertions.assertThat(secondGenerateSequence).isEqualTo(0L);
    }

    @Test
    @DisplayName("생성된 ID가 올바른 비트구조를 갖는다.")
    void generate_whenCalled_thenCorrectStructureId() {
        //given
        long nodeId = 1L;
        SnowflakeProperties testProps = createTestProps(validEpoch, nodeId, 5000, 5);
        AtomicLong mockFixedTime = new AtomicLong(validEpoch + 100L);
        TimeProvider timeProvider = mockFixedTime::get;
        SnowflakeGenerator generator = new SnowflakeGenerator(testProps, nodeId, timeProvider);
        long expectedTimestamp = mockFixedTime.get() - validEpoch;

        //when
        Long generateId = generator.generate();

        //then
        long sequence = generateId & ((1L << 12) - 1);
        long extractedNodeId = (generateId >>> 12) & ((1L << 10) - 1);
        long timestamp = (generateId >>> 22) & ((1L << 41) - 1);

        Assertions.assertThat(timestamp).isEqualTo(expectedTimestamp);
        Assertions.assertThat(extractedNodeId).isEqualTo(nodeId);
        Assertions.assertThat(sequence).isEqualTo(0L);

    }


    private SnowflakeProperties createTestProps(long epoch, long nodeId, long timeoutMs, long maxAttempts) {
        SnowflakeProperties props = new SnowflakeProperties();
        props.setEpoch(epoch);
        props.setNodeId(nodeId);
        props.setTimeoutMs(timeoutMs);
        props.setMaxAttempts(maxAttempts);
        return props;
    }

}