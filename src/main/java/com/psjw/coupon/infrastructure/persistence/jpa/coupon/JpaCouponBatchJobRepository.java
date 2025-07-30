package com.psjw.coupon.infrastructure.persistence.jpa.coupon;

import com.psjw.coupon.domain.model.coupon.CouponBatchJob;
import com.psjw.coupon.domain.repository.coupon.CouponBatchJobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaCouponBatchJobRepository implements CouponBatchJobRepository {
    private final CouponBatchJobJpaRepository couponBatchJobJpaRepository;

    @Override
    public CouponBatchJob save(CouponBatchJob couponBatchJob) {
        return couponBatchJobJpaRepository.save(couponBatchJob);
    }

    @Override
    public void delete(CouponBatchJob couponBatchJob) {
        couponBatchJobJpaRepository.delete(couponBatchJob);
    }

    @Override
    public Optional<CouponBatchJob> findById(Long jobId) {
        return couponBatchJobJpaRepository.findById(jobId);
    }
}
