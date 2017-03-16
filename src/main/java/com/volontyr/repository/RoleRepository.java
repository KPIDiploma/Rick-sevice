package com.volontyr.repository;

import com.volontyr.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by volontyr on 15.03.17.
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

}
