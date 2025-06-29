package com.psjw.coupon.domain.repository.coupon;

import com.psjw.coupon.domain.model.coupon.CouponBatchJob;

public interface CouponBatchJobRepository {
    CouponBatchJob save(CouponBatchJob couponBatchJob);
    void delete(CouponBatchJob couponBatchJob);
    CouponBatchJob findById(Long jobId);
}
