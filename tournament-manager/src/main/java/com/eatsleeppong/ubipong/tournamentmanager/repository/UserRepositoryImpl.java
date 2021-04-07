package com.eatsleeppong.ubipong.tournamentmanager.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.eatsleeppong.ubipong.tournamentmanager.domain.UserRepository;
import com.eatsleeppong.ubipong.tournamentmanager.entity.SpringJpaUser;
import com.eatsleeppong.ubipong.tournamentmanager.mapper.UserMapper;
import com.eatsleeppong.ubipong.tournamentmanager.domain.User;
import com.eatsleeppong.ubipong.tournamentmanager.domain.UserExternalReference;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Value;

@Component
@Value
@AllArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final SpringJpaUserRepository springJpaUserRepository;
    private final UserMapper userMapper;

    public User save(final User user) {
        return userMapper.mapSpringJpaUserToUser(
            springJpaUserRepository.save(userMapper.mapUserToSpringJpaUser(user)));
    }

    public User getOne(String userId) {
        return userMapper.mapSpringJpaUserToUser(springJpaUserRepository.getOne(userId));
    }

    public Optional<User> findByExternalReference(UserExternalReference externalReference) {
        return springJpaUserRepository.findByExternalReference(externalReference.getUserReference())
            .map(userMapper::mapSpringJpaUserToUser);
    }
}
