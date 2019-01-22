package com.secretsanta.securitytemplate.repositories;

import com.secretsanta.securitytemplate.models.AppUser;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<AppUser, Long> {

    AppUser findByUsername(String username);
}
