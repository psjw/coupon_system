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
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_coupon_code_coupon_batch", columnNames = {"coupon_code", "batch_id"})
        }
)
public class CouponCode extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    private LocalDateTime issuedAt;

    private LocalDateTime expiredAt;

    @OneToOne(mappedBy = "couponCode")
    private CouponMapping couponMapping;


}
