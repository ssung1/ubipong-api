package com.eatsleeppong.ubipong.tournamentmanager.repository;

import com.eatsleeppong.ubipong.tournamentmanager.entity.SpringJpaUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "users", path = "users")
public interface SpringJpaUserRepository extends JpaRepository<SpringJpaUser, String> {
    SpringJpaUser findByExternalReference(@Param("externalReference") String externalReference);
}

