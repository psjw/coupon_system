package com.psjw.coupon.domain.model.coupon;

import com.psjw.coupon.common.entity.BaseAuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PartnerCoupon extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "partner_coupon_id")
    private Long id;

    @Column(length = 16, nullable = false)
    private String partnerCouponCode;

    @Column(length = 100)
    private String originalFileName;

    @Column(length = 100)
    private String uploadFileName;

    @Column(length = 100)
    private String uploadPath;

    private LocalDateTime uploadedAt;

    private String uploadedBy;

    @OneToOne(mappedBy = "partnerCoupon")
    private CouponMapping couponMapping;

}
