package com.psjw.coupon.common.bloom;

import com.google.common.hash.BloomFilter;
import org.springframework.util.Assert;

public class GuavaBloomFilterService implements BloomFilterService {
    private final BloomFilter<String> bloomFilter;
    private static final String INVALID_EXPECTED_INSERTIONS_MESSAGE = "expectedInsertions must be greater than 0";
    private static final String INVALID_FPP_MESSAGE = "False positive probability must be > 0.0 and < 1.0";
    private static final String INVALID_IS_NULL_MESSAGE = "BloomFilter value must not be null";

    public GuavaBloomFilterService(int expectedInsertions, double fpp) {
        Assert.isTrue(expectedInsertions > 0, INVALID_EXPECTED_INSERTIONS_MESSAGE);
        Assert.isTrue(fpp > 0.0 && fpp < 1.0, INVALID_FPP_MESSAGE);
        this.bloomFilter = BloomFilterFactory.create(expectedInsertions, fpp);
    }

    @Override
    public void put(String value) {
        Assert.notNull(value, INVALID_IS_NULL_MESSAGE);
        bloomFilter.put(value);
    }

    @Override
    public boolean contains(String value) {
        Assert.notNull(value, INVALID_IS_NULL_MESSAGE);
        return bloomFilter.mightContain(value);
    }
}
