package mujinjang.couponsystem.domain.coupon.dto.response;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

public record CouponUsageStatusResponse(@Schema(description = "유저 id") Long userId,
										@Schema(description = "유저 이름") String username,
										@Schema(description = "유저가 쿠폰을 발급한 시각") LocalDateTime couponIssuedAt,
										@Schema(description = "유저가 쿠폰을 사용한 시각", nullable = true)
										LocalDateTime couponUsedAt) {

}
