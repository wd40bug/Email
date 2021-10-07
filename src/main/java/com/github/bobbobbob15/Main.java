package com.github.bobbobbob15;


import com.google.gson.Gson;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.*;
import java.util.Properties;
import java.util.Scanner;

public class Main {
    static File file = new File("src/main/java/com/github/bobbobbob15/login.txt");
    public static void main(String[] args) throws IOException, MessagingException {
        var gson = new Gson();
        var user = new Person();
        Scanner in = new Scanner(file);
        var fileString = in.nextLine();
        System.out.println(fileString+"is the thing");
        user = gson.fromJson(fileString, Person.class);
        Properties prop = System.getProperties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "465");
        prop.put("mail.smtp.ssl.enable", "true");
        prop.put("mail.smtp.auth", "true");


        Person finalPerson = user;
        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(finalPerson.getUsername(), finalPerson.getPassword());
            }
        });
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(user.getUsername()));
        message.setRecipients(
                Message.RecipientType.TO, InternetAddress.parse("wd40bug@gmail.com"));
        message.setSubject("Mail Subject");

        String msg = "This is my first email using JavaMailer";

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(msg, "text/html");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        message.setContent(multipart);
        Transport.send(message);
    }
}
