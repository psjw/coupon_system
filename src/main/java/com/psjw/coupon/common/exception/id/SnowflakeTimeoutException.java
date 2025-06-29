package com.psjw.coupon.common.exception.id;

public class SnowflakeTimeoutException extends RuntimeException {
    public SnowflakeTimeoutException(String message) {
        super(message);
    }
}
