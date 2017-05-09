package com.volontyr.converter;

import com.volontyr.model.Role;
import com.volontyr.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

/**
 * Created by volontyr on 04.05.17.
 */
public final class StringToRole implements Converter<String, Role> {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role convert(String roleId) {
        return roleRepository.findRolesById(Long.valueOf(roleId));
    }
}
