package com.volontyr.repository;

import com.volontyr.model.Doctor;
import com.volontyr.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by volontyr on 04.05.17.
 */
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    VerificationToken findByToken(String token);

    VerificationToken findByDoctor(Doctor doctor);
}
