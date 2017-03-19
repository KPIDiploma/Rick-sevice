package com.volontyr.controller;

import com.volontyr.model.Patient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by volontyr on 18.03.17.
 */
@Controller
public class PatientController {

    @PreAuthorize("hasRole('ROLE_ADMIN') and hasPermission(#patient, 'addPatient')")
    @RequestMapping(value = "/patients/add", method = RequestMethod.GET)
    public String addPatient(@ModelAttribute("patient") Patient patient) {
        return "addPatient";
    }
}
