package mujinjang.couponsystem.domain.coupon.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record IssueCouponWalletResponse(@Schema(description = "쿠폰 지갑 id") Long couponWalletId) {

	public static IssueCouponWalletResponse of(Long couponWalletId) {
		return new IssueCouponWalletResponse(couponWalletId);
	}
}
