package com.volontyr.repository;

import com.volontyr.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by volontyr on 18.03.17.
 */
public interface PermissionRepository extends JpaRepository<Permission, Long> {
}
