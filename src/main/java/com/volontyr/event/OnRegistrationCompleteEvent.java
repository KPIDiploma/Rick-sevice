package com.volontyr.event;

import com.volontyr.model.Doctor;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

/**
 * Created by volontyr on 04.05.17.
 */
public class OnRegistrationCompleteEvent extends ApplicationEvent {

    private String appUrl;
    private Doctor doctor;
    private String password;

    public OnRegistrationCompleteEvent(Doctor doctor, String appUrl, String password) {
        super(doctor);
        this.doctor = doctor;
        this.appUrl = appUrl;
        this.password = password;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
