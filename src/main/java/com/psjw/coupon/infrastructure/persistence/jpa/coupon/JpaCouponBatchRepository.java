package com.psjw.coupon.infrastructure.persistence.jpa.coupon;

import com.psjw.coupon.domain.model.coupon.CouponBatch;
import com.psjw.coupon.domain.model.coupon.CouponInventory;
import com.psjw.coupon.domain.repository.coupon.CouponBatchRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JpaCouponBatchRepository implements CouponBatchRepository {
    private final CouponBatchJpaRepository couponBatchJpaRepository;

    @Override
    public Optional<CouponBatch> findById(Long batchId) {
        return couponBatchJpaRepository.findById(batchId);
    }

    @Override
    public List<CouponBatch> findAllByCouponInventory_Id(Long inventoryId) {
        return couponBatchJpaRepository.findAllByCouponInventory_Id(inventoryId);
    }

    @Override
    public boolean existsById(Long batchId) {
        return couponBatchJpaRepository.existsById(batchId);
    }

    @Override
    public CouponBatch save(CouponBatch couponBatch) {
        return couponBatchJpaRepository.save(couponBatch);
    }

    @Override
    public void delete(CouponBatch couponBatch) {
        couponBatchJpaRepository.delete(couponBatch);
    }
}
