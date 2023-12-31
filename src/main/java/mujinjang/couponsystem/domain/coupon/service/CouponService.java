package mujinjang.couponsystem.domain.coupon.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import mujinjang.couponsystem.domain.coupon.dto.request.CreateCouponRequest;
import mujinjang.couponsystem.domain.coupon.dto.response.CouponInfoResponse;
import mujinjang.couponsystem.domain.coupon.dto.response.CreateCouponResponse;
import reactor.core.publisher.Mono;

public interface CouponService {
	Mono<CreateCouponResponse> createCoupon(CreateCouponRequest dto);

	Mono<Page<CouponInfoResponse>> getCouponsInfo(Pageable pageable);

	Mono<CouponInfoResponse> getCouponInfo(Long couponId);

	Mono<Boolean> isCouponRemain(Long couponId);
}
