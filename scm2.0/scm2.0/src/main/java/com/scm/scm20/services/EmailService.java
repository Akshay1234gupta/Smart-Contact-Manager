package com.scm.scm20.services;

public interface EmailService {

    //

    boolean sendEmail(String to,String subject,String body);

    //
    void sendEmailWithHtml();

    //
    void sendEmailWithAttachment();

}
