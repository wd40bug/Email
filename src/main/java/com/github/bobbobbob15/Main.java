package com.github.bobbobbob15;


import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.io.*;
import java.util.Properties;

public class Main {
    static String loginFile = "C:/Users/wd40b/eclipse-workspace/Email/src/main/java/com/github/bobbobbob15/login.txt";
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        FileOutputStream fileOutputStream = new FileOutputStream(loginFile);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        var user = new User();
        user.setUsername("wd50bug@gmail.com");
        user.setPassword("mR2D8Z5RhLrnjzw");
        objectOutputStream.writeObject(user);
        objectOutputStream.flush();
        objectOutputStream.close();
        FileInputStream fileInputStream = new FileInputStream(loginFile);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        user = (User) objectInputStream.readObject();
        objectInputStream.close();
        System.out.println(user.getUsername()+", "+user.getPassword());
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "25");
        prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        User finalUser = user;
        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(finalUser.getUsername(), finalUser.getPassword());
            }
        });
    }
}
