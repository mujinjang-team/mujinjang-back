package mujinjang.couponsystem.domain.user.service.mock;

import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import mujinjang.couponsystem.domain.coupon.domain.Coupon;
import mujinjang.couponsystem.domain.coupon.domain.CouponWallet;
import mujinjang.couponsystem.domain.coupon.exception.CouponNotFoundException;
import mujinjang.couponsystem.domain.coupon.repository.CouponRepository;
import mujinjang.couponsystem.domain.coupon.repository.CouponWalletRepository;
import mujinjang.couponsystem.domain.user.domain.User;
import mujinjang.couponsystem.domain.user.exception.UserNotFoundException;
import mujinjang.couponsystem.domain.user.repository.UserRepository;
import mujinjang.couponsystem.domain.user.service.impl.UserServiceImpl;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class UserCouponUseServiceMockTest {

	@InjectMocks
	private UserServiceImpl userService;

	@Mock
	private CouponWalletRepository couponWalletRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private CouponRepository couponRepository;

	@Test
	void isCouponUsed_whenCouponUsed() {
		Long userId = 1L;
		Long couponId = 1L;
		CouponWallet couponWallet = mock(CouponWallet.class);

		when(couponWallet.getUsedAt()).thenReturn(LocalDateTime.now());

		when(userRepository.findById(userId)).thenReturn(Mono.just(mock(User.class)));
		when(couponRepository.findById(couponId)).thenReturn(Mono.just(mock(Coupon.class)));
		when(couponWalletRepository.findByUserIdAndCouponId(userId, couponId))
			.thenReturn(Mono.just(couponWallet));

		StepVerifier.create(userService.isCouponUsed(userId, couponId))
			.expectNext(true)
			.verifyComplete();
	}

	@Test
	void isCouponUsed_whenCouponIsNotUsed() {
		Long userId = 1L;
		Long couponId = 1L;
		CouponWallet couponWallet = mock(CouponWallet.class);

		when(userRepository.findById(userId)).thenReturn(Mono.just(mock(User.class)));
		when(couponRepository.findById(couponId)).thenReturn(Mono.just(mock(Coupon.class)));
		when(couponWalletRepository.findByUserIdAndCouponId(userId, couponId))
			.thenReturn(Mono.just(couponWallet));

		StepVerifier.create(userService.isCouponUsed(userId, couponId))
			.expectNext(false)
			.verifyComplete();
	}

	@Test
	void isCouponUsed_UserNotFound() {
		Long userId = 1L;
		Long couponId = 1L;

		when(userRepository.findById(userId)).thenReturn(Mono.empty());
		when(couponRepository.findById(couponId)).thenReturn(Mono.just(mock(Coupon.class)));
		when(couponWalletRepository.findByUserIdAndCouponId(userId, couponId)).thenReturn(
			Mono.just(mock(CouponWallet.class)));

		StepVerifier.create(userService.isCouponUsed(userId, couponId))
			.expectError(UserNotFoundException.class)
			.verify();
	}

	@Test
	void isCouponUsed_CouponNotFound() {
		Long userId = 1L;
		Long couponId = 1L;

		when(userRepository.findById(userId)).thenReturn(Mono.just(mock(User.class)));
		when(couponRepository.findById(couponId)).thenReturn(Mono.empty());
		when(couponWalletRepository.findByUserIdAndCouponId(userId, couponId)).thenReturn(
			Mono.just(mock(CouponWallet.class)));

		StepVerifier.create(userService.isCouponUsed(userId, couponId))
			.expectError(CouponNotFoundException.class)
			.verify();
	}
}
