package com.github.bobbobbob15;

import org.simplejavamail.api.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;

import javax.activation.DataSource;
import javax.activation.FileDataSource;
import java.io.File;

public class SendEmails {
    public static Email writeBlankEmail(Person user, String recipients, String text, String subject, String cc,
                                        String bcc, File[] attachments){
        var emailBuilder =EmailBuilder.startingBlank()
                .from(user.getUsername())
                .to(recipients)
                .withPlainText(text)
                .withSubject(subject);
        for(var attachment:attachments){
            emailBuilder.withAttachment(attachment.getName(),new FileDataSource(attachment));
        }
        if(!cc.isEmpty()){
            emailBuilder.cc(cc);
        }
        if(!bcc.isEmpty()){
            emailBuilder.bcc(bcc);
        }
        return emailBuilder.buildEmail();
    }
    public static void sendEmail(Email email, Person user){
        MailerBuilder
                .withSMTPServer(user.getSmtpHost(), user.getSmtpPort(), user.getUsername(), user.getPassword())
                .withTransportStrategy(user.getTransportStrategy())
                .buildMailer()
                .sendMail(email);

    }
}
