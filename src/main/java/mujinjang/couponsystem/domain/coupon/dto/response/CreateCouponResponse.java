package mujinjang.couponsystem.domain.coupon.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record CreateCouponResponse(@Schema(description = "쿠폰 id") Long couponId) {

	public static CreateCouponResponse of(Long couponId) {
		return new CreateCouponResponse(couponId);
	}
}
