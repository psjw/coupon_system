package com.psjw.coupon.infrastructure.persistence.jpa.coupon;

import com.psjw.coupon.domain.model.coupon.CouponBatchJob;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponBatchJobJpaRepository extends JpaRepository<CouponBatchJob, Long> {

}
