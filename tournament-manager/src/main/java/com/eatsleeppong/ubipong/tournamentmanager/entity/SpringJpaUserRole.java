package com.eatsleeppong.ubipong.tournamentmanager.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity(name = "userRole")
@Table(name = "user_role", indexes = {
    @Index(name = "user_id_role_idx", columnList = "userId, role", unique = true)
})
public class SpringJpaUserRole {
    @Id
    private String id;
    private String userId;
    private String role;    
}
