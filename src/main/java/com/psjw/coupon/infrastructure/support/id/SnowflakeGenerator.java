package com.psjw.coupon.infrastructure.support.id;

import com.psjw.coupon.common.exception.id.InvalidNodeIdException;
import com.psjw.coupon.common.exception.id.InvalidSystemClockException;
import com.psjw.coupon.common.exception.id.SnowflakeTimeoutException;
import com.psjw.coupon.common.id.IdGenerator;
import com.psjw.coupon.common.time.TimeProvider;
import java.util.concurrent.atomic.AtomicLong;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SnowflakeGenerator implements IdGenerator {
    private final SnowflakeProperties snowflakeProperties;
    private final long nodeId;
    private final TimeProvider timeProvider;

    private final static int SIGN_BITS = 1;
    private final static int TIMESTAMP_BITS = 41;
    private final static int NODE_ID_BITS = 10;
    private final static int SEQUENCE_BITS = 12;

    private final static long MAX_NODE_ID = (1L << NODE_ID_BITS) - 1; //0 ~ 1023
    private final static long MAX_SEQUENCE_NUMBER = (1L << SEQUENCE_BITS) - 1; //0 ~ 4095

    private final static int TIMESTAMP_SHIFT = SEQUENCE_BITS + NODE_ID_BITS;
    private final static int NODE_ID_SHIFT = SEQUENCE_BITS;

    private final AtomicLong lastTimestampAndSequence = new AtomicLong(0L);


    public SnowflakeGenerator(SnowflakeProperties snowflakeProperties, long nodeId, TimeProvider timeProvider) {
        if (nodeId < 0 || nodeId > MAX_NODE_ID) {
            log.error("Allowed nodeId range is [0, " + MAX_NODE_ID + "], but was {}", nodeId);
            throw new InvalidNodeIdException(nodeId, MAX_NODE_ID);
        }
        this.snowflakeProperties = snowflakeProperties;
        this.nodeId = nodeId;
        this.timeProvider = timeProvider;
    }


    @Override
    public Long generate() {
        while (true) {
            long currentTime = getCurrentTimestamp();
            long last = lastTimestampAndSequence.get();
            long lastTime = last >>> TIMESTAMP_SHIFT;
            long lastSequence = last & MAX_SEQUENCE_NUMBER;


            if (lastTime > currentTime) {
                log.error("System clock is moving backward. Node ID: {}, epoch : {}, current timestamp: {}",
                        nodeId, snowflakeProperties.getEpoch(), currentTime);
                throw new InvalidSystemClockException(nodeId, currentTime);
            }

            long nextSequence;
            if (lastTime == currentTime) {
                nextSequence = (lastSequence + 1) & MAX_SEQUENCE_NUMBER;
                if (nextSequence == 0) {
                    currentTime = waitUntilNextMillis(lastTime);
                }
            } else {
                nextSequence = 0L;
            }

            long newTime = currentTime;
            long next = (newTime << TIMESTAMP_SHIFT) | nextSequence;

            if (lastTimestampAndSequence.compareAndSet(last, next)) {
                return ((newTime - snowflakeProperties.getEpoch()) << TIMESTAMP_SHIFT)
                        | (nodeId << NODE_ID_SHIFT)
                        | nextSequence;
            }
        }
    }

    private long waitUntilNextMillis(long lastTime) {
        long start = System.currentTimeMillis();
        int attempts = 0;
        long currentTime = getCurrentTimestamp();
        while (currentTime <= lastTime) {
            if (System.currentTimeMillis() - start > snowflakeProperties.getTimeoutMs()) {
                log.error("Snowflake wait exceeded timeout of {} ms (lastTime={}, currentTime={})",
                        snowflakeProperties.getTimeoutMs(), lastTime, currentTime);
                throw new SnowflakeTimeoutException("Snowflake wait exceeded timeout of %d ms (lastTime=%d, currentTime=%d)"
                        .formatted(snowflakeProperties.getTimeoutMs(), lastTime, currentTime));
            }
            attempts++;
            if (attempts > snowflakeProperties.getMaxAttempts()) {
                log.error("Snowflake max attempts after {}", snowflakeProperties.getMaxAttempts());
                throw new SnowflakeTimeoutException("Snowflake max attempts after %d"
                        .formatted(snowflakeProperties.getMaxAttempts()));
            }
            Thread.yield();
            currentTime = getCurrentTimestamp();
        }
        return currentTime;
    }

    private long getCurrentTimestamp() {
        return timeProvider.currentTimeMillis();
    }
}
