package com.psjw.coupon.domain.repository.coupon;

import com.psjw.coupon.domain.model.coupon.CouponInventory;

public interface CouponInventoryRepository {
    CouponInventory save(CouponInventory couponInventory);
    void delete(CouponInventory couponInventory);

    CouponInventory findById(Long inventoryId);
    boolean existsById(Long inventoryId);
}
