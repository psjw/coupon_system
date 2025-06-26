package com.psjw.coupon.domain.model.coupon;

import com.psjw.coupon.common.entity.BaseAuditEntity;
import com.psjw.coupon.domain.enums.coupon.MappingStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "uq_partner_coupon_coupon_code", columnNames = {"coupon_id",
                "partner_coupon_id"})
})
public class CouponMapping extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mapping_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_coupon_id", nullable = false)
    private PartnerCoupon partnerCoupon;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private CouponCode couponCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MappingStatus mappingStatus;

}
