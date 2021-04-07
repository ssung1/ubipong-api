package com.eatsleeppong.ubipong.tournamentmanager.domain;

import java.util.Optional;

public interface UserRepository {
    User save(User user);
    User getOne(String userId);
    Optional<User> findByExternalReference(UserExternalReference externalReference);
}
