package com.volontyr.validator;

import com.volontyr.model.Doctor;
import com.volontyr.model.Gender;
import com.volontyr.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by volontyr on 15.03.17.
 */
@Component
public class DoctorValidator implements Validator {

    @Autowired
    private DoctorService doctorService;

    static final String EMAIL_PATTERN = "^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@" +
            "[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$";
    static final String PHONE_PATTERN = "[0-9]{10}";

    @Override
    public boolean supports(Class<?> aClass) {
        return Doctor.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Doctor doctor = (Doctor) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "fullName", "NotEmpty");
        if (doctor.getFullName().length() < 6 || doctor.getFullName().length() > 50) {
            errors.rejectValue("fullName", "Size.userForm.username");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "gender", "NotEmpty");
        if (!Arrays.asList(Gender.values()).contains(doctor.getGender())) {
            errors.rejectValue("gender", "NotValid.userForm.gender");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty");
        if (!validateField(EMAIL_PATTERN, doctor.getEmail())) {
            errors.rejectValue("email", "NotValid.userForm.email");
        }

        if (doctorService.findByEmail(doctor.getEmail()) != null) {
            errors.rejectValue("email", "Duplicate.userForm.email");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "phone", "NotEmpty");
        if (!validateField(PHONE_PATTERN, doctor.getPhone())) {
            errors.rejectValue("phone", "NotValid.userForm.phone");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "position", "NotEmpty");

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty");
        if (doctor.getPassword().length() < 8 || doctor.getPassword().length() > 32) {
            errors.rejectValue("password", "Size.userForm.password");
        }

        if (!doctor.getPasswordConfirm().equals(doctor.getPassword())) {
            errors.rejectValue("passwordConfirm", "Diff.userForm.passwordConfirm");
        }
    }

    protected boolean validateField(String fieldPattern, String field) {
        Pattern pattern = Pattern.compile(fieldPattern);
        Matcher matcher = pattern.matcher(field);
        return matcher.matches();
    }
}
