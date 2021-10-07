package com.github.bobbobbob15;


import com.google.gson.Gson;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.io.*;
import java.util.Properties;
import java.util.Scanner;

public class Main {
    static File file = new File("src/main/java/com/github/bobbobbob15/login.txt");
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        var gson = new Gson();
        var user = new Person();
        Scanner in = new Scanner(file);
        var fileString = in.nextLine();
        System.out.println(fileString+"is the thing");
        user = gson.fromJson(fileString, Person.class);
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "25");
        prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Person finalPerson = user;
        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(finalPerson.getUsername(), finalPerson.getPassword());
            }
        });
    }
}
