package com.github.bobbobbob15;

import com.google.gson.Gson;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class OtherMain {
    static File file = new File("src/main/java/com/github/bobbobbob15/login.txt");
    public static void main(String[] args) throws FileNotFoundException {
        var gson = new Gson();
        Scanner sc = new Scanner(file);
        var user = gson.fromJson(sc.nextLine(),Person.class);
        Email email = EmailBuilder.startingBlank()
                .to("wd40bug@gmail.com")
                .from(user.getUsername())
                .withSubject("Yo")
                .withPlainText("First email with simple email")
                .buildEmail();
        MailerBuilder
                .withSMTPServer("smtp.gmail.com",465)
                .withSMTPServerUsername(user.getUsername())
                .withSMTPServerPassword(user.getPassword())
                .withTransportStrategy(TransportStrategy.SMTPS)
                .buildMailer()
                .sendMail(email);
    }
}
