package com.psjw.coupon.infrastructure.persistence.jpa.coupon;

import com.psjw.coupon.domain.model.coupon.CouponBatch;
import com.psjw.coupon.domain.model.coupon.CouponCode;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponCodeJpaRepository extends JpaRepository<CouponCode, Long> {

    Optional<CouponCode> findByCouponCode(String couponCode);

    List<CouponCode> findAllByCouponBatch(CouponBatch couponBatch);

    boolean existsByCouponCodeAndCouponBatch(String couponCode, CouponBatch couponBatch);

    boolean existsByCouponCode(String couponCode);
}
