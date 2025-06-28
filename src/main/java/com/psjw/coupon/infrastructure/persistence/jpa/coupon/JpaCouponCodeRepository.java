package com.psjw.coupon.infrastructure.persistence.jpa.coupon;

import com.psjw.coupon.domain.model.coupon.CouponBatch;
import com.psjw.coupon.domain.model.coupon.CouponCode;
import com.psjw.coupon.domain.repository.coupon.CouponCodeRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class JpaCouponCodeRepository implements CouponCodeRepository {

    private final CouponCodeJpaRepository couponCodeJpaRepository;

    @Override
    public Optional<CouponCode> findByCouponCode(String couponCode) {
        return couponCodeJpaRepository.findByCouponCode(couponCode);
    }

    @Override
    public Optional<CouponCode> findById(Long couponId) {
        return couponCodeJpaRepository.findById(couponId);
    }

    @Override
    public List<CouponCode> findAllByCouponBatch(CouponBatch couponBatch) {
        return couponCodeJpaRepository.findAllByCouponBatch(couponBatch);
    }

    @Override
    public boolean existsByCouponCodeAndCouponBatch(String couponCode, CouponBatch couponBatch) {
        return couponCodeJpaRepository.existsByCouponCodeAndCouponBatch(couponCode, couponBatch);
    }

    @Override
    public boolean existsByCouponCode(String couponCode) {
        return couponCodeJpaRepository.existsByCouponCode(couponCode);
    }

    @Override
    public CouponCode save(CouponCode couponCode) {
        return couponCodeJpaRepository.save(couponCode);
    }

    @Override
    public void delete(CouponCode couponCode) {
        couponCodeJpaRepository.delete(couponCode);
    }
}
