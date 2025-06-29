package com.psjw.coupon.infrastructure.persistence.jpa.coupon;

import com.psjw.coupon.domain.model.coupon.CouponInventory;
import com.psjw.coupon.domain.repository.coupon.CouponInventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JpaCouponInventoryRepository implements CouponInventoryRepository {

    private final CouponInventoryRepository couponInventoryRepository;

    @Override
    public CouponInventory save(CouponInventory couponInventory) {
        return couponInventoryRepository.save(couponInventory);
    }

    @Override
    public void delete(CouponInventory couponInventory) {
        couponInventoryRepository.delete(couponInventory);
    }

    @Override
    public CouponInventory findById(Long inventoryId) {
        return couponInventoryRepository.findById(inventoryId);
    }

    @Override
    public boolean existsById(Long inventoryId) {
        return couponInventoryRepository.existsById(inventoryId);
    }
}
