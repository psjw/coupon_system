package com.psjw.coupon.domain.model.coupon.event;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CouponGeneratedEvent {
    private Long couponBatchJobId;
    private int successCount;
    private int totalCount;

    @Builder
    private CouponGeneratedEvent(Long couponBatchJobId, int successCount, int totalCount) {
        this.couponBatchJobId = couponBatchJobId;
        this.successCount = successCount;
        this.totalCount = totalCount;
    }

    public static CouponGeneratedEvent of(Long couponBatchJobId, int successCount, int totalCount) {
        return CouponGeneratedEvent.builder()
                .couponBatchJobId(couponBatchJobId)
                .successCount(successCount)
                .totalCount(totalCount)
                .build();
    }
}
