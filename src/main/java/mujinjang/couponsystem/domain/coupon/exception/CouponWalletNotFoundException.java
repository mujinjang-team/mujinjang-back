package mujinjang.couponsystem.domain.coupon.exception;

import mujinjang.couponsystem.common.exception.BusinessException;
import mujinjang.couponsystem.common.exception.ErrorCode;

public class CouponWalletNotFoundException extends BusinessException {

	public CouponWalletNotFoundException() {
		super(ErrorCode.COUPON_WALLET_NOT_FOUND);
	}
}
