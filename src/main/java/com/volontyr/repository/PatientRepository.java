package com.volontyr.repository;

import com.volontyr.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by volontyr on 22.03.17.
 */
public interface PatientRepository extends JpaRepository<Patient, Long> {

    Patient findByEmail(String email);
}
