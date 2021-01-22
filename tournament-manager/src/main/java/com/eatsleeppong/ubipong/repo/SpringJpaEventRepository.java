package com.eatsleeppong.ubipong.repo;

import com.eatsleeppong.ubipong.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpringJpaEventRepository extends JpaRepository<Event, Integer> {
    List<Event> findByTournamentId(@Param("tournamentId") Integer tournamentId);
}
