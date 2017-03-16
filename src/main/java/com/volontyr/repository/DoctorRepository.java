package com.volontyr.repository;

import com.volontyr.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by volontyr on 15.03.17.
 */
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Doctor findByUsername(String username);
}
