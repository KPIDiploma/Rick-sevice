package com.volontyr.model;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by volontyr on 15.03.17.
 */
@Entity
@Table(name = "role")
public class Role {

    private Long id;
    private String name;
    private Set<Doctor> doctors;

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToMany(mappedBy = "roles")
    public Set<Doctor> getDoctors() {
        return doctors;
    }

    public void setDoctors(Set<Doctor> doctors) {
        this.doctors = doctors;
    }
}
