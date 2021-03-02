package com.eatsleeppong.ubipong.tournamentmanager.entity;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity(name = "userOrganizationRole")
@Table(name = "user_organization_role", indexes = {
    @Index(name = "user_id_organization_id_role_idx", columnList = "userId, organizationId, role", unique = true)
})
public class SpringJpaUserOrganizationRole {
    private String userId;
    private String organizationId;
    private String role;
}
