package mujinjang.couponsystem.domain.user.controller;

import java.util.Collections;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mujinjang.couponsystem.domain.user.service.UserService;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "유저 API", description = "유저 관련 API 입니다.")
public class UserController {
	private final UserService userService;

	@Operation(summary = "쿠폰 발급 여부 조회 API", description = "유저가 쿠폰을 발급 받았는지 여부를 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "쿠폰 발급 여부 조회 성공"),
		@ApiResponse(
			responseCode = "404",
			description = "유저 또는 쿠폰을 찾을 수 없습니다.")
	})
	@GetMapping("/{userId}/coupons/{couponId}/issued")
	public Mono<ResponseEntity<Map<String, Boolean>>> isCouponIssued(@PathVariable Long userId,
		@PathVariable Long couponId) {
		return userService.isCouponIssued(userId, couponId)
			.map(issued -> ResponseEntity.ok(Collections.singletonMap("isIssued", issued)));
	}

	@Operation(summary = "쿠폰 사용 여부 조회 API", description = "유저가 쿠폰을 사용했는지 여부를 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "쿠폰 사용 여부 조회 성공"),
		@ApiResponse(
			responseCode = "404",
			description = "유저 또는 쿠폰을 찾을 수 없습니다.")
	})
	@GetMapping("/{userId}/coupons/{couponId}/used")
	public Mono<ResponseEntity<Map<String, Boolean>>> isCouponUsed(@PathVariable Long userId,
		@PathVariable Long couponId) {
		return userService.isCouponUsed(userId, couponId)
			.map(used -> ResponseEntity.ok(Collections.singletonMap("isUsed", used)));
	}

}
