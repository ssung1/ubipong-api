package com.eatsleeppong.ubipong.tournamentmanager.domain;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TournamentRepository {
    Tournament save(@Valid Tournament tournament);
    Tournament getOne(Integer id);
    Page<Tournament> findAll(Pageable pageable);
}
