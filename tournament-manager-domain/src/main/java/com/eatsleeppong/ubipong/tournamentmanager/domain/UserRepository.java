package com.eatsleeppong.ubipong.tournamentmanager.domain;

public interface UserRepository {
    User save(User user);
    User getOne(String userId);
    User getOneByExternalReference(UserExternalReference externalReference);
}
