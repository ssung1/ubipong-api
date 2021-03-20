package com.eatsleeppong.ubipong.tournamentmanager.domain;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TournamentRepository {
    Tournament save(@Valid Tournament tournament);
    Tournament getOne(Integer id);
    List<Tournament> findAll();
    Page<Tournament> findAll(Pageable pageable);
}
