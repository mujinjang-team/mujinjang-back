package mujinjang.couponsystem.domain.coupon.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "coupon_wallet")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class CouponWallet {

    @Id
    private Long id;

    private Long userId;

    private Long couponId;

    @CreatedDate
    private LocalDateTime createdAt;

    private LocalDateTime usedAt;

    public CouponWallet(Long userId, Long couponId) {
        this.userId = userId;
        this.couponId = couponId;
    }

    public void useCoupon() {
        this.usedAt = LocalDateTime.now();
    }
}
