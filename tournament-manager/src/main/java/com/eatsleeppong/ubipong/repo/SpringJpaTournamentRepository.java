package com.eatsleeppong.ubipong.repo;

import com.eatsleeppong.ubipong.entity.SpringJpaTournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface SpringJpaTournamentRepository extends JpaRepository<SpringJpaTournament, Integer> {
    Optional<SpringJpaTournament> findByName(@Param("name") String name);
}
