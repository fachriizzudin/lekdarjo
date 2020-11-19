package com.lazuardifachri.bps.lekdarjo.repository;

import com.lazuardifachri.bps.lekdarjo.security.model.ERole;
import com.lazuardifachri.bps.lekdarjo.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByRoles(ERole role);
}
