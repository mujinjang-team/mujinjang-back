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
import mujinjang.couponsystem.domain.coupon.domain.CouponWallet;
import mujinjang.couponsystem.domain.coupon.repository.CouponWalletRepository;
import mujinjang.couponsystem.domain.coupon.service.impl.CouponWalletQueryServiceImpl;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
@DisplayName("getCoupon 메서드는")
public class CouponWalletQueryServiceMockTest {

	@InjectMocks
	private CouponWalletQueryServiceImpl couponWalletQueryService;

	@Mock
	private CouponWalletRepository couponWalletRepository;

	@Test
	@DisplayName("쿠폰 지갑이 존재하면 쿠폰 지갑을 반환한다.")
	void getCouponWallet_whenCouponWalletExist() {
		// given
		CouponWallet couponWallet = new CouponWallet(1L, 1L);
		given(couponWalletRepository.findById(anyLong())).willReturn(Mono.just(couponWallet));

		// when
		// then
		StepVerifier.create(couponWalletQueryService.getCouponWallet(1L))
			.expectNextMatches(c -> c.equals(couponWallet))
			.verifyComplete();
	}

	@Test
	@DisplayName("쿠폰 지갑이 존재하지 않으면 CouponWalletNotFoundException을 던진다.")
	void getCouponWallet_whenCouponWalletNotExist() {
		// given
		given(couponWalletRepository.findById(anyLong())).willReturn(Mono.empty());

		// when
		// then
		StepVerifier.create(couponWalletQueryService.getCouponWallet(1L))
			.expectErrorMatches(throwable -> throwable instanceof BusinessException
				&& ((BusinessException)throwable).getErrorCode() == ErrorCode.COUPON_WALLET_NOT_FOUND)
			.verify();
	}
}
