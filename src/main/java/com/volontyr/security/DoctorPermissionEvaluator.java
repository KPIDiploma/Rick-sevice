package com.volontyr.security;

import com.volontyr.model.Doctor;
import com.volontyr.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;

import javax.sql.DataSource;
import java.io.Serializable;

/**
 * Created by volontyr on 18.03.17.
 */
public class DoctorPermissionEvaluator implements PermissionEvaluator {

    private DataSource dataSource;

    @Autowired
    private DoctorRepository doctorRepository;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object targetObject, Object permission) {
//        JdbcTemplate template = new JdbcTemplate(dataSource);

        Doctor doctor = doctorRepository.findByEmail(((User) authentication.getPrincipal()).getUsername());

        return doctor.hasPermission(targetObject.getClass().getName(), permission.toString());
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable serializable, String s, Object o) {
        return false;
    }
}
