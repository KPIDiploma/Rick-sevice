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

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "fullname", "NotEmpty");
        if (patient.getFullname().length() < 6 || patient.getFullname().length() > 50) {
            errors.rejectValue("fullname", "Size.userForm.username");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "sex", "NotEmpty");
        if (!Arrays.asList(Gender.values()).contains(patient.getSex())) {
            errors.rejectValue("sex", "NotValid.userForm.gender");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty");
        if (!validateField(EMAIL_PATTERN, patient.getEmail())) {
            errors.rejectValue("email", "NotValid.userForm.email");
        }

        if (patientService.findByEmail(patient.getEmail()) != null) {
            errors.rejectValue("email", "Duplicate.userForm.email");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "mobile", "NotEmpty");
        if (!validateField(PHONE_PATTERN, patient.getMobile())) {
            errors.rejectValue("mobile", "NotValid.userForm.phone");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address", "NotEmpty");
        if (patient.getAddress().length() < 8) {
            errors.rejectValue("address", "Size.userForm.address");
        }
    }
}
