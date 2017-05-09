package com.volontyr.service;

import com.volontyr.model.Role;
import com.volontyr.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by volontyr on 03.05.17.
 */
@Component
public class RoleService {

    private static RoleRepository roleRepository;

    @Autowired
    private RoleRepository _roleRepository;

    @PostConstruct
    public void init() {
        RoleService.roleRepository = _roleRepository;
    }

    public static Map<Long, String> getRoles() {
        Map<Long, String> roles = new HashMap<>();

        for (Role role : roleRepository.findAll()) {
            roles.put(role.getId(), role.getName());
        }

        return roles;
    }
}
