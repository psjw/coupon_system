package com.psjw.coupon.common.message;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ResponseMessages {
    public static final String COUPON_GENERATE_ACCEPTED = "쿠폰 생성 작업이 수락되었습니다. 상태는 jobId를 통해 조회하세요.";
}
