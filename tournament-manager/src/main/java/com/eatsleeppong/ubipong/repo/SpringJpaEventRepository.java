package com.eatsleeppong.ubipong.repo;

import com.eatsleeppong.ubipong.entity.SpringJpaEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpringJpaEventRepository extends JpaRepository<SpringJpaEvent, Integer> {
    List<SpringJpaEvent> findByTournamentId(@Param("tournamentId") Integer tournamentId);
}
