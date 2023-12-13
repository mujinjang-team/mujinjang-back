package mujinjang.couponsystem.domain.user.exception;

import mujinjang.couponsystem.common.exception.BusinessException;
import mujinjang.couponsystem.common.exception.ErrorCode;

public class UserNotFoundException extends BusinessException {

	public UserNotFoundException() {
		super(ErrorCode.USER_NOT_FOUND);
	}
}
