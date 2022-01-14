package com.kursovaya.institut.repo;

import com.kursovaya.institut.models.User;
import com.kursovaya.institut.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByUsername(String username);

    Collection<User> findAllByRoles(Role role);
}
