package com.secretsanta.securitytemplate.repositories;

import com.secretsanta.securitytemplate.models.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long> {

    Role findByRoleName(String role);
}
