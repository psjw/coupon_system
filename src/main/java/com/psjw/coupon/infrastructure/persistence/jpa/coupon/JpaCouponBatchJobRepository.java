package com.psjw.coupon.infrastructure.persistence.jpa.coupon;

import com.psjw.coupon.domain.model.coupon.CouponBatchJob;
import com.psjw.coupon.domain.repository.coupon.CouponBatchJobRepository;
import com.psjw.coupon.domain.repository.coupon.CouponBatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JpaCouponBatchJobRepository implements CouponBatchJobRepository {
    private final CouponBatchJobRepository couponBatchJobRepository;

    @Override
    public CouponBatchJob save(CouponBatchJob couponBatchJob) {
        return couponBatchJobRepository.save(couponBatchJob);
    }

    @Override
    public void delete(CouponBatchJob couponBatchJob) {
        couponBatchJobRepository.delete(couponBatchJob);
    }

    @Override
    public CouponBatchJob findById(Long jobId) {
        return couponBatchJobRepository.findById(jobId);
    }
}
