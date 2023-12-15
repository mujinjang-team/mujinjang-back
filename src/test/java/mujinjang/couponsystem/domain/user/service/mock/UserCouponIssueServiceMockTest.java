package mujinjang.couponsystem.domain.user.service.mock;

import static org.mockito.Mockito.*;

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
class UserServiceImplMockTest {

	@InjectMocks
	private UserServiceImpl userService;

	@Mock
	private CouponWalletRepository couponWalletRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private CouponRepository couponRepository;

	@Test
	void isCouponIssued_CouponNotIssued() {
		Long userId = 1L;
		Long couponId = 1L;

		when(userRepository.findById(userId)).thenReturn(Mono.just(mock(User.class)));
		when(couponRepository.findById(couponId)).thenReturn(Mono.just(mock(Coupon.class)));
		when(couponWalletRepository.findByUserIdAndCouponId(userId, couponId)).thenReturn(Mono.empty());

		StepVerifier.create(userService.isCouponIssued(userId, couponId))
			.expectNext(false)
			.verifyComplete();
	}

	@Test
	void isCouponIssued_CouponIssued() {
		Long userId = 1L;
		Long couponId = 1L;

		when(userRepository.findById(userId)).thenReturn(Mono.just(mock(User.class)));
		when(couponRepository.findById(couponId)).thenReturn(Mono.just(mock(Coupon.class)));
		when(couponWalletRepository.findByUserIdAndCouponId(userId, couponId)).thenReturn(
			Mono.just(mock(CouponWallet.class)));

		StepVerifier.create(userService.isCouponIssued(userId, couponId))
			.expectNext(true)
			.verifyComplete();
	}

	@Test
	void isCouponIssued_UserNotFound() {
		Long userId = 1L;
		Long couponId = 1L;

		when(userRepository.findById(userId)).thenReturn(Mono.empty());
		when(couponRepository.findById(couponId)).thenReturn(Mono.just(mock(Coupon.class)));
		when(couponWalletRepository.findByUserIdAndCouponId(userId, couponId)).thenReturn(
			Mono.just(mock(CouponWallet.class)));

		StepVerifier.create(userService.isCouponIssued(userId, couponId))
			.expectError(UserNotFoundException.class)
			.verify();
	}

	@Test
	void isCouponIssued_CouponNotFound() {
		Long userId = 1L;
		Long couponId = 1L;

		when(userRepository.findById(userId)).thenReturn(Mono.just(mock(User.class)));
		when(couponRepository.findById(couponId)).thenReturn(Mono.empty());
		when(couponWalletRepository.findByUserIdAndCouponId(userId, couponId)).thenReturn(
			Mono.just(mock(CouponWallet.class)));

		StepVerifier.create(userService.isCouponIssued(userId, couponId))
			.expectError(CouponNotFoundException.class)
			.verify();
	}

}
