package mujinjang.couponsystem.domain.user.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import mujinjang.couponsystem.domain.user.domain.User;
import mujinjang.couponsystem.domain.user.exception.UserNotFoundException;
import mujinjang.couponsystem.domain.user.repository.UserRepository;
import mujinjang.couponsystem.domain.user.service.UserQueryService;
import reactor.core.publisher.Mono;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserQueryServiceImpl implements UserQueryService {

    private final UserRepository userRepository;

    @Override
    public Mono<User> getUser(final Long userId) {
        return userRepository.findById(userId)
                .switchIfEmpty(Mono.error(new UserNotFoundException()));
    }
}
