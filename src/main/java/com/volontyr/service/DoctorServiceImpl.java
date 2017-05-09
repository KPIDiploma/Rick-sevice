package com.volontyr.service;

import com.volontyr.model.Doctor;
import com.volontyr.model.PasswordResetToken;
import com.volontyr.model.VerificationToken;
import com.volontyr.repository.DoctorRepository;
import com.volontyr.repository.PasswordResetTokenRepository;
import com.volontyr.repository.RoleRepository;
import com.volontyr.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by volontyr on 15.03.17.
 */
@Service("doctorService")
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private VerificationTokenRepository tokenRepository;
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Doctor save(Doctor doctor) {
        doctor.setPassword(bCryptPasswordEncoder.encode(doctor.getPassword()));

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        Date birthDate = Calendar.getInstance().getTime();

        try {
            birthDate = dateFormat.parse(dateFormat.format(doctor.getBirthDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        doctor.setBirthDate(birthDate);

        return doctorRepository.save(doctor);
    }

    @Override
    public Doctor findByEmail(String email) {
        return doctorRepository.findByEmail(email);
    }

    @Override
    public void remove(Doctor doctor) {
        tokenRepository.delete(tokenRepository.findByDoctor(doctor));
        doctorRepository.delete(doctor);
    }

    @Override
    public Doctor getDoctor(String verificationToken) {
        return tokenRepository.findByToken(verificationToken).getDoctor();
    }

    @Override
    public void createVerificationToken(Doctor doctor, String token) {
        VerificationToken myToken = new VerificationToken(doctor, token);
        tokenRepository.save(myToken);
    }

    @Override
    public VerificationToken getVerificationToken(String token) {
        return tokenRepository.findByToken(token);
    }

    @Override
    public void saveRegisteredDoctor(Doctor doctor) {
        doctorRepository.save(doctor);
    }

    @Override
    public VerificationToken generateNewVerificationToken(final String existingToken) {
        VerificationToken token = tokenRepository.findByToken(existingToken);
        token.updateToken(UUID.randomUUID().toString());
        token = tokenRepository.save(token);
        return token;
    }

    @Override
    public void createPasswordResetTokenForDoctor(Doctor doctor, String token) {
        PasswordResetToken resetToken = new PasswordResetToken(doctor, token);
        passwordResetTokenRepository.save(resetToken);
    }

    @Override
    public void changePassword(Doctor doctor, String password) {
        doctor.setPassword(bCryptPasswordEncoder.encode(password));
        doctorRepository.save(doctor);
    }
}
