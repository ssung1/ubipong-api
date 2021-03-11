package com.eatsleeppong.ubipong.tournamentmanager.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity(name = "organization")
@Table(name = "organization", indexes = {
    @Index(name = "name_idx", columnList = "name", unique = true)
})
public class SpringJpaOrganization {
    @Id
    private String id;
    private String name;
    private String logo;
}
