package com.volontyr.service;

import com.volontyr.model.Patient;

/**
 * Created by volontyr on 22.03.17.
 */
public interface PatientService {

    void save(Patient patient);

    Patient findByEmail(String email);
}
