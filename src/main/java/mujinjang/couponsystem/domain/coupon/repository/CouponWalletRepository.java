package mujinjang.couponsystem.domain.coupon.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import mujinjang.couponsystem.domain.coupon.domain.CouponWallet;
import mujinjang.couponsystem.domain.coupon.dto.response.CouponUsageStatusResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CouponWalletRepository extends R2dbcRepository<CouponWallet, Long> {

	@Query("SELECT cw.user_id, u.username, cw.created_at AS coupon_issued_at, cw.used_at AS coupon_used_at "
		+ "FROM coupon_wallet cw "
		+ "INNER JOIN users u ON cw.user_id = u.id "
		+ "WHERE cw.coupon_id = :couponId "
		+ "ORDER BY cw.created_at DESC "
		+ "LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}")
	Flux<CouponUsageStatusResponse> findAllCouponUsageStatusByCouponId(Pageable pageable, Long couponId);

	@Query("SELECT * FROM coupon_wallet cw WHERE cw.user_id = :userId AND cw.coupon_id = :couponId")
	Mono<CouponWallet> findByUserIdAndCouponId(Long userId, Long couponId);
}
