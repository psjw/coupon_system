package com.psjw.coupon.common.exception.id;

public class InvalidSystemClockException extends RuntimeException {
    public InvalidSystemClockException(long nodeId, long currentTime) {
        super("System clock is moving backward. Node ID: %d, current timestamp: %d".formatted(nodeId, currentTime));
    }
}
