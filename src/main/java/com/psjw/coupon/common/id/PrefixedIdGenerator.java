package com.psjw.coupon.common.id;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PrefixedIdGenerator {
    private final IdGenerator idGenerator;

    public String generateWithPrefix(String prefix) {
        return prefix + idGenerator.generate();
    }
}
