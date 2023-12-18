package mujinjang.couponsystem.domain.coupon.service.mock;

import static org.mockito.BDDMockito.*;

import java.util.Objects;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.ReactiveRedisCallback;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import mujinjang.couponsystem.common.exception.BusinessException;
import mujinjang.couponsystem.common.exception.ErrorCode;
import mujinjang.couponsystem.domain.coupon.domain.Coupon;
import mujinjang.couponsystem.domain.coupon.domain.CouponType;
import mujinjang.couponsystem.domain.coupon.domain.CouponWallet;
import mujinjang.couponsystem.domain.coupon.repository.CouponWalletRepository;
import mujinjang.couponsystem.domain.coupon.service.CouponQueryService;
import mujinjang.couponsystem.domain.coupon.service.impl.CouponWalletServiceImpl;
import mujinjang.couponsystem.domain.user.domain.User;
import mujinjang.couponsystem.domain.user.service.UserQueryService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
@DisplayName("issueCoupon 메서드는")
public class IssueCouponServiceMockTest {

	@InjectMocks
	private CouponWalletServiceImpl couponWalletService;

	@Mock
	private UserQueryService userQueryService;
	@Mock
	private CouponQueryService couponQueryService;
	@Mock
	private CouponWalletRepository couponWalletRepository;
	@Mock
	private ReactiveStringRedisTemplate redisTemplate;

	@Test
	@DisplayName("쿠폰 잔여수량이 없으면 쿠폰 품절 에러를 던진다.")
	void issueCoupon_whenCouponSoldOut() {
		// given
		User user = new User("username");
		ReflectionTestUtils.setField(user, "id", 1L);
		Coupon coupon = new Coupon("name", "code", CouponType.FIXED, 1D, 1L);
		ReflectionTestUtils.setField(coupon, "id", 1L);

		given(userQueryService.getUser(any(Long.class))).willReturn(Mono.just(user));
		given(couponQueryService.getCoupon(any(Long.class))).willReturn(Mono.just(coupon));
		given(redisTemplate.execute(any(ReactiveRedisCallback.class))).willReturn(Flux.just(false));

		// when
		// then
		StepVerifier.create(couponWalletService.issueCoupon(1L, 1L))
			.expectErrorMatches(throwable -> throwable instanceof BusinessException
				&& ((BusinessException)throwable).getErrorCode() == ErrorCode.COUPON_SOLD_OUT)
			.verify();
	}

	@Test
	@DisplayName("쿠폰 잔여수량이 있으면 쿠폰을 발급한다.")
	void issueCoupon_whenCouponNotSoldOut() {
		// given
		User user = new User("username");
		ReflectionTestUtils.setField(user, "id", 1L);
		Coupon coupon = new Coupon("name", "code", CouponType.FIXED, 1D, 1L);
		ReflectionTestUtils.setField(coupon, "id", 1L);
		CouponWallet couponWallet = new CouponWallet(user.getId(), coupon.getId());

		given(userQueryService.getUser(any(Long.class))).willReturn(Mono.just(user));
		given(couponQueryService.getCoupon(any(Long.class))).willReturn(Mono.just(coupon));
		given(redisTemplate.execute(any(ReactiveRedisCallback.class))).willReturn(Flux.just(true));
		given(couponWalletRepository.save(any())).willReturn(Mono.just(couponWallet));

		// when
		// then
		StepVerifier.create(couponWalletService.issueCoupon(1L, 1L))
			.expectNextMatches(response -> Objects.equals(response.couponWalletId(), couponWallet.getId()))
			.verifyComplete();
	}
}
