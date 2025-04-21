package com.softeng.backend.repository;

import com.softeng.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByPersonalNo(String personalNo);
    Optional<User> findByEmail(String email);
    boolean existsByPersonalNo(String personalNo);
    boolean existsByEmail(String email);
}