package mujinjang.couponsystem.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // System
    INTERNAL_SERVER_ERROR(500, "S001", "서버에 오류가 발생하였습니다."),

    // Coupon
    COUPON_NOT_FOUND(404, "C001", "쿠폰을 찾을 수 없습니다."),
    COUPON_SOLD_OUT(409, "C002", "쿠폰이 모두 소진되었습니다."),
    COUPON_CODE_DUPLICATED(409, "C003", "쿠폰 코드가 중복되었습니다."),

    // User
    USER_NOT_FOUND(404, "U001", "사용자를 찾을 수 없습니다."),

    // CouponWallet
    COUPON_WALLET_NOT_FOUND(404, "CW001", "쿠폰 지갑을 찾을 수 없습니다.");

    private final int status;
    private final String code;
    private final String message;
}
