package com.psjw.coupon.infrastructure.support.id;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "snowflake")
@Getter
@Setter
@NoArgsConstructor
public class SnowflakeProperties {
    private long epoch;
    private long nodeId;
    private long timeoutMs;
    private long maxAttempts;
}
