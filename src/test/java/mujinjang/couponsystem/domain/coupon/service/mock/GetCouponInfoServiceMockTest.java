package mujinjang.couponsystem.domain.coupon.service.mock;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.Objects;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.ReactiveSetOperations;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import mujinjang.couponsystem.domain.coupon.domain.Coupon;
import mujinjang.couponsystem.domain.coupon.domain.CouponType;
import mujinjang.couponsystem.domain.coupon.dto.response.CouponInfoResponse;
import mujinjang.couponsystem.domain.coupon.service.CouponQueryService;
import mujinjang.couponsystem.domain.coupon.service.impl.CouponServiceImpl;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
@DisplayName("getCouponInfo 메서드는")
public class GetCouponInfoServiceMockTest {

	@InjectMocks
	private CouponServiceImpl couponService;
	@Mock
	private CouponQueryService couponQueryService;
	@Mock
	private ReactiveStringRedisTemplate redisTemplate;

	@Test
	@DisplayName("쿠폰 정보를 반환한다.")
	void getCouponsInfo() {
		// given
		Coupon coupon = new Coupon("name", "code", CouponType.FIXED, 1D, 1L);
		ReflectionTestUtils.setField(coupon, "id", 1L);
		ReflectionTestUtils.setField(coupon, "createdAt", LocalDateTime.now());
		Long issuedCouponNum = 1L;
		CouponInfoResponse couponInfoResponse = CouponInfoResponse.of(coupon, coupon.getAmount() - issuedCouponNum);
		given(couponQueryService.getCoupon(any(Long.class))).willReturn(Mono.just(coupon));
		given(redisTemplate.opsForSet()).willReturn(mock(ReactiveSetOperations.class));
		given(redisTemplate.opsForSet().size(any())).willReturn(Mono.just(issuedCouponNum));

		// when
		// then
		StepVerifier.create(couponService.getCouponInfo(1L))
			.expectNextMatches(response -> Objects.equals(response, couponInfoResponse))
			.verifyComplete();
	}
}
