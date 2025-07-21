package com.psjw.coupon.common.bloom;

import com.google.common.hash.BloomFilter;

public class GuavaBloomFilterService implements BloomFilterService {
    private final BloomFilter<String> bloomFilter;

    public GuavaBloomFilterService(int expectedInsertions, double fpp) {
        this.bloomFilter = BloomFilterFactory.create(expectedInsertions, fpp);
    }

    @Override
    public void put(String value) {
        bloomFilter.put(value);
    }

    @Override
    public boolean contains(String value) {
        return bloomFilter.mightContain(value);
    }
}
