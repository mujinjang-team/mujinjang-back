package mujinjang.couponsystem.domain.coupon.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import mujinjang.couponsystem.domain.coupon.domain.Coupon;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CouponRepository extends R2dbcRepository<Coupon, Long> {

    Flux<Coupon> findAllBy(Pageable pageable);

    Mono<Coupon> findByCode(String code);
}
