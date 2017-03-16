package com.volontyr.controller;

import com.volontyr.model.Doctor;
import com.volontyr.service.DoctorService;
import com.volontyr.service.SecurityService;
import com.volontyr.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by volontyr on 15.03.17.
 */
@Controller
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserValidator validator;

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registration(Model model) {
        model.addAttribute("doctorForm", new Doctor());

        return "registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registration(@ModelAttribute ("doctorForm") Doctor doctor, BindingResult bindingResult, Model model) {
        validator.validate(doctor, bindingResult);

        if (bindingResult.hasErrors()) {
            return "registration";
        }

        doctorService.save(doctor);

        securityService.autoLogin(doctor.getUsername(), doctor.getPasswordConfirm());

        return "rediret:/welcome";
    }

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String login(String error, String logout, Model model) {
        if (error != null)
            model.addAttribute("error", "Your username and password is invalid.");

        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");

        return "login";
    }

    @RequestMapping(value = {"/", "/welcome"}, method = RequestMethod.GET)
    public String welcome(Model model) {
        return "welcome";
    }
}
