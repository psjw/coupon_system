package com.psjw.coupon.common.time;

@FunctionalInterface
public interface TimeProvider {
    long currentTimeMillis();
}
