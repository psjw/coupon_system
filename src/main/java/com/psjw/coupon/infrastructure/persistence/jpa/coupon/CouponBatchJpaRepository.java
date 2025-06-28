package com.psjw.coupon.infrastructure.persistence.jpa.coupon;

import com.psjw.coupon.domain.model.coupon.CouponBatch;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponBatchJpaRepository extends JpaRepository<CouponBatch, Long> {

    List<CouponBatch> findAllByCouponInventory_Id(Long inventoryId);
}
