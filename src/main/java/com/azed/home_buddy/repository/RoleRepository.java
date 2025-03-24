package com.azed.home_buddy.repository;

import com.azed.home_buddy.model.AppRole;
import com.azed.home_buddy.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(AppRole appRole);
}
