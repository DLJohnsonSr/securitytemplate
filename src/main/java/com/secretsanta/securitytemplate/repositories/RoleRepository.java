package com.secretsanta.securitytemplate.repositories;

import com.secretsanta.securitytemplate.models.AppRole;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<AppRole, Long> {

    AppRole findByRoleName(String role);
}
