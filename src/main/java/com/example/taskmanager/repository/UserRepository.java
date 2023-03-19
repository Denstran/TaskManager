package com.example.taskmanager.repository;

import com.example.taskmanager.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Override
    Page<User> findAll(Pageable pageable);

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByMail(String mail);
}
