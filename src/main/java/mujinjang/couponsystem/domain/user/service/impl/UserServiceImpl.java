package mujinjang.couponsystem.domain.user.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import mujinjang.couponsystem.domain.coupon.exception.CouponWalletNotFoundException;
import mujinjang.couponsystem.domain.coupon.repository.CouponWalletRepository;
import mujinjang.couponsystem.domain.coupon.service.CouponQueryService;
import mujinjang.couponsystem.domain.user.service.UserQueryService;
import mujinjang.couponsystem.domain.user.service.UserService;
import reactor.core.publisher.Mono;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final CouponWalletRepository couponWalletRepository;
	private final UserQueryService userQueryService;
	private final CouponQueryService couponQueryService;

	@Override
	public Mono<Boolean> isCouponIssued(Long userId, Long couponId) {
		return Mono.zip(
				userQueryService.getUser(userId),
				couponQueryService.getCoupon(couponId))
			.then(couponWalletRepository.findByUserIdAndCouponId(userId, couponId)
				.map(couponWallet -> true)
				.defaultIfEmpty(false));
	}

	@Override
	public Mono<Boolean> isCouponUsed(Long userId, Long couponId) {
		return Mono.zip(
				userQueryService.getUser(userId),
				couponQueryService.getCoupon(couponId))
			.then(couponWalletRepository.findByUserIdAndCouponId(userId, couponId)
				.map(couponWallet -> couponWallet.getUsedAt() != null))
			.switchIfEmpty(Mono.error(new CouponWalletNotFoundException()));
	}

}

