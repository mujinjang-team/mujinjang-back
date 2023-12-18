package mujinjang.couponsystem.domain.coupon.service.mock;

import static org.mockito.BDDMockito.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import mujinjang.couponsystem.domain.coupon.domain.CouponWallet;
import mujinjang.couponsystem.domain.coupon.repository.CouponWalletRepository;
import mujinjang.couponsystem.domain.coupon.service.CouponWalletQueryService;
import mujinjang.couponsystem.domain.coupon.service.impl.CouponWalletServiceImpl;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
@DisplayName("useCoupon 메서드는")
public class UseCouponServiceMockTest {

	@InjectMocks
	private CouponWalletServiceImpl couponWalletService;

	@Mock
	private CouponWalletRepository couponWalletRepository;
	@Mock
	private CouponWalletQueryService couponWalletQueryService;

	@Test
	@DisplayName("쿠폰 지갑이 존재하면 쿠폰을 사용한다.")
	void useCoupon_whenCouponWalletExist() {
		// given
		CouponWallet couponWallet = new CouponWallet(1L, 1L);
		given(couponWalletQueryService.getCouponWallet(anyLong())).willReturn(Mono.just(couponWallet));
		given(couponWalletRepository.save(any(CouponWallet.class))).willReturn(Mono.just(couponWallet));

		// when
		// then
		StepVerifier.create(couponWalletService.useCoupon(1L))
			.verifyComplete();
		Assertions.assertThat(couponWallet.getUsedAt()).isNotNull();
	}
}
