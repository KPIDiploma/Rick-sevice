package com.volontyr.controller;

import com.volontyr.api.PatientAPI;
import com.volontyr.event.OnRegistrationCompleteEvent;
import com.volontyr.mail.Mail;
import com.volontyr.mail.Mailer;
import com.volontyr.model.Doctor;
import com.volontyr.model.VerificationToken;
import com.volontyr.service.DoctorService;
import com.volontyr.service.GenderService;
import com.volontyr.service.RoleService;
import com.volontyr.service.SecurityService;
import com.volontyr.validator.DoctorValidator;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.UUID;

/**
 * Created by volontyr on 15.03.17.
 */
@Controller
public class DoctorController {

    private static final Logger log = LoggerFactory.getLogger(DoctorController.class);

    @Autowired
    private DoctorService doctorService;
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    @Autowired
    private Mailer mailSender;
    @Autowired
    private SecurityService securityService;
    @Qualifier("doctorValidator")
    @Autowired
    private DoctorValidator validator;

    @ModelAttribute("doctor")
    public Doctor getCurrentDoctor() {
        return doctorService.findByEmail(securityService.findLoggedInUsername());
    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registration(Model model) {

        model.addAttribute("genderValues", GenderService.getGenders());
        model.addAttribute("roleValues", RoleService.getRoles());
        model.addAttribute("doctorForm", new Doctor());

        return "registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registration(@ModelAttribute ("doctorForm") Doctor doctor, BindingResult bindingResult, Model model, HttpServletRequest request) {
        validator.validate(doctor, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("genderValues", GenderService.getGenders());
            model.addAttribute("roleValues", RoleService.getRoles());
            return "registration";
        }

        String password = doctor.getPassword();
        Doctor registered = doctorService.save(doctor);

        if (registered == null) {
            model.addAttribute("error", "Error occurred while registering doctor " + doctor.getFullName());
            return "login";
        }

        try {
            String appUrl = getURLWithContextPath(request);
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, appUrl, password));
        } catch (Exception e) {
            log.error("Error while registering. Reason: ", e);
            doctorService.remove(doctor);
            model.addAttribute("error", "Error occurred while registering doctor " + doctor.getFullName());
            return "login";
        }

        model.addAttribute("message", "Account has been registered. Please confirm it using your email");
        return "login";
    }

    @RequestMapping(value = "/registrationConfirm", method = RequestMethod.GET)
    public String confirmRegistration(Model model, @RequestParam("token") String token) {

        VerificationToken verificationToken = doctorService.getVerificationToken(token);
        if (verificationToken == null) {
            model.addAttribute("error", "Verification token is wrong");
            return "login";
        }

        Doctor doctor = verificationToken.getDoctor();
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            return "redirect:/resendRegistrationToken?token=" + token;
        }

        doctor.setEnabled(true);
        doctorService.saveRegisteredDoctor(doctor);

        model.addAttribute("message", "Account has been confirmed. Please login");
        return "login";
    }

    @RequestMapping(value = "/resendRegistrationToken", method = RequestMethod.GET)
    public String resendRegistrationToken(HttpServletRequest request, @RequestParam("token") String existingToken, Model model) {
        VerificationToken newToken = doctorService.generateNewVerificationToken(existingToken);

        Doctor doctor = doctorService.getDoctor(newToken.getToken());
        String appUrl = getURLWithContextPath(request);

        String confirmationUrl = appUrl + "/registrationConfirm?token=" + newToken.getToken();

        Mail mail = new Mail();
        mail.setMailTo(doctor.getEmail());
        mail.setMailSubject("Registration Confirmation");
        mail.setMailContent(confirmationUrl);

        try {
            mailSender.send(mail, doctor.getFullName());
        } catch (Exception e) {
            log.error("Error while resending email. Reason: ", e);
            model.addAttribute("error", "Error occurred while resending email to " + doctor.getFullName());
            return "login";
        }

        model.addAttribute("message", "Confirmation link has been resent to your email account");
        return "login";
    }

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String login(String error, String logout, Model model) {
        if (error != null)
            model.addAttribute("error", "Your username or password is invalid.");

        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");

        return "login";
    }

    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout";
    }

    @RequestMapping(value = {"/", "/welcome"}, method = RequestMethod.GET)
    public String welcome(Model model) {
        return "welcome";
    }

    @RequestMapping(value = {"/forgotPassword"}, method = RequestMethod.GET)
    public String showForgotPasswordPage() {
        return "forgotPassword";
    }

    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    public String resetPassword(HttpServletRequest request, @RequestParam("email") String email, Model model) {
        Doctor doctor = doctorService.findByEmail(email);
        if (doctor == null) {
            model.addAttribute("error", "There is no user with such email");
            return "login";
        }

        String token = UUID.randomUUID().toString();
        doctorService.createPasswordResetTokenForDoctor(doctor, token);

        String appUrl = getURLWithContextPath(request);
        String passwordResetUrl = appUrl + "/changePassword?id=" + doctor.getId() + "&token=" + token;

        Mail mail = new Mail();
        mail.setMailTo(doctor.getEmail());
        mail.setTemplateName("reset_password_email.vm");
        mail.setMailSubject("Password Reset");
        mail.setMailContent(passwordResetUrl);

        try {
            mailSender.send(mail, doctor.getFullName());
        } catch (Exception e) {
            log.error("Error while sending email. Reason: ", e);
            model.addAttribute("error", "Error occurred while sending email to " + doctor.getFullName());
            return "login";
        }

        model.addAttribute("message", "Link for password reset has been sent to your email");
        return "login";
    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.GET)
    public String showChangePasswordPage(Model model, @RequestParam("id") long id, @RequestParam("token") String token) {
        String result = securityService.validatePasswordResetToken(id, token);
        if (result != null) {
            model.addAttribute("message", result);
            return "login";
        }
        return "updatePassword";
    }

    @RequestMapping(value = "/savePassword", method = RequestMethod.POST)
    public String savePassword(Model model, @RequestParam("password") String password, @RequestParam("passwordConfirm") String passwordConfirm) {
        Doctor doctor = (Doctor) SecurityContextHolder.getContext()
                                .getAuthentication().getPrincipal();

        if (password.equals(passwordConfirm)) {
            doctorService.changePassword(doctor, password);
        } else {
            model.addAttribute("message", "Passwords mismatch");
            return "updatePassword";
        }

        model.addAttribute("message", "Password is successfully changed");
        return "login";
    }

    private String getURLWithContextPath(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}
