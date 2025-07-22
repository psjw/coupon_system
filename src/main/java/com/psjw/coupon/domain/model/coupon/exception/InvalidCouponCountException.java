package com.psjw.coupon.domain.model.coupon.exception;

import com.psjw.coupon.common.exception.BaseException;
import com.psjw.coupon.common.exception.ErrorCode;

public class InvalidCouponCountException extends BaseException {
    public InvalidCouponCountException(ErrorCode errorCode) {
        super(errorCode);
    }
}
