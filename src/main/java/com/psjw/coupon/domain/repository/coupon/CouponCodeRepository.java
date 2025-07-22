package com.psjw.coupon.domain.repository.coupon;

import com.psjw.coupon.domain.model.coupon.CouponBatch;
import com.psjw.coupon.domain.model.coupon.CouponCode;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CouponCodeRepository {
    Optional<CouponCode> findByCouponCode(String couponCode);
    Optional<CouponCode> findById(Long couponId);

    List<CouponCode> findAllByCouponBatch(CouponBatch couponBatch);


    boolean existsByCouponCodeAndCouponBatch(String couponCode, CouponBatch couponBatch);
    boolean existsByCouponCode(String couponCode);

    CouponCode save(CouponCode couponCode);
    List<CouponCode> saveAll(List<CouponCode> couponCodes);
    void delete(CouponCode couponCode);
}
