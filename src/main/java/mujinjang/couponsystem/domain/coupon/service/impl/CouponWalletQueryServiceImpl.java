package mujinjang.couponsystem.domain.coupon.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import mujinjang.couponsystem.domain.coupon.domain.CouponWallet;
import mujinjang.couponsystem.domain.coupon.exception.CouponWalletNotFoundException;
import mujinjang.couponsystem.domain.coupon.repository.CouponWalletRepository;
import mujinjang.couponsystem.domain.coupon.service.CouponWalletQueryService;
import reactor.core.publisher.Mono;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CouponWalletQueryServiceImpl implements CouponWalletQueryService {

	private final CouponWalletRepository couponWalletRepository;

	@Override
	public Mono<CouponWallet> getCouponWallet(Long couponWalletId) {
		return couponWalletRepository.findById(couponWalletId)
			.switchIfEmpty(Mono.error(new CouponWalletNotFoundException()));
	}
}
