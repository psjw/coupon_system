package com.psjw.coupon.common.config;

import com.psjw.coupon.common.generator.coupon.CouponCodeGenerator;
import com.psjw.coupon.common.generator.coupon.ThreadLocalRandomCodeGenerationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CodeGenerationStrategyConfig {

    @Bean
    public CouponCodeGenerator couponCodeGenerator() {
        return new ThreadLocalRandomCodeGenerationStrategy();
    }
}
