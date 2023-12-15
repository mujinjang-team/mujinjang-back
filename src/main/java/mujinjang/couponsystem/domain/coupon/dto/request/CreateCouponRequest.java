package mujinjang.couponsystem.domain.coupon.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import mujinjang.couponsystem.domain.coupon.domain.CouponType;

public record CreateCouponRequest(@Schema(description = "쿠폰 코드") @NotBlank String code,
								  @Schema(description = "쿠폰 이름") @NotBlank String name,
								  @Schema(description = "쿠폰 종류") @NotNull CouponType type,
								  @Schema(description = "할인 정도(정률이면 할인율, 정액이면 할인 가격)", minimum = "0.0001")
								  @NotNull @Positive Double discount,
								  @Schema(description = "쿠폰 발급 양", minimum = "1")
								  @NotNull @Positive Long amount) {
}
