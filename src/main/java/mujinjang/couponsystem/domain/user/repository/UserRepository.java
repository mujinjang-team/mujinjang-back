package mujinjang.couponsystem.domain.user.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import mujinjang.couponsystem.domain.user.domain.User;

public interface UserRepository extends R2dbcRepository<User, Long> {
}
