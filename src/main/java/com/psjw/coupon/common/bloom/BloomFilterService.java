package com.psjw.coupon.common.bloom;

public interface BloomFilterService {
    void put(String value);
    boolean contains(String value);
}
