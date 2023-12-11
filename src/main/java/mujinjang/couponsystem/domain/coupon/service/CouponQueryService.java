package mujinjang.couponsystem.domain.coupon.service;

import mujinjang.couponsystem.domain.coupon.domain.Coupon;
import reactor.core.publisher.Mono;

public interface CouponQueryService {
    Mono<Coupon> getCoupon(Long couponId);
}
