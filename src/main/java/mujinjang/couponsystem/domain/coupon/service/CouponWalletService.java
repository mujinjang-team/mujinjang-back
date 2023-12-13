package mujinjang.couponsystem.domain.coupon.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import mujinjang.couponsystem.domain.coupon.dto.response.CouponUsageStatusResponse;
import mujinjang.couponsystem.domain.coupon.dto.response.IssueCouponWalletResponse;
import reactor.core.publisher.Mono;

public interface CouponWalletService {
	Mono<IssueCouponWalletResponse> issueCoupon(Long userId, Long couponId);

	Mono<Page<CouponUsageStatusResponse>> getCouponUsageStatus(Pageable pageable);

	Mono<Void> useCoupon(Long couponWalletId);
}
