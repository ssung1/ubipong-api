package com.eatsleeppong.ubipong.tournamentmanager.repository;

import com.eatsleeppong.ubipong.entity.SpringJpaEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "events", path = "events")
public interface SpringJpaEventRepository extends JpaRepository<SpringJpaEvent, Integer> {
    List<SpringJpaEvent> findByTournamentId(@Param("tournamentId") Integer tournamentId);
}
