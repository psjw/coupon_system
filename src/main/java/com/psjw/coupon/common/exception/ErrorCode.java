package com.psjw.coupon.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    //공통
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "Invalid Request", "요청 필드가 유효하지 않습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Unauthorized", "인증 정보가 없습니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "Forbidden", "관리자 권한이 필요합니다."),

    //시스템 오류
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error",
            "서버 내부 오류가 발생했습니다."),

    //이벤트 관련
    EVENT_INVALID_DATETIME(HttpStatus.BAD_REQUEST, "Event Invalid Datetime",
            "이벤트 시작 시간은 종료 시간보다 이전이여야 합니다."),
    INVALID_STATUS_TRANSITION(HttpStatus.BAD_REQUEST, "Invalid Status Transition",
            "허용되지 않는 상태 변경입니다."),
    CANNOT_DELETE_EVENT(HttpStatus.BAD_REQUEST, "Cannot Delete Event", "해당 이벤트는 삭제할 수 없습니다."),
    EVENT_NOT_FOUND(HttpStatus.NOT_FOUND, "Event Not Found", "이벤트를 찾을 수 없습니다."),
    DUPLICATE_EVENT(HttpStatus.CONFLICT, "Duplicate Event", "동일한 이름의 이벤트가 이미 실행중입니다."),

    //쿠폰 관련
    MAX_COUPON_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "Max Coupon Limit Exceeded",
            "생성 가능한 최대 쿠폰 수를 초과했습니다."),
    INVENTORY_INVALID_PERIOD(HttpStatus.BAD_REQUEST, "Inventory Invalid Period",
            "쿠폰 유효기간의 시작 시간은 종료 시간보다 이전이어야 합니다."),
    CANNOT_MODIFY_COUPON_INVENTORY(HttpStatus.BAD_REQUEST, "Cannot Modify Coupon Inventory",
            "해당 쿠폰 재고는 수정할 수 없습니다."),
    CANNOT_MODIFY_COUPON_BATCH(HttpStatus.BAD_REQUEST, "Cannot Modify Coupon Batch",
            "해당 회차는 수정할 수 없습니다."),
    CANNOT_DELETE_COUPON_INVENTORY(HttpStatus.BAD_REQUEST, "Cannot Delete Coupon Inventory",
            "이미 생성된 쿠폰 재고는 삭제할 수 없습니다."),
    BATCH_INVALID_PERIOD(HttpStatus.BAD_REQUEST, "Batch Invalid Period",
            "쿠폰 유효기간의 시작 시간은 종료 시간보다 이전이어야 합니다."),
    COUPON_INVENTORY_NOT_FOUND(HttpStatus.NOT_FOUND, "Coupon Inventory Not Found",
            "쿠폰 재고를 찾을 수 없습니다."),
    COUPON_BATCH_NOT_FOUND(HttpStatus.NOT_FOUND, "Coupon Batch Not Found",
            "쿠폰 재고 회차를 찾을 수 없습니다."),
    COUPON_JOB_NOT_FOUND(HttpStatus.NOT_FOUND, "Coupon Job Not Found",
            "쿠폰 셍성 작업을 찾을 수 없습니다."),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
