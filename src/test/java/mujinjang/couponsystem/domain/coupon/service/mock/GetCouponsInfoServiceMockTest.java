package mujinjang.couponsystem.domain.coupon.service.mock;

import static org.mockito.BDDMockito.*;

import java.util.Objects;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.ReactiveSetOperations;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;

import mujinjang.couponsystem.domain.coupon.domain.Coupon;
import mujinjang.couponsystem.domain.coupon.domain.CouponType;
import mujinjang.couponsystem.domain.coupon.repository.CouponRepository;
import mujinjang.couponsystem.domain.coupon.service.impl.CouponServiceImpl;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
@DisplayName("getCouponsInfo 메서드는")
public class GetCouponsInfoServiceMockTest {

	@InjectMocks
	private CouponServiceImpl couponService;
	@Mock
	private CouponRepository couponRepository;
	@Mock
	private ReactiveStringRedisTemplate redisTemplate;

	@Test
	@DisplayName("쿠폰 정보를 반환한다.")
	void getCouponsInfo() {
		// given
		Coupon coupon = new Coupon("name", "code", CouponType.FIXED, 1D, 1L);
		given(couponRepository.findAllBy(any(Pageable.class))).willReturn(Flux.just(coupon));
		given(redisTemplate.opsForSet()).willReturn(mock(ReactiveSetOperations.class));
		given(redisTemplate.opsForSet().size(any())).willReturn(Mono.just(1L));
		given(couponRepository.count()).willReturn(Mono.just(1L));

		// when
		// then
		StepVerifier.create(couponService.getCouponsInfo(Pageable.unpaged()))
			.expectNextMatches(couponInfoResponsePage -> Objects.equals(couponInfoResponsePage.getTotalElements(), 1L))
			.verifyComplete();
	}
}
