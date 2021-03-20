package com.eatsleeppong.ubipong.tournamentmanager.domain;

import java.util.List;

public interface UserRepository {
    User save(User user);
    User getOne(String userId);
    List<User> findByExternalReference(UserExternalReference externalReference);
}
