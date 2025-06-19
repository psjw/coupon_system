package psjw.coupon.domain.model.coupon;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import psjw.coupon.common.entity.BaseAuditEntity;
import psjw.coupon.domain.enumtype.coupon.CodeStatus;

import java.time.LocalDateTime;

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
