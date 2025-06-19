package psjw.coupon.domain.model.coupon;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import psjw.coupon.common.entity.BaseAuditEntity;
import psjw.coupon.domain.enumtype.coupon.BatchStatus;
import psjw.coupon.domain.enumtype.coupon.IssueType;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CouponBatch extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "batch_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_id", nullable = false)
    private CouponInventory couponInventory;

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


}
