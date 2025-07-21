package com.psjw.coupon.domain.model.coupon.exception;

import com.psjw.coupon.common.exception.BaseException;
import com.psjw.coupon.common.exception.ErrorCode;

public class CouponBatchNotFoundException extends BaseException {
    public CouponBatchNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
