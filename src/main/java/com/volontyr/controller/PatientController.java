package com.volontyr.controller;

import com.volontyr.model.Doctor;
import com.volontyr.model.Gender;
import com.volontyr.model.Patient;
import com.volontyr.service.DoctorService;
import com.volontyr.service.GenderService;
import com.volontyr.service.PatientService;
import com.volontyr.service.SecurityService;
import com.volontyr.validator.PatientValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by volontyr on 18.03.17.
 */
@Controller
public class PatientController {

    @Autowired
    private PatientService patientService;
    @Autowired
    private DoctorService doctorService;
    @Autowired
    private SecurityService securityService;
    @Autowired
    private PatientValidator validator;

    @ModelAttribute("doctor")
    public Doctor getCurrentDoctor() {
        return doctorService.findByEmail(securityService.findLoggedInUsername());
    }

    @RequestMapping(value = "/patients/add", method = RequestMethod.GET)
    public String addPatient(@ModelAttribute("patient") Patient patient, Model model) {

        model.addAttribute("genderValues", GenderService.getGenders());
        model.addAttribute("patient", new Patient());

        return "addPatient";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @RequestMapping(value = "/patients/add", method = RequestMethod.POST)
    public String registerPatient(@ModelAttribute("patient") Patient patient, BindingResult result, Model model) {
        validator.validate(patient, result);

        if (result.hasErrors()) {
            model.addAttribute("genderValues", GenderService.getGenders());
            return "addPatient";
        }

        patientService.save(patient);

        return "patientRegistrationSuccess";
    }
}
