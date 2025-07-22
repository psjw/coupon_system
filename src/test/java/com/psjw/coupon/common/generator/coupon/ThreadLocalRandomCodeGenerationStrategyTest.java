package com.psjw.coupon.common.generator.coupon;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class ThreadLocalRandomCodeGenerationStrategyTest {
    private CouponCodeGenerator generator;
    private static final String ALLOWED_CHARACTERS_REGEX = "^[ABCDEFGHJKLMNPQRSTUVWXYZ23456789]+$";
    private static final String INVALID_LENGTH_MESSAGE = "Coupon code generation length is only positive";

    @BeforeEach
    void setUp(){
        generator = new ThreadLocalRandomCodeGenerationStrategy();
    }

    @Test
    @DisplayName("쿠폰 길이를 지정하면 해당 길이의 쿠폰이 생성된다.")
    void generate_whenGivenLength_thenReturnCouponWithSameLength() {
        // given
        int length = 8;

        // when
        String generatedCoupon = generator.generate(length);

        // then
        assertThat(generatedCoupon).hasSize(length);
    }


    @Test
    @DisplayName("쿠폰 길이가 0일 경우 예외가 발생해야 한다.")
    void generate_whenLengthIsZero_thenThrowException() {
        // given
        int length = 0;

        // when & then
        assertThatThrownBy(() -> generator.generate(length))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Coupon code generation length is only positive");
    }



    @Test
    @DisplayName("생성된 쿠폰은 허용된 문자만 포함한다.")
    void generate_whenCalled_thenReturnOnlyAllowedCharacters() {
        // given
        int length = 10;

        // when
        String generatedCoupon = generator.generate(length);

        // then
        assertThat(generatedCoupon).matches(ALLOWED_CHARACTERS_REGEX);
        assertThat(generatedCoupon).hasSize(length);
    }

    @Test
    @DisplayName("음수 길이를 요청하면 예외가 발생해야 한다.")
    void generate_whenNegativeLength_thenThrowException() {
        // given
        int length = -5;

        // when & then
        assertThatThrownBy(() -> generator.generate(length))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(INVALID_LENGTH_MESSAGE);
    }

}