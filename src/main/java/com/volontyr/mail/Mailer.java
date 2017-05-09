package com.volontyr.mail;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import java.io.StringWriter;

/**
 * Created by volontyr on 04.05.17.
 */
public class Mailer {

    private MailSender mailSender;
    private VelocityEngine velocityEngine;

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

    public void send(Mail mail, String fullNameMailTo) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(mail.getMailFrom());
        message.setTo(mail.getMailTo());
        message.setSubject(mail.getMailSubject());

        Template template = velocityEngine.getTemplate("./velocity/" + mail.getTemplateName());

        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("name", fullNameMailTo);
        velocityContext.put("content", mail.getMailContent());

        StringWriter stringWriter = new StringWriter();

        template.merge(velocityContext, stringWriter);

        message.setText(stringWriter.toString());

        mailSender.send(message);
    }
}
