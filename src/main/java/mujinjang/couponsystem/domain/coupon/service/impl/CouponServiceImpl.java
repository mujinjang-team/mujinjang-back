package mujinjang.couponsystem.domain.coupon.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import mujinjang.couponsystem.common.exception.BusinessException;
import mujinjang.couponsystem.common.exception.ErrorCode;
import mujinjang.couponsystem.domain.coupon.domain.Coupon;
import mujinjang.couponsystem.domain.coupon.dto.request.CreateCouponRequest;
import mujinjang.couponsystem.domain.coupon.dto.response.CouponInfoResponse;
import mujinjang.couponsystem.domain.coupon.dto.response.CreateCouponResponse;
import mujinjang.couponsystem.domain.coupon.repository.CouponRepository;
import mujinjang.couponsystem.domain.coupon.service.CouponQueryService;
import mujinjang.couponsystem.domain.coupon.service.CouponService;
import reactor.core.publisher.Mono;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

	private final CouponRepository couponRepository;
	private final CouponQueryService couponQueryService;
	private final ReactiveStringRedisTemplate redisTemplate;

	@Override
	@Transactional
	public Mono<CreateCouponResponse> createCoupon(final CreateCouponRequest dto) {
		return couponRepository.findByCode(dto.code())
			.flatMap(
				coupon -> Mono.<CreateCouponResponse>error(new BusinessException(ErrorCode.COUPON_CODE_DUPLICATED)))
			.switchIfEmpty(Mono.defer(
				() -> createCouponEntity(dto).map(createCoupon -> new CreateCouponResponse(createCoupon.getId())))
			);
	}

	@Override
	public Mono<Page<CouponInfoResponse>> getCouponsInfo(final Pageable pageable) {
		return couponRepository.findAllBy(pageable).flatMap(coupon -> {
			final String key = "coupon:" + coupon.getCode();
			return redisTemplate.opsForSet()
				.size(key)
				.map(issuedCouponNum -> CouponInfoResponse.of(coupon, coupon.getAmount() - issuedCouponNum));
		}).collectList().zipWith(couponRepository.count()).map(p -> new PageImpl<>(p.getT1(), pageable, p.getT2()));
	}

	@Override
	public Mono<CouponInfoResponse> getCouponInfo(Long couponId) {
		return couponQueryService.getCoupon(couponId).flatMap(coupon -> {
			final String key = "coupon:" + coupon.getCode();
			return redisTemplate.opsForSet()
				.size(key)
				.map(issuedCouponNum -> CouponInfoResponse.of(coupon, coupon.getAmount() - issuedCouponNum));
		});
	}

	@Override
	public Mono<Boolean> isCouponRemain(Long couponId) {
		return couponQueryService.getCoupon(couponId).flatMap(coupon -> {
			String key = "coupon:" + coupon.getCode();
			return redisTemplate.opsForSet().size(key).map(issued -> coupon.getAmount() - issued > 0);
		});
	}

	private Mono<Coupon> createCouponEntity(CreateCouponRequest dto) {
		return couponRepository.save(Coupon.builder()
			.name(dto.name())
			.code(dto.code())
			.type(dto.type())
			.discount(dto.discount())
			.amount(dto.amount())
			.build());
	}
}
