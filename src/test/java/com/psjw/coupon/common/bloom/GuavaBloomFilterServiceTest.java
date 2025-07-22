package com.psjw.coupon.common.bloom;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class GuavaBloomFilterServiceTest {
    private BloomFilterService bloomFilterService;

    private static final String INVALID_EXPECTED_INSERTIONS_MESSAGE = "expectedInsertions must be greater than 0";
    private static final String INVALID_FPP_MESSAGE = "False positive probability must be > 0.0 and < 1.0";
    private static final String INVALID_IS_NULL_MESSAGE = "BloomFilter value must not be null";

    @BeforeEach
    void setUp() {
        bloomFilterService = new GuavaBloomFilterService(1_000, 0.01);
    }

    @Test
    @DisplayName("쿠폰 값이 필터에 존재하면 true를 반환한다.")
    void contains_whenValueExists_thenReturnTrue() {
        // given
        String coupon = "ABCDEFG";
        bloomFilterService.put(coupon);

        // when
        boolean result = bloomFilterService.contains(coupon);

        // then
        assertTrue(result);
    }


    @Test
    @DisplayName("존재하지 않는 쿠폰값은 false를 반환한다.")
    void contains_whenValueNotExists_thenReturnFalse() {
        // given
        String coupon = "ZZZZZZZ";

        // when
        boolean contains = bloomFilterService.contains(coupon);

        // then
        assertFalse(contains);
    }

    @ParameterizedTest(name = "expectedInsertions = {0}일 때 예외 발생")
    @ValueSource(ints = {0, -10})
    @DisplayName("expectedInsertions가 0 이하이면 예외가 발생한다.")
    void constructor_whenInvalidExpectedInsertions_thenThrowException(int invalidValue) {
        assertThatThrownBy(() -> new GuavaBloomFilterService(invalidValue, 0.01))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(INVALID_EXPECTED_INSERTIONS_MESSAGE);
    }

    @ParameterizedTest(name = "fpp = {0}일 때 예외 발생")
    @ValueSource(doubles = {0.0, -0.01, 1.0, 1.01})
    @DisplayName("fpp가 0.0 이하이거나 1.0 이상이면 예외가 발생한다.")
    void constructor_whenInvalidFpp_thenThrowException(double invalidFpp) {
        assertThatThrownBy(() -> new GuavaBloomFilterService(1_000, invalidFpp))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(INVALID_FPP_MESSAGE);
    }

    @Test
    @DisplayName("put 함수에 null이 들어오면 예외가 발생한다.")
    void put_whenCouponPutIsNull_thenThrowException() {
        // given
        String coupon = null;
        // when & then
        assertThatThrownBy(() -> bloomFilterService.put(coupon))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(INVALID_IS_NULL_MESSAGE);
    }

    @Test
    @DisplayName("contains 함수에 null이 들어오면 예외가 발생한다.")
    void contains_whenCouponContainsIsNull_thenThrowException() {
        // given
        String coupon = null;
        // when & then
        assertThatThrownBy(() -> bloomFilterService.contains(coupon))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(INVALID_IS_NULL_MESSAGE);
    }


}