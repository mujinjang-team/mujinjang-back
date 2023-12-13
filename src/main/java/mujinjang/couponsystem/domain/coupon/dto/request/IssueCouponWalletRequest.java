package mujinjang.couponsystem.domain.coupon.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record IssueCouponWalletRequest(@Schema(description = "유저 id") @NotNull Long userId,
									   @Schema(description = "쿠폰 id") @NotNull Long couponId) {
}
