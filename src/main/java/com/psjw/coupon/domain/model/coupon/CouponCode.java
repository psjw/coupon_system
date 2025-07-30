package com.psjw.coupon.domain.model.coupon;

import com.psjw.coupon.common.entity.BaseAuditEntity;
import com.psjw.coupon.domain.enums.coupon.CodeStatus;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_coupon_code_coupon_batch", columnNames = {"coupon_code", "batch_id"})
        }
)
@BatchSize(size = 1_000)
public class CouponCode extends BaseAuditEntity {

    @Id
    @Column(name = "coupon_id")
    private Long id;

    @Column(length = 16, nullable = false)
    private String couponCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id", nullable = false)
    private CouponBatch couponBatch;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CodeStatus codeStatus;


    @Column(nullable = false)
    private LocalDateTime issuedAt;

    private LocalDateTime expiredAt;

    @OneToOne(mappedBy = "couponCode")
    private CouponMapping couponMapping;

    @Builder(access = AccessLevel.PROTECTED)
    private CouponCode(Long id, String couponCode, CouponBatch couponBatch, CodeStatus codeStatus, LocalDateTime issuedAt, LocalDateTime expiredAt, CouponMapping couponMapping) {
        this.id = id;
        this.couponCode = couponCode;
        this.couponBatch = couponBatch;
        this.codeStatus = codeStatus;
        this.issuedAt = issuedAt;
        this.expiredAt = expiredAt;
        this.couponMapping = couponMapping;
    }

    public static CouponCode createCoupon(Long id, String code, CouponBatch batch) {
        return CouponCode.builder()
                .id(id)
                .couponCode(code)
                .couponBatch(batch)
                .codeStatus(CodeStatus.AVAILABLE)
                .issuedAt(LocalDateTime.now())
                .build();
    }

}
