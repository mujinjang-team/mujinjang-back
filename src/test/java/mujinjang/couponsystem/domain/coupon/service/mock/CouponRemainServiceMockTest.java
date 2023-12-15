package mujinjang.couponsystem.domain.coupon.service.mock;

import static mujinjang.couponsystem.domain.coupon.domain.CouponType.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.ReactiveSetOperations;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;

import mujinjang.couponsystem.domain.coupon.domain.Coupon;
import mujinjang.couponsystem.domain.coupon.exception.CouponNotFoundException;
import mujinjang.couponsystem.domain.coupon.service.CouponQueryService;
import mujinjang.couponsystem.domain.coupon.service.impl.CouponServiceImpl;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class CouponRemainServiceMockTest {
	@InjectMocks
	private CouponServiceImpl couponService;
	@Mock
	private CouponQueryService couponQueryService;
	@Mock
	private ReactiveStringRedisTemplate redisTemplate;
	@Mock
	private ReactiveSetOperations<String, String> reactiveSetOperations;

	@Test
	void isCouponRemainTest_whenCouponExistAndAmountGreaterThanZero() {
		Long couponId = 1L;
		String couponCode = "couponCode";
		Long issued = 50L;

		Coupon coupon = Coupon.builder()
			.name("couponName")
			.code(couponCode)
			.type(PERCENTAGE)
			.discount(20.0)
			.amount(100L)
			.build();

		when(couponQueryService.getCoupon(couponId)).thenReturn(Mono.just(coupon));
		when(redisTemplate.opsForSet()).thenReturn(reactiveSetOperations);
		when(reactiveSetOperations.size("coupon:" + couponCode)).thenReturn(Mono.just(issued));

		StepVerifier.create(couponService.isCouponRemain(couponId))
			.expectNext(true)
			.verifyComplete();
	}

	@Test
	void isCouponRemainTest_whenCouponExistAndAmountZero() {
		Long couponId = 1L;
		String couponCode = "couponCode";
		Long issued = 50L;

		Coupon coupon = Coupon.builder()
			.name("couponName")
			.code(couponCode)
			.type(PERCENTAGE)
			.discount(20.0)
			.amount(50L)
			.build();

		when(couponQueryService.getCoupon(couponId)).thenReturn(Mono.just(coupon));
		when(redisTemplate.opsForSet()).thenReturn(reactiveSetOperations);
		when(reactiveSetOperations.size("coupon:" + couponCode)).thenReturn(Mono.just(issued));

		StepVerifier.create(couponService.isCouponRemain(couponId))
			.expectNext(false)
			.verifyComplete();
	}

	@Test
	void isCouponRemainTest_whenCouponNotExist() {
		Long couponId = 1L;

		when(couponQueryService.getCoupon(couponId))
			.thenReturn(Mono.error(new CouponNotFoundException()));

		StepVerifier.create(couponService.isCouponRemain(couponId))
			.expectError(CouponNotFoundException.class)
			.verify();
	}

}

