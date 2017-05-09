package com.volontyr.service;

import com.volontyr.model.Patient;
import com.volontyr.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by volontyr on 22.03.17.
 */
@Service("patientService")
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void save(Patient patient) {
        patient.setPassword(bCryptPasswordEncoder.encode(patient.getPassword()));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/mm/dd", Locale.ENGLISH);
        Date birthDate = Calendar.getInstance().getTime();
        Date registrationDate = Calendar.getInstance().getTime();

        try {
            birthDate = dateFormat.parse(dateFormat.format(patient.getBirthDate()));
            registrationDate = dateFormat.parse(dateFormat.format(Calendar.getInstance().getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        patient.setBirthDate(birthDate);
        patient.setRegistrationDate(registrationDate);

        patientRepository.save(patient);
    }

    @Override
    public Patient findByEmail(String email) {
        return patientRepository.findByEmail(email);
    }
}
