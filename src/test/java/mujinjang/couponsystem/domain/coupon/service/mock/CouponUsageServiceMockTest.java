package mujinjang.couponsystem.domain.coupon.service.mock;

import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import mujinjang.couponsystem.domain.coupon.dto.response.CouponUsageStatusResponse;
import mujinjang.couponsystem.domain.coupon.repository.CouponWalletRepository;
import mujinjang.couponsystem.domain.coupon.service.impl.CouponWalletServiceImpl;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
@DisplayName("getCouponUsageStatus 메서드는")
public class CouponUsageServiceMockTest {

	@InjectMocks
	private CouponWalletServiceImpl couponWalletService;
	@Mock
	private CouponWalletRepository couponWalletRepository;

	@Test
	@DisplayName("쿠폰 지갑이 존재하지 않으면 빈 content를 반환한다.")
	void getCouponUsage_whenCouponWalletNotExist() {
		// given
		given(couponWalletRepository.findAllCouponUsageStatusByCouponId(any(Pageable.class), anyLong()))
			.willReturn(Flux.empty());
		given(couponWalletRepository.count()).willReturn(Mono.just(0L));

		// when
		Mono<Page<CouponUsageStatusResponse>> result = couponWalletService.getCouponUsageStatus(1L,
			Pageable.unpaged());

		// then
		StepVerifier.create(result)
			.expectNextMatches(page -> page.getContent().isEmpty())
			.verifyComplete();
	}

	@Test
	@DisplayName("쿠폰 지갑이 존재하면 쿠폰 사용 내역을 반환한다.")
	void getCouponUsage_whenCouponWalletExist() {
		// given
		CouponUsageStatusResponse couponUsageStatusResponse = new CouponUsageStatusResponse(
			1L, "username", LocalDateTime.now(), LocalDateTime.now());
		given(couponWalletRepository.findAllCouponUsageStatusByCouponId(any(Pageable.class), anyLong()))
			.willReturn(Flux.just(couponUsageStatusResponse));
		given(couponWalletRepository.count()).willReturn(Mono.just(1L));

		// when
		Mono<Page<CouponUsageStatusResponse>> result = couponWalletService.getCouponUsageStatus(1L,
			Pageable.unpaged());

		// then
		StepVerifier.create(result)
			.expectNextMatches(page -> page.getContent().get(0).equals(couponUsageStatusResponse))
			.verifyComplete();
	}

}
