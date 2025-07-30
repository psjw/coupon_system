package com.psjw.coupon.domain.repository.coupon;

import com.psjw.coupon.domain.model.coupon.CouponInventory;

import java.util.Optional;

public interface CouponInventoryRepository {
    CouponInventory save(CouponInventory couponInventory);
    void delete(CouponInventory couponInventory);

    Optional<CouponInventory> findById(Long inventoryId);
    boolean existsById(Long inventoryId);
}
