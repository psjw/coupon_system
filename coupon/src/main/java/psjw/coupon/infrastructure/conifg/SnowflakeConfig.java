package psjw.coupon.infrastructure.conifg;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import psjw.coupon.common.id.IdGenerator;
import psjw.coupon.common.time.TimeProvider;
import psjw.coupon.infrastructure.support.id.SnowflakeGenerator;
import psjw.coupon.infrastructure.support.id.SnowflakeProperties;

import java.time.Instant;

@Configuration
@EnableConfigurationProperties(SnowflakeProperties.class)
public class SnowflakeConfig {
    @Bean
    public IdGenerator idGenerator(SnowflakeProperties snowflakeProperties, TimeProvider timeProvider) {
        return new SnowflakeGenerator(snowflakeProperties, snowflakeProperties.getNodeId(), timeProvider);
    }
}
