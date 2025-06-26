package com.psjw.coupon.common.error.id;

public class SnowflakeTimeoutException extends RuntimeException {
    public SnowflakeTimeoutException(String message) {
        super(message);
    }
}
