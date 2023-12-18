package mujinjang.couponsystem.domain.coupon.service.mock;

import static org.mockito.BDDMockito.*;

import java.util.Objects;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import mujinjang.couponsystem.common.exception.BusinessException;
import mujinjang.couponsystem.common.exception.ErrorCode;
import mujinjang.couponsystem.domain.coupon.domain.Coupon;
import mujinjang.couponsystem.domain.coupon.domain.CouponType;
import mujinjang.couponsystem.domain.coupon.dto.request.CreateCouponRequest;
import mujinjang.couponsystem.domain.coupon.repository.CouponRepository;
import mujinjang.couponsystem.domain.coupon.service.impl.CouponServiceImpl;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
@DisplayName("createCoupon 메서드는")
public class CreateCouponServiceMockTest {

	@InjectMocks
	private CouponServiceImpl couponService;
	@Mock
	private CouponRepository couponRepository;

	@Test
	@DisplayName("생성하려는 쿠폰 코드가 이미 존재하면 쿠폰 중복 에러를 던진다.")
	void createCoupon_whenCouponCodeAlreadyExist() {
		// given
		CreateCouponRequest dto = new CreateCouponRequest("duplicateCode", "name", CouponType.FIXED, 1D, 1L);
		given(couponRepository.findByCode(any())).willReturn(Mono.just(mock(Coupon.class)));

		// when
		// then
		StepVerifier.create(couponService.createCoupon(dto))
			.expectErrorMatches(throwable -> throwable instanceof BusinessException
				&& ((BusinessException)throwable).getErrorCode() == ErrorCode.COUPON_CODE_DUPLICATED)
			.verify();
	}

	@Test
	@DisplayName("생성하려는 쿠폰 코드가 존재하지 않으면 쿠폰을 생성한다.")
	void createCoupon_whenCouponCodeNotExist() {
		// given
		String code = "notExistCode";
		CreateCouponRequest dto = new CreateCouponRequest(code, "name", CouponType.FIXED, 1D, 1L);
		Coupon coupon = new Coupon("name", code, CouponType.FIXED, 1D, 1L);
		ReflectionTestUtils.setField(coupon, "id", 1L);
		given(couponRepository.findByCode(any())).willReturn(Mono.empty());
		given(couponRepository.save(any())).willReturn(Mono.just(coupon));

		// when
		// then
		StepVerifier.create(couponService.createCoupon(dto))
			.expectNextMatches(response -> Objects.equals(response.couponId(), coupon.getId()))
			.verifyComplete();
	}
}
