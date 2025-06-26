package com.psjw.coupon.infrastructure.conifg;

import com.psjw.coupon.common.id.IdGenerator;
import com.psjw.coupon.common.time.TimeProvider;
import com.psjw.coupon.infrastructure.support.id.SnowflakeGenerator;
import com.psjw.coupon.infrastructure.support.id.SnowflakeProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(SnowflakeProperties.class)
public class SnowflakeConfig {
    @Bean
    public IdGenerator idGenerator(SnowflakeProperties snowflakeProperties, TimeProvider timeProvider) {
        return new SnowflakeGenerator(snowflakeProperties, snowflakeProperties.getNodeId(), timeProvider);
    }
}
