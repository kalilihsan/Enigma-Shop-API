package com.enigma.enigma_shop.service.impl;

import com.enigma.enigma_shop.constant.UserRole;
import com.enigma.enigma_shop.entity.Role;
import com.enigma.enigma_shop.repository.RoleRepository;
import com.enigma.enigma_shop.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Role getOrSave(UserRole role) {
        return roleRepository.findByRole(role)
                .orElseGet(() -> roleRepository.saveAndFlush(Role.builder().role(role).build()));
    }
}
