package com.volontyr.model;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by volontyr on 18.03.17.
 */
@Entity
@Table(name = "permissions")
public class Permission {

    private Long id;
    private String target;
    private String permission;
    private Set<Doctor> doctors;

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    @ManyToMany(mappedBy = "permissions")
    public Set<Doctor> getDoctors() {
        return doctors;
    }

    public void setDoctors(Set<Doctor> doctors) {
        this.doctors = doctors;
    }
}
