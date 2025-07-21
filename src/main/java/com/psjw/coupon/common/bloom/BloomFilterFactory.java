package com.psjw.coupon.common.bloom;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class BloomFilterFactory {

    public static BloomFilter<String> create(int expectedInsertions, double fpp) {
        return BloomFilter.create(
                Funnels.stringFunnel(StandardCharsets.UTF_8),
                expectedInsertions,
                fpp
        );
    }
}
