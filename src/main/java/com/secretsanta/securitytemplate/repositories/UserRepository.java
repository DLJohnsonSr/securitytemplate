package com.secretsanta.securitytemplate.repositories;

import com.secretsanta.securitytemplate.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);
}
