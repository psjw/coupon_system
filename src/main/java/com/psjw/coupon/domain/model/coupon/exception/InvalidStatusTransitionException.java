package com.psjw.coupon.domain.model.coupon.exception;

import com.psjw.coupon.common.exception.BaseException;
import com.psjw.coupon.common.exception.ErrorCode;

public class InvalidStatusTransitionException extends BaseException {
    public InvalidStatusTransitionException(ErrorCode errorCode) {
        super(errorCode);
    }
}
