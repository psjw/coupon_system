package psjw.coupon.domain.model.coupon;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import psjw.coupon.common.entity.BaseAuditEntity;
import psjw.coupon.domain.enumtype.coupon.InventoryStatus;
import psjw.coupon.domain.model.event.Event;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CouponInventory extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(nullable = false)
    private Integer totalCount;

    @Column(nullable = false)
    private LocalDateTime usableFormAt;

    @Column(nullable = false)
    private LocalDateTime usableUntilAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InventoryStatus inventoryStatus;

}
