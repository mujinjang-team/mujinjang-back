package mujinjang.couponsystem.domain.coupon.service.mock;

import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import mujinjang.couponsystem.common.exception.BusinessException;
import mujinjang.couponsystem.common.exception.ErrorCode;
import mujinjang.couponsystem.domain.coupon.domain.Coupon;
import mujinjang.couponsystem.domain.coupon.domain.CouponType;
import mujinjang.couponsystem.domain.coupon.repository.CouponRepository;
import mujinjang.couponsystem.domain.coupon.service.impl.CouponQueryServiceImpl;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
@DisplayName("getCoupon 메서드는")
public class CouponQueryServiceMockTest {

	@InjectMocks
	private CouponQueryServiceImpl couponQueryService;

	@Mock
	private CouponRepository couponRepository;

	@Test
	@DisplayName("쿠폰이 존재하면 쿠폰을 반환한다.")
	void getCoupon_whenCouponExist() {
		// given
		Coupon coupon = new Coupon("name", "code", CouponType.FIXED, 1D, 1L);
		given(couponRepository.findById(anyLong())).willReturn(Mono.just(coupon));

		// when
		// then
		StepVerifier.create(couponQueryService.getCoupon(1L))
			.expectNextMatches(c -> c.equals(coupon))
			.verifyComplete();
	}

	@Test
	@DisplayName("쿠폰이 존재하지 않으면 CouponNotFoundException을 던진다.")
	void getCoupon_whenCouponNotExist() {
		// given
		given(couponRepository.findById(anyLong())).willReturn(Mono.empty());

		// when
		// then
		StepVerifier.create(couponQueryService.getCoupon(1L))
			.expectErrorMatches(throwable -> throwable instanceof BusinessException
				&& ((BusinessException)throwable).getErrorCode() == ErrorCode.COUPON_NOT_FOUND)
			.verify();
	}
}
