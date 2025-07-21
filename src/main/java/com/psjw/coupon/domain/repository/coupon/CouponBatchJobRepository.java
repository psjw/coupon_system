package com.psjw.coupon.domain.repository.coupon;

import com.psjw.coupon.domain.model.coupon.CouponBatchJob;

import java.util.Optional;

public interface CouponBatchJobRepository {
    CouponBatchJob save(CouponBatchJob couponBatchJob);
    void delete(CouponBatchJob couponBatchJob);
    Optional<CouponBatchJob> findById(Long jobId);
}
