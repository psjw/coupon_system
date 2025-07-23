package com.psjw.coupon.domain.model.coupon;

import com.psjw.coupon.domain.enums.coupon.JobStatus;
import com.psjw.coupon.domain.enums.coupon.JobType;
import com.psjw.coupon.domain.model.coupon.exception.InvalidCouponCountException;
import com.psjw.coupon.domain.model.coupon.exception.InvalidStatusTransitionException;
import com.psjw.coupon.surpport.helper.CouponTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;


class CouponBatchJobTest {
    private CouponBatch couponBatch;

    private static final String INVALID_STATUS_MSG = "허용되지 않는 상태 변경입니다.";
    private static final String INVALID_COUNT_MSG = "쿠폰 수량이 올바르지 않습니다.";
    private static final String REQUESTED_BY = "admin";
    private static final int TOTAL_COUNT = 10_000;
    private static final JobType JOB_TYPE = JobType.COUPON_GENERATION;
    private static final LocalDateTime REQUESTED_AT = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        couponBatch = Mockito.mock(CouponBatch.class);
    }

    private CouponBatchJob createNewJob(JobStatus status) {
        CouponBatchJob job = CouponBatchJob.createNew(couponBatch, JOB_TYPE, REQUESTED_BY, REQUESTED_AT, TOTAL_COUNT);
        CouponTestHelper.setJobStatus(job, status);
        return job;
    }


    @Test
    @DisplayName("정상적인 값을 넣으면 CouponBatchJob 객체를 반환한다.")
    void createNewJob_WhenValidInput_ThenReturnCorrectObject() {
        // given
        // when
        CouponBatchJob job = CouponBatchJob.createNew(couponBatch, JOB_TYPE, REQUESTED_BY, REQUESTED_AT, TOTAL_COUNT);

        // then
        assertThat(job).isNotNull();
        assertThat(job.getJobStatus()).isEqualTo(JobStatus.PENDING);
        assertThat(job.getRequestedBy()).isEqualTo(REQUESTED_BY);
        assertThat(job.getTotalCount()).isEqualTo(TOTAL_COUNT);
        assertThat(job.getJobType()).isEqualTo(JOB_TYPE);
        assertThat(job.getRequestedAt()).isEqualTo(REQUESTED_AT);
        assertThat(job.getSuccessCount()).isZero();
        assertThat(job.getFailureCount()).isZero();
    }


    @Test
    @DisplayName("PENDING 상태에서 IN_PROGRESS로 상태 변경 성공")
    void changeInProgress_WhenPending_ThenSuccess() {
        // given
        CouponBatchJob job = createNewJob(JobStatus.PENDING);

        // when
        job.changeInProgress();

        // then
        assertThat(job.getJobStatus()).isEqualTo(JobStatus.IN_PROGRESS);
    }


    @ParameterizedTest(name = "현재 상태: {0}")
    @EnumSource(value = JobStatus.class, names = {"COMPLETED", "IN_PROGRESS", "CANCELED", "FAILED"})
    @DisplayName("IN_PROGRESS 변경 시 PENDING이 아니면 예외 발생")
    void changeInProgress_WhenNotPending_ThenThrow(JobStatus status) {
        // given
        CouponBatchJob job = createNewJob(status);

        // when & then
        assertThatThrownBy(job::changeInProgress)
                .isInstanceOf(InvalidStatusTransitionException.class)
                .hasMessage(INVALID_STATUS_MSG);
    }

    @Test
    @DisplayName("IN_PROGRESS 상태에서 COMPLETED로 상태 변경 성공")
    void changeCompleted_WhenInProgress_ThenSuccess() {
        // given
        CouponBatchJob job = createNewJob(JobStatus.IN_PROGRESS);
        LocalDateTime completedAt = LocalDateTime.now();

        // when
        job.changeCompleted(completedAt);

        // then
        assertThat(job.getJobStatus()).isEqualTo(JobStatus.COMPLETED);
        assertThat(job.getCompletedAt()).isEqualTo(completedAt);
    }

    @ParameterizedTest(name = "현재 상태: {0}")
    @EnumSource(value = JobStatus.class, names = {"COMPLETED", "PENDING", "CANCELED", "FAILED"})
    @DisplayName("COMPLETED 변경 시 IN_PROGRESS가 아니면 예외 발생")
    void changeCompleted_WhenNotInProgress_ThenThrow(JobStatus status) {
        // given
        CouponBatchJob job = createNewJob(status);

        // when & then
        assertThatThrownBy(() -> job.changeCompleted(LocalDateTime.now()))
                .isInstanceOf(InvalidStatusTransitionException.class)
                .hasMessage(INVALID_STATUS_MSG);
    }


    @ParameterizedTest(name = "현재 상태: {0}")
    @EnumSource(value = JobStatus.class, names = {"IN_PROGRESS", "PENDING"})
    @DisplayName("FAILED 변경 시 IN_PROGRESS 또는 PENDING이면 정상 변경")
    void changeFailed_WhenInProgressOrPending_ThenSuccess(JobStatus status) {
        // given
        CouponBatchJob job = createNewJob(status);

        // when
        job.changeFailed();

        // then
        assertThat(job.getJobStatus()).isEqualTo(JobStatus.FAILED);
    }


    @ParameterizedTest(name = "현재 상태: {0}")
    @EnumSource(value = JobStatus.class, names = {"COMPLETED", "CANCELED", "FAILED"})
    @DisplayName("FAILED 변경 시 IN_PROGRESS 또는 PENDING이 아니면 예외 발생")
    void changeFailed_WhenInvalidStatus_ThenThrow(JobStatus status) {
        // given
        CouponBatchJob job = createNewJob(status);

        // when & then
        assertThatThrownBy(job::changeFailed)
                .isInstanceOf(InvalidStatusTransitionException.class)
                .hasMessage(INVALID_STATUS_MSG);
    }

    @ParameterizedTest(name = "현재 상태: {0}")
    @EnumSource(value = JobStatus.class, names = {"IN_PROGRESS", "PENDING"})
    @DisplayName("CANCELED 변경 시 IN_PROGRESS 또는 PENDING이면 정상 변경")
    void changeCancel_WhenInProgressOrPending_ThenSuccess(JobStatus status) {
        // given
        CouponBatchJob job = createNewJob(status);

        // when
        job.changeCancel();

        // then
        assertThat(job.getJobStatus()).isEqualTo(JobStatus.CANCELED);
    }


    @ParameterizedTest(name = "현재 상태: {0}")
    @EnumSource(value = JobStatus.class, names = {"COMPLETED", "CANCELED", "FAILED"})
    @DisplayName("CANCELED 변경 시 IN_PROGRESS 또는 PENDING이 아니면 예외 발생")
    void changeCancel_WhenInvalidStatus_ThenThrow(JobStatus status) {
        // given
        CouponBatchJob job = createNewJob(status);

        // when & then
        assertThatThrownBy(job::changeCancel)
                .isInstanceOf(InvalidStatusTransitionException.class)
                .hasMessage(INVALID_STATUS_MSG);
    }

    @Test
    @DisplayName("성공 건수 증가")
    void increaseSuccessCount_WhenValid_ThenIncreased() {
        // given
        CouponBatchJob job = createNewJob(JobStatus.PENDING);

        // when
        job.increaseSuccessCount(5000);

        // then
        assertThat(job.getSuccessCount()).isEqualTo(5000);
    }


    @ParameterizedTest(name = "입력 값: {0}")
    @ValueSource(ints = {0, -1})
    @DisplayName("성공 건수 0 이하 입력 시 예외 발생")
    void increaseSuccessCount_WhenInvalid_ThenThrow(int count) {
        // given
        CouponBatchJob job = createNewJob(JobStatus.PENDING);

        // when & then
        assertThatThrownBy(() -> job.increaseSuccessCount(count))
                .isInstanceOf(InvalidCouponCountException.class)
                .hasMessage(INVALID_COUNT_MSG);
    }


    @Test
    @DisplayName("실패 건수 증가")
    void increaseFailureCount_WhenValid_ThenIncreased() {
        // given
        CouponBatchJob job = createNewJob(JobStatus.PENDING);

        // when
        job.increaseFailureCount(3000);

        // then
        assertThat(job.getFailureCount()).isEqualTo(3000);
    }

    @ParameterizedTest(name = "입력 값: {0}")
    @ValueSource(ints = {0, -1})
    @DisplayName("실패 건수 0 이하 입력 시 예외 발생")
    void increaseFailureCount_WhenInvalid_ThenThrow(int count) {
        // given
        CouponBatchJob job = createNewJob(JobStatus.PENDING);

        // when & then
        assertThatThrownBy(() -> job.increaseFailureCount(count))
                .isInstanceOf(InvalidCouponCountException.class)
                .hasMessage(INVALID_COUNT_MSG);
    }


    @Test
    @DisplayName("성공+실패 건수가 총 건수 초과 시 예외 발생")
    void increaseFailureCount_WhenExceedsTotal_ThenThrow() {
        // given
        CouponBatchJob job = createNewJob(JobStatus.PENDING);
        job.increaseSuccessCount(TOTAL_COUNT - 1);
        int count = 2;

        // when & then
        assertThatThrownBy(() -> job.increaseFailureCount(count))
                .isInstanceOf(InvalidCouponCountException.class)
                .hasMessage(INVALID_COUNT_MSG);
    }

    @Test
    @DisplayName("성공+실패 건수가 총 건수와 같으면 정상 처리")
    void increaseFailureCount_WhenEqualTotal_ThenSuccess() {
        // given
        CouponBatchJob job = createNewJob(JobStatus.PENDING);
        int successCount = 5000;
        job.increaseSuccessCount(successCount);
        int failureCount = TOTAL_COUNT - successCount;

        // when
        job.increaseFailureCount(failureCount);

        //then
        assertThat(job.getSuccessCount() + job.getFailureCount()).isEqualTo(TOTAL_COUNT);
        assertThat(job.getFailureCount()).isEqualTo(failureCount);
        assertThat(job.getSuccessCount()).isEqualTo(successCount);
    }


}