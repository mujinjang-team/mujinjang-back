package mujinjang.couponsystem.domain.coupon.service.impl;

import static mujinjang.couponsystem.common.utils.StringToByteBufferUtils.*;

import java.nio.ByteBuffer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import mujinjang.couponsystem.common.exception.BusinessException;
import mujinjang.couponsystem.common.exception.ErrorCode;
import mujinjang.couponsystem.domain.coupon.domain.Coupon;
import mujinjang.couponsystem.domain.coupon.domain.CouponWallet;
import mujinjang.couponsystem.domain.coupon.dto.response.CouponUsageStatusResponse;
import mujinjang.couponsystem.domain.coupon.dto.response.IssueCouponWalletResponse;
import mujinjang.couponsystem.domain.coupon.repository.CouponWalletRepository;
import mujinjang.couponsystem.domain.coupon.service.CouponQueryService;
import mujinjang.couponsystem.domain.coupon.service.CouponWalletQueryService;
import mujinjang.couponsystem.domain.coupon.service.CouponWalletService;
import mujinjang.couponsystem.domain.user.domain.User;
import mujinjang.couponsystem.domain.user.service.UserQueryService;
import reactor.core.publisher.Mono;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CouponWalletServiceImpl implements CouponWalletService {

	private final UserQueryService userQueryService;
	private final CouponQueryService couponQueryService;
	private final CouponWalletRepository couponWalletRepository;
	private final CouponWalletQueryService couponWalletQueryService;
	private final ReactiveStringRedisTemplate redisTemplate;

	@Override
	@Transactional
	public Mono<IssueCouponWalletResponse> issueCoupon(final Long userId, final Long couponId) {
		return Mono.zip(userQueryService.getUser(userId), couponQueryService.getCoupon(couponId)).flatMap(tuple -> {
			final User user = tuple.getT1();
			final Coupon coupon = tuple.getT2();
			final ByteBuffer scriptByteBuffer = toByteBuffer("local key = KEYS[1]", "local userId = ARGV[1]",
				"local remainingQuantity = redis.call('SCARD', key)",
				"if remainingQuantity < " + coupon.getAmount() + " then",
				"    return redis.call('SADD', key, userId) > 0", "else", "    return false", "end");
			final ByteBuffer keyByteBuffer = toByteBuffer("coupon:" + coupon.getCode());
			final ByteBuffer userIdByteBuffer = toByteBuffer(userId.toString());
			return redisTemplate.execute(connection -> connection.scriptingCommands()
				.eval(scriptByteBuffer, ReturnType.BOOLEAN, 1, keyByteBuffer, userIdByteBuffer)).flatMap(result -> {
				if ((boolean)result) {
					return couponWalletRepository.save(new CouponWallet(user.getId(), coupon.getId()))
						.flatMap(couponWallet -> Mono.just(IssueCouponWalletResponse.of(couponWallet.getId())));
				} else {
					return Mono.error(new BusinessException(ErrorCode.COUPON_SOLD_OUT));
				}
			}).next();
		});
	}

	@Override
	public Mono<Page<CouponUsageStatusResponse>> getCouponUsageStatus(final Pageable pageable) {
		return couponWalletRepository.findAllCouponUsageStatus(pageable)
			.collectList()
			.zipWith(couponWalletRepository.count())
			.map(p -> new PageImpl<>(p.getT1(), pageable, p.getT2()));
	}

	@Override
	@Transactional
	public Mono<Void> useCoupon(Long couponWalletId) {
		return couponWalletQueryService.getCouponWallet(couponWalletId).flatMap(couponWallet -> {
			couponWallet.useCoupon();
			return couponWalletRepository.save(couponWallet).then();
		});
	}
}
