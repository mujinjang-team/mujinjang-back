package mujinjang.couponsystem.domain.coupon.exception;

import mujinjang.couponsystem.common.exception.BusinessException;
import mujinjang.couponsystem.common.exception.ErrorCode;

public class CouponNotFoundException extends BusinessException {
    public CouponNotFoundException() {
        super(ErrorCode.COUPON_NOT_FOUND);
    }
}
