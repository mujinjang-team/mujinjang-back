package mujinjang.couponsystem.domain.coupon.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mujinjang.couponsystem.domain.coupon.dto.request.IssueCouponWalletRequest;
import mujinjang.couponsystem.domain.coupon.dto.response.CouponUsageStatusResponse;
import mujinjang.couponsystem.domain.coupon.dto.response.IssueCouponWalletResponse;
import mujinjang.couponsystem.domain.coupon.service.CouponWalletService;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/coupon-wallets")
@RequiredArgsConstructor
@Tag(name = "쿠폰 지갑 API", description = "쿠폰이 유저에게 발급되면 쿠폰 지갑이 됩니다.")
public class CouponWalletController {

	private final CouponWalletService couponWalletService;

	@Operation(summary = "쿠폰 발급 API")
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "쿠폰 발급 성공")
	})
	@PostMapping("/issue")
	public Mono<ResponseEntity<IssueCouponWalletResponse>> issueCoupon
		(@Valid @RequestBody Mono<IssueCouponWalletRequest> request) {
		return request.flatMap(requestDto -> couponWalletService.issueCoupon(requestDto.userId(),
				requestDto.couponId()))
			.map(responseDto -> ResponseEntity.status(HttpStatus.CREATED)
				.body(responseDto));
	}

	@Operation(summary = "쿠폰 사용 현황 API", description = "쿠폰 사용 현황")
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "쿠폰 사용 현황 조회 성공(쿠폰을 최근에 발급한 사용자를 먼저 보여줍니다.)")
	})
	@GetMapping
	public Mono<ResponseEntity<Page<CouponUsageStatusResponse>>> getCouponUsageStatus(
		@Parameter(name = "couponId", description = "쿠폰 ID", in = ParameterIn.QUERY)
		@RequestParam(value = "couponId", required = true) Long couponId,
		@Parameter(name = "page", description = "페이지네이션의 페이지 넘버. 0부터 시작함", in = ParameterIn.QUERY)
		@RequestParam(value = "page", required = false, defaultValue = "0") int page,
		@Parameter(name = "size", description = "페이지네이션의 페이지당 데이터 수", in = ParameterIn.QUERY)
		@RequestParam(value = "size", required = false, defaultValue = "20") int size
	) {
		return Mono.just(PageRequest.of(page, size))
			.flatMap(pageable -> couponWalletService.getCouponUsageStatus(couponId, pageable))
			.map(ResponseEntity::ok);
	}

	@Operation(summary = "쿠폰 사용 API")
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "쿠폰 사용 성공"),
		@ApiResponse(
			responseCode = "404",
			description = "CW001: 쿠폰 지갑을 찾을 수 없습니다.",
			content = @Content(schema = @Schema(hidden = true)))
	})
	@PatchMapping("{id}/use")
	public Mono<ResponseEntity<Void>> useCoupon(
		@Parameter(name = "id", description = "쿠폰 지갑 ID", in = ParameterIn.PATH)
		@PathVariable(name = "id") Long couponWalletId) {
		return couponWalletService.useCoupon(couponWalletId)
			.then(Mono.just(ResponseEntity.ok().build()));
	}
}
