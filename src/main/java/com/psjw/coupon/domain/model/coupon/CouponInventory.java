package com.psjw.coupon.domain.model.coupon;

import com.psjw.coupon.common.entity.BaseAuditEntity;
import com.psjw.coupon.domain.enums.coupon.InventoryStatus;
import com.psjw.coupon.domain.model.event.Event;
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
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column(length = 100, nullable = false)
    private String inventoryName;

    @Column(nullable = false)
    private Integer totalCount;

    @Column(nullable = false)
    private LocalDateTime usableFormAt;

    @Column(nullable = false)
    private LocalDateTime usableUntilAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InventoryStatus inventoryStatus;

    @Column(nullable = false)
    private Boolean isDeleted;

    @Column
    private LocalDateTime isDeletedAt;

    public void delete(){
        if(this.isDeleted){
            throw new IllegalStateException("이미 삭제된 쿠폰 재고 입니다.");
        }
        if(cannotDelete()){
            throw new IllegalStateException("허용되지 않는 상태 변경입니다.");
        }
        this.isDeleted = true;
        this.isDeletedAt = LocalDateTime.now();
    }

    private boolean cannotDelete() {
        return this.inventoryStatus == InventoryStatus.ONGOING  || this.inventoryStatus == InventoryStatus.END;
    }
}
