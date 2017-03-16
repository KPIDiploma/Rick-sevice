package com.volontyr.service;

import com.volontyr.model.Doctor;

/**
 * Created by volontyr on 15.03.17.
 */
public interface DoctorService {

    void save(Doctor doctor);

    Doctor findByUsername(String username);
}
