package com.eatsleeppong.ubipong.tournamentmanager.repository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.eatsleeppong.ubipong.tournamentmanager.domain.UserRepository;
import com.eatsleeppong.ubipong.tournamentmanager.mapper.UserMapper;
import com.eatsleeppong.ubipong.tournamentmanager.domain.User;
import com.eatsleeppong.ubipong.tournamentmanager.domain.UserExternalReference;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@Component
@Value
@AllArgsConstructor
@Slf4j
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

    public User getOneByExternalReference(UserExternalReference externalReference) {
        return userMapper.mapSpringJpaUserToUser(springJpaUserRepository.findByExternalReference(
            externalReference.getUserReference()));
    }
}
