package com.psjw.coupon.domain.model.coupon;


import com.psjw.coupon.common.entity.BaseAuditEntity;
import com.psjw.coupon.domain.enums.coupon.JobStatus;
import com.psjw.coupon.domain.enums.coupon.JobType;
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

    @Builder
    public static CouponBatchJob createNew(CouponBatch batch, JobType type, String requestedBy, int totalCount) {
        return CouponBatchJob.builder()
                .couponBatch(batch)
                .jobType(type)
                .jobStatus(JobStatus.PENDING)
                .totalCount(totalCount)
                .requestedBy(requestedBy)
                .failureCount(0)
                .successCount(0)
                .build();
    }
}
