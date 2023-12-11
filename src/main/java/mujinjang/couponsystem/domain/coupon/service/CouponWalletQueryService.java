package mujinjang.couponsystem.domain.coupon.service;

import mujinjang.couponsystem.domain.coupon.domain.CouponWallet;
import reactor.core.publisher.Mono;

public interface CouponWalletQueryService {
    Mono<CouponWallet> getCouponWallet(Long couponWalletId);
}
