package com.enigma.enigma_shop.repository;

import com.enigma.enigma_shop.constant.UserRole;
import com.enigma.enigma_shop.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByRole(UserRole role);
}
