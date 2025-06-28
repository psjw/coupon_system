package com.psjw.coupon.domain.repository.coupon;

import com.psjw.coupon.domain.model.coupon.CouponBatch;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;


@Repository
public interface CouponBatchRepository {

    Optional<CouponBatch> findById(Long batchId);

    List<CouponBatch> findAllByCouponInventory_Id(Long inventoryId);

    boolean existsById(Long batchId);

    CouponBatch save(CouponBatch couponBatch);

    void delete(CouponBatch couponBatch);

}
