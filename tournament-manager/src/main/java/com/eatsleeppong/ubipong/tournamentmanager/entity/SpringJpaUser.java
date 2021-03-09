package com.eatsleeppong.ubipong.tournamentmanager.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity(name = "user")
@Table(name = "user", indexes = {
    @Index(name = "email_idx", columnList = "email", unique = true)
})
public class SpringJpaUser {
    @Id
    private String id;
    private String email;
}
