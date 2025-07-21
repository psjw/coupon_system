package com.psjw.coupon.domain.model.coupon.exception;

import com.psjw.coupon.common.exception.BaseException;
import com.psjw.coupon.common.exception.ErrorCode;

public class DuplicateBatchJobRequestException extends BaseException {
    public DuplicateBatchJobRequestException(ErrorCode errorCode) {
        super(errorCode);
    }
}
