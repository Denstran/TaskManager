package com.example.taskmanager.repository;

import com.example.taskmanager.model.ERole;
import com.example.taskmanager.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
