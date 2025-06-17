package psjw.coupon.common.time;

import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class InstantTimeProvider implements TimeProvider {
    @Override
    public long currentTimeMillis() {
        return Instant.now().toEpochMilli();
    }
}
