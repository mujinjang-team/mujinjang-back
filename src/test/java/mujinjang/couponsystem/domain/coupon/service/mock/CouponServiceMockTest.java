package mujinjang.couponsystem.domain.coupon.service.mock;

import static mujinjang.couponsystem.domain.coupon.domain.CouponType.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import mujinjang.couponsystem.domain.coupon.domain.Coupon;
import mujinjang.couponsystem.domain.coupon.exception.CouponNotFoundException;
import mujinjang.couponsystem.domain.coupon.repository.CouponRepository;
import mujinjang.couponsystem.domain.coupon.service.CouponQueryService;
import mujinjang.couponsystem.domain.coupon.service.impl.CouponServiceImpl;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class CouponServiceMockTest {
	@InjectMocks
	private CouponServiceImpl couponService;
	@Mock
	private CouponQueryService couponQueryService;

	@Mock
	private CouponRepository couponRepository;

	@Test
	void isCouponRemainTest_whenCouponExistAndAmountGreaterThanZero() {
		Long couponId = 1L;
		Coupon coupon = Coupon.builder()
			.name("couponName")
			.code("couponCode")
			.type(PERCENTAGE)
			.discount(20.0)
			.amount(100L)
			.build();

		when(couponQueryService.getCoupon(couponId)).thenReturn(Mono.just(coupon));

		StepVerifier.create(couponService.isCouponRemain(couponId))
			.expectNext(true)
			.verifyComplete();
	}

	@Test
	void isCouponRemainTest_whenCouponExistAndAmountZero() {
		Long couponId = 1L;
		Coupon coupon = Coupon.builder()
			.name("couponName")
			.code("couponCode")
			.type(PERCENTAGE)
			.discount(20.0)
			.amount(0L)
			.build();

		when(couponQueryService.getCoupon(couponId)).thenReturn(Mono.just(coupon));

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

