package mujinjang.couponsystem.domain.user.service;

import mujinjang.couponsystem.domain.user.domain.User;
import reactor.core.publisher.Mono;

public interface UserQueryService {
    Mono<User> getUser(Long userId);
}
