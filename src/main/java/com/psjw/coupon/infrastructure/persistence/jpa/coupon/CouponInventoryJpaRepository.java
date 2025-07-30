package com.psjw.coupon.infrastructure.persistence.jpa.coupon;

import com.psjw.coupon.domain.model.coupon.CouponInventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponInventoryJpaRepository extends JpaRepository<CouponInventory, Long> {
}
