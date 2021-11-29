package com.kursovaya.institut.repo;

import com.kursovaya.institut.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findAllByUsername(String username);

}
