package psjw.coupon.domain.model.coupon;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import psjw.coupon.common.entity.BaseAuditEntity;
import psjw.coupon.domain.enumtype.coupon.MappingStatus;

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
