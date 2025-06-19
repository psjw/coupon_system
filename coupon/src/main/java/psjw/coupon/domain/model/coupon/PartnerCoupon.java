package psjw.coupon.domain.model.coupon;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import psjw.coupon.common.entity.BaseAuditEntity;

import java.time.LocalDateTime;

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

    @OneToOne(mappedBy = "couponMapping")
    private CouponMapping couponMapping;

}
