package com.volontyr.service;

import com.volontyr.model.Doctor;
import com.volontyr.model.VerificationToken;

/**
 * Created by volontyr on 15.03.17.
 */
public interface DoctorService {

    Doctor save(Doctor doctor);

    void remove(Doctor doctor);

    Doctor findByEmail(String email);

    Doctor getDoctor(String verificationToken);

    void createVerificationToken(Doctor doctor, String token);

    VerificationToken getVerificationToken(String token);

    void saveRegisteredDoctor(Doctor doctor);

    VerificationToken generateNewVerificationToken(String existingToken);

    void createPasswordResetTokenForDoctor(Doctor doctor, String token);

    void changePassword(Doctor doctor, String password);
}
