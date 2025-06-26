package com.psjw.coupon.common.error.id;

public class InvalidNodeIdException extends RuntimeException{
    public InvalidNodeIdException(long nodeId, long maxNodeId) {
        super("Allowed nodeId range is [0, " + maxNodeId + "], but was " + nodeId);
    }
}
