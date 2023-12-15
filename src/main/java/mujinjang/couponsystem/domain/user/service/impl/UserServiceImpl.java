package mujinjang.couponsystem.domain.user.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import mujinjang.couponsystem.domain.coupon.exception.CouponNotFoundException;
import mujinjang.couponsystem.domain.coupon.repository.CouponRepository;
import mujinjang.couponsystem.domain.coupon.repository.CouponWalletRepository;
import mujinjang.couponsystem.domain.user.exception.UserNotFoundException;
import mujinjang.couponsystem.domain.user.repository.UserRepository;
import mujinjang.couponsystem.domain.user.service.UserService;
import reactor.core.publisher.Mono;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final CouponRepository couponRepository;
	private final CouponWalletRepository couponWalletRepository;
	private final UserRepository userRepository;

	@Override
	public Mono<Boolean> isCouponIssued(Long userId, Long couponId) {
		return Mono.zip(
				userRepository.findById(userId)
					.switchIfEmpty(Mono.error(new UserNotFoundException())),
				couponRepository.findById(couponId)
					.switchIfEmpty(Mono.error(new CouponNotFoundException())))
			.then(couponWalletRepository.findByUserIdAndCouponId(userId, couponId)
				.map(couponWallet -> true)
				.defaultIfEmpty(false));
	}

	@Override
	public Mono<Boolean> isCouponUsed(Long userId, Long couponId) {
		return Mono.zip(
				userRepository.findById(userId)
					.switchIfEmpty(Mono.error(new UserNotFoundException())),
				couponRepository.findById(couponId)
					.switchIfEmpty(Mono.error(new CouponNotFoundException())))
			.then(couponWalletRepository.findByUserIdAndCouponId(userId, couponId)
				.map(couponWallet -> couponWallet.getUsedAt() != null));
	}

}

