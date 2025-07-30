package com.psjw.coupon.infrastructure.persistence.jpa.coupon;

import com.psjw.coupon.domain.model.coupon.CouponInventory;
import com.psjw.coupon.domain.repository.coupon.CouponInventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaCouponInventoryRepository implements CouponInventoryRepository {

    private final CouponInventoryJpaRepository couponInventoryJpaRepository;

    @Override
    public CouponInventory save(CouponInventory couponInventory) {
        return couponInventoryJpaRepository.save(couponInventory);
    }

    @Override
    public void delete(CouponInventory couponInventory) {
        couponInventoryJpaRepository.delete(couponInventory);
    }

    @Override
    public Optional<CouponInventory> findById(Long inventoryId) {
        return couponInventoryJpaRepository.findById(inventoryId);
    }

    @Override
    public boolean existsById(Long inventoryId) {
        return couponInventoryJpaRepository.existsById(inventoryId);
    }
}
