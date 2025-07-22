package com.psjw.coupon.common.generator.coupon;

import org.springframework.util.Assert;

import java.util.concurrent.ThreadLocalRandom;

public class ThreadLocalRandomCodeGenerationStrategy implements CouponCodeGenerator{
    private static final String COUPON_CODE_POOL = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";

    private static final String INVALID_LENGTH_MESSAGE = "Coupon code generation length is only positive";

    @Override
    public String generate(int length) {
        Assert.isTrue(length > 0, INVALID_LENGTH_MESSAGE);

        ThreadLocalRandom random = ThreadLocalRandom.current();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(COUPON_CODE_POOL.charAt(random.nextInt(COUPON_CODE_POOL.length())));
        }
        return sb.toString();
    }
}
