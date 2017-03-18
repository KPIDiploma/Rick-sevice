package com.volontyr.validator;

import com.volontyr.model.Doctor;
import com.volontyr.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Created by volontyr on 15.03.17.
 */
@Component
public class UserValidator implements Validator {

    @Autowired
    private DoctorService doctorService;

    @Override
    public boolean supports(Class<?> aClass) {
        return Doctor.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Doctor doctor = (Doctor) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "NotEmpty");
        if (doctor.getUsername().length() < 6 || doctor.getUsername().length() > 32) {
            errors.rejectValue("username", "Size.userForm.username");
        }
        if (doctorService.findByUsername(doctor.getUsername()) != null) {
            errors.rejectValue("username", "Duplicate.userForm.username");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty");
        if (doctor.getPassword().length() < 8 || doctor.getPassword().length() > 32) {
            errors.rejectValue("password", "Size.userForm.password");
        }

        if (!doctor.getPasswordConfirm().equals(doctor.getPassword())) {
            errors.rejectValue("passwordConfirm", "Diff.userForm.passwordConfirm");
        }
    }
}
