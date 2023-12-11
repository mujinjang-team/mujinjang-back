package mujinjang.couponsystem.domain.coupon.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import mujinjang.couponsystem.domain.coupon.domain.Coupon;
import mujinjang.couponsystem.domain.coupon.exception.CouponNotFoundException;
import mujinjang.couponsystem.domain.coupon.repository.CouponRepository;
import mujinjang.couponsystem.domain.coupon.service.CouponQueryService;
import reactor.core.publisher.Mono;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CouponQueryServiceImpl implements CouponQueryService {

    private final CouponRepository couponRepository;

    @Override
    public Mono<Coupon> getCoupon(final Long couponId) {
        return couponRepository.findById(couponId)
                .switchIfEmpty(Mono.error(new CouponNotFoundException()));
    }

}
