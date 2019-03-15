package com.eatsleeppong.ubipong.repo;

import com.eatsleeppong.ubipong.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Integer>{
}
