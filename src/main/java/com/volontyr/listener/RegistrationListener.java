package com.volontyr.listener;

import com.volontyr.event.OnRegistrationCompleteEvent;
import com.volontyr.mail.Mail;
import com.volontyr.mail.Mailer;
import com.volontyr.model.Doctor;
import com.volontyr.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;

import java.util.UUID;

/**
 * Created by volontyr on 04.05.17.
 */
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    @Autowired
    private DoctorService doctorService;
    @Autowired
    private Mailer mailSender;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        Doctor doctor = event.getDoctor();
        String token = UUID.randomUUID().toString();
        doctorService.createVerificationToken(doctor, token);

        String content = (event.getAppUrl() + "/registrationConfirm?token=" + token) +
                "\nYour password is " + event.getPassword();

        Mail mail = new Mail();
        mail.setMailTo(doctor.getEmail());
        mail.setMailSubject("Registration Confirmation");
        mail.setMailContent(content);

        mailSender.send(mail, doctor.getFullName());
    }
}
