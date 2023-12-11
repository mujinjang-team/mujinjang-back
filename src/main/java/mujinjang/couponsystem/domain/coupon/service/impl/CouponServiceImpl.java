package mujinjang.couponsystem.domain.coupon.service.impl;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import mujinjang.couponsystem.common.exception.BusinessException;
import mujinjang.couponsystem.common.exception.ErrorCode;
import mujinjang.couponsystem.domain.coupon.domain.Coupon;
import mujinjang.couponsystem.domain.coupon.dto.request.IssueCouponRequest;
import mujinjang.couponsystem.domain.coupon.dto.response.CouponInfoResponse;
import mujinjang.couponsystem.domain.coupon.dto.response.CreateCouponResponse;
import mujinjang.couponsystem.domain.coupon.repository.CouponRepository;
import mujinjang.couponsystem.domain.coupon.service.CouponQueryService;
import mujinjang.couponsystem.domain.coupon.service.CouponService;
import reactor.core.publisher.Mono;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final CouponQueryService couponQueryService;

    @Override
    @Transactional
    public Mono<CreateCouponResponse> createCoupon(final IssueCouponRequest dto) {
        return couponRepository.findByCode(dto.code())
                   .flatMap(coupon -> Optional.ofNullable(coupon)
                                              .map(c -> Mono.<CreateCouponResponse>error(
                                                      new BusinessException(ErrorCode.COUPON_CODE_DUPLICATED)))
                                              .orElseGet(() -> createCouponEntity(dto)
                                                      .map(crateCoupon -> new CreateCouponResponse(
                                                              crateCoupon.getId()))));
    }

    @Override
    public Mono<Page<CouponInfoResponse>> getCouponsInfo(final Pageable pageable) {
        return couponRepository.findAllBy(pageable)
                .map(CouponInfoResponse::of)
                .collectList()
                .zipWith(couponRepository.count())
                .map(p -> new PageImpl<>(p.getT1(), pageable, p.getT2()));
    }

    @Override
    public Mono<CouponInfoResponse> getCouponInfo(Long couponId) {
        return couponQueryService.getCoupon(couponId)
                                 .map(CouponInfoResponse::of);
    }

    private Mono<Coupon> createCouponEntity(IssueCouponRequest dto) {
        return couponRepository.save(Coupon.builder()
                                           .name(dto.name())
                                           .code(dto.code())
                                           .type(dto.type())
                                           .discount(dto.discount())
                                           .amount(dto.amount())
                                           .build());
    }
}
