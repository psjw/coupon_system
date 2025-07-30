package com.psjw.coupon.domain.model.coupon;

import com.psjw.coupon.common.entity.BaseAuditEntity;
import com.psjw.coupon.domain.enums.coupon.BatchStatus;
import com.psjw.coupon.domain.enums.coupon.IssueType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_inventory_batch_number", columnNames = {"inventory_id", "batch_number"})

        }
)
@Getter
public class CouponBatch extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "batch_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_id", nullable = false)
    private CouponInventory couponInventory;

    @Column(length = 100, nullable = false)
    private String batchName;

    @Column(nullable = false)
    private Integer batchNumber;

    @Column(nullable = false)
    private Integer issueCount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IssueType issueType;

    @Column(nullable = false)
    private LocalDateTime validFromAt;

    @Column(nullable = false)
    private LocalDateTime validUntilAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BatchStatus batchStatus;

    @Builder(access = AccessLevel.PROTECTED)
    private CouponBatch(Long id, CouponInventory couponInventory, String batchName, Integer batchNumber, Integer issueCount, IssueType issueType, LocalDateTime validFromAt, LocalDateTime validUntilAt, BatchStatus batchStatus) {
        this.id = id;
        this.couponInventory = couponInventory;
        this.batchName = batchName;
        this.batchNumber = batchNumber;
        this.issueCount = issueCount;
        this.issueType = issueType;
        this.validFromAt = validFromAt;
        this.validUntilAt = validUntilAt;
        this.batchStatus = batchStatus;
    }

    public static CouponBatch of(CouponInventory couponInventory,
                                 String batchName,
                                 Integer batchNumber,
                                 Integer issueCount,
                                 IssueType issueType,
                                 LocalDateTime validFromAt,
                                 LocalDateTime validUntilAt,
                                 BatchStatus batchStatus) {
        return CouponBatch.builder()
                .couponInventory(couponInventory)
                .batchName(batchName)
                .batchNumber(batchNumber)
                .issueCount(issueCount)
                .issueType(issueType)
                .validFromAt(validFromAt)
                .validUntilAt(validUntilAt)
                .batchStatus(batchStatus)
                .build();
    }
}
