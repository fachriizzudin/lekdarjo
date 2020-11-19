package com.lazuardifachri.bps.lekdarjo.repository;

import com.lazuardifachri.bps.lekdarjo.security.model.Role;
import com.lazuardifachri.bps.lekdarjo.security.model.ERole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
