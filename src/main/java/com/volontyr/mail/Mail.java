package com.volontyr.mail;

import java.util.Date;

/**
 * Created by volontyr on 04.05.17.
 */
public class Mail {

    private String mailFrom;
    private String mailTo;
    private String mailSubject;
    private String mailContent;
    private String templateName;
    private String contentType;

    public Mail() {
        contentType = "text/html";
        mailFrom = "alex.volontyr@gmail.com";
        templateName = "emailtemplate.vm";
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getMailFrom() {
        return mailFrom;
    }

    public void setMailFrom(String mailFrom) {
        this.mailFrom = mailFrom;
    }

    public String getMailSubject() {
        return mailSubject;
    }

    public void setMailSubject(String mailSubject) {
        this.mailSubject = mailSubject;
    }

    public String getMailTo() {
        return mailTo;
    }

    public void setMailTo(String mailTo) {
        this.mailTo = mailTo;
    }

    public Date getMailSendDate() {
        return new Date();
    }

    public String getMailContent() {
        return mailContent;
    }

    public void setMailContent(String mailContent) {
        this.mailContent = mailContent;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Mail From:- ").append(getMailFrom());
        stringBuilder.append("Mail To:- ").append(getMailTo());
        stringBuilder.append("Mail Subject:- ").append(getMailSubject());
        stringBuilder.append("Mail Send Date:- ").append(getMailSendDate());
        stringBuilder.append("Mail Content:- ").append(getMailContent());
        return stringBuilder.toString();
    }
}
