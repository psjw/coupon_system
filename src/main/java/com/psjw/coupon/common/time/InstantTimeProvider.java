package com.psjw.coupon.common.time;

import java.time.Instant;
import org.springframework.stereotype.Component;

@Component
public class InstantTimeProvider implements TimeProvider {
    @Override
    public long currentTimeMillis() {
        return Instant.now().toEpochMilli();
    }
}
