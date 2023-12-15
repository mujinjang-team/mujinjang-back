package mujinjang.couponsystem.domain.user.service;

import reactor.core.publisher.Mono;

public interface UserService {
	Mono<Boolean> isCouponIssued(Long userId, Long couponId);

	Mono<Boolean> isCouponUsed(Long userId, Long couponId);
}
