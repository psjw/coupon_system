package com.psjw.coupon.domain.model.coupon;


import com.psjw.coupon.common.entity.BaseAuditEntity;
import com.psjw.coupon.common.exception.ErrorCode;
import com.psjw.coupon.domain.enums.coupon.JobStatus;
import com.psjw.coupon.domain.enums.coupon.JobType;
import com.psjw.coupon.domain.model.coupon.exception.InvalidCouponCountException;
import com.psjw.coupon.domain.model.coupon.exception.InvalidStatusTransitionException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;
import java.util.Arrays;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CouponBatchJob extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id", nullable = false)
    private CouponBatch couponBatch;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobType jobType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobStatus jobStatus;

    @Column(nullable = false)
    private String requestedBy;

    @Column(nullable = false)
    private LocalDateTime requestedAt;

    private LocalDateTime completedAt;

    @Column(nullable = false)
    private Integer totalCount;

    @Column(nullable = false)
    private Integer successCount;

    @Column(nullable = false)
    private Integer failureCount;

    @Lob
    private String errorMessage;

    private String errorLogPath;

    public boolean isInProgress() {
        return this.jobStatus == JobStatus.IN_PROGRESS;
    }

    @Builder
    private CouponBatchJob(CouponBatch couponBatch, JobType jobType, JobStatus jobStatus, String requestedBy, LocalDateTime requestedAt, LocalDateTime completedAt, Long id, Integer totalCount, Integer successCount, Integer failureCount, String errorMessage, String errorLogPath) {
        this.couponBatch = couponBatch;
        this.jobType = jobType;
        this.jobStatus = jobStatus;
        this.requestedBy = requestedBy;
        this.requestedAt = requestedAt;
        this.completedAt = completedAt;
        this.id = id;
        this.totalCount = totalCount;
        this.successCount = successCount;
        this.failureCount = failureCount;
        this.errorMessage = errorMessage;
        this.errorLogPath = errorLogPath;
    }

    public static CouponBatchJob createNew(CouponBatch batch, JobType type, String requestedBy, int totalCount) {
        return CouponBatchJob.builder()
                .couponBatch(batch)
                .jobType(type)
                .jobStatus(JobStatus.PENDING)
                .totalCount(totalCount)
                .requestedBy(requestedBy)
                .requestedAt(LocalDateTime.now())
                .failureCount(0)
                .successCount(0)
                .build();
    }

    public void increaseSuccessCount(int count) {
        if (count <= 0) {
            throw new InvalidCouponCountException(ErrorCode.INVALID_COUPON_COUNT);
        }
        this.successCount += count;
    }

    public void increaseFailureCount(int count) {
        if (count <= 0 || this.successCount + this.failureCount + count > this.totalCount){
            throw new InvalidCouponCountException(ErrorCode.INVALID_COUPON_COUNT);
        }
        this.failureCount += count;
    }

    public void changeInProgress() {
        assertTransitionAllowed(JobStatus.PENDING);
        this.jobStatus = JobStatus.IN_PROGRESS;
    }

    public void changeCompleted() {
        assertTransitionAllowed(JobStatus.IN_PROGRESS);
        this.jobStatus = JobStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
    }

    public void changeFailed() {
        assertTransitionAllowed(JobStatus.IN_PROGRESS, JobStatus.PENDING);
        this.jobStatus = JobStatus.FAILED;
    }


    public void changeCancel() {
        assertTransitionAllowed(JobStatus.IN_PROGRESS, JobStatus.PENDING);
        this.jobStatus = JobStatus.CANCELED;
    }

    private void assertTransitionAllowed(JobStatus... allowedStatus) {
        boolean isAllowed = Arrays.stream(allowedStatus).anyMatch(jobStatus -> jobStatus.equals(this.jobStatus));

        if (!isAllowed) {
            throw new InvalidStatusTransitionException(ErrorCode.INVALID_STATUS_TRANSITION);
        }
    }
}
