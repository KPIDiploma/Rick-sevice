package com.volontyr.validator;

import com.volontyr.model.Gender;
import com.volontyr.model.Patient;
import com.volontyr.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.Arrays;

/**
 * Created by volontyr on 22.03.17.
 */
@Component
public class PatientValidator extends DoctorValidator implements Validator {

    @Autowired
    private PatientService patientService;

    @Override
    public boolean supports(Class<?> aClass) {
        return Patient.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Patient patient = (Patient) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "fullName", "NotEmpty");
        if (patient.getFullName().length() < 6 || patient.getFullName().length() > 50) {
            errors.rejectValue("fullName", "Size.userForm.username");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "gender", "NotEmpty");
        if (!Arrays.asList(Gender.values()).contains(patient.getGender())) {
            errors.rejectValue("gender", "NotValid.userForm.gender");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty");
        if (!validateField(EMAIL_PATTERN, patient.getEmail())) {
            errors.rejectValue("email", "NotValid.userForm.email");
        }

        if (patientService.findByEmail(patient.getEmail()) != null) {
            errors.rejectValue("email", "Duplicate.userForm.email");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "phone", "NotEmpty");
        if (!validateField(PHONE_PATTERN, patient.getPhone())) {
            errors.rejectValue("phone", "NotValid.userForm.phone");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address", "NotEmpty");
        if (patient.getAddress().length() < 8) {
            errors.rejectValue("address", "Size.userForm.address");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty");
        if (patient.getPassword().length() < 8) {
            errors.rejectValue("password", "Size.userForm.password");
        }

        if (!patient.getPassword().equals(patient.getPasswordConfirm())) {
            errors.rejectValue("passwordConfirm", "Diff.userForm.passwordConfirm");
        }
    }
}
