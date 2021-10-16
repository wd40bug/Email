package com.github.bobbobbob15;

import org.hazlewood.connor.bottema.emailaddress.EmailAddressValidator;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.email.Recipient;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;

import javax.mail.Message;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class ConsoleEmail {
    static final File file = new File("src/main/java/com/github/bobbobbob15/login.json");
    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        System.out.println("Load from file?(y/n)");
        Person user = null;
        if(in.nextLine().charAt(0)=='y'){
            user = Main.getUserFromFile(file);
        } else{
            user = new Person();
        }
        var email = writeBlankEmail(user);
        sendEmail(email,user);
    }
    public static Email writeBlankEmail(Person user) {
        Scanner in = new Scanner(System.in);
        var recipients = getRecipients(user, in);
        System.out.println("What is the subject of your email?");
        var subject = in.nextLine();
        System.out.println("What is the body of your email?");
        var body = in.nextLine();

        return EmailBuilder.startingBlank()
                .from(user.getUsername())
                .to(recipients)
                .withSubject(subject)
                .withPlainText(body)
                .buildEmail();
    }
    public static void sendEmail(Email email, Person user){
        MailerBuilder
                .withSMTPServer("smtp.gmail.com", 465)
                .withSMTPServerUsername(user.getUsername())
                .withSMTPServerPassword(user.getPassword())
                .withTransportStrategy(TransportStrategy.SMTPS)
                .buildMailer()
                .sendMail(email);
    }
    public static Recipient[] getRecipients(Person user, Scanner in) {
        ArrayList<Recipient> recipients = new ArrayList<>();
        boolean keepGoing = true;
        while (keepGoing) {
            System.out.println("Add a recipient's information(Name*,Email,Send Type*) * is optional");
            var recipientString = in.nextLine().split(",");
            String address = null;
            String name = null;
            Message.RecipientType sendType = null;
            for (var string : recipientString) { //should work if nothing goes wrong
                if (EmailAddressValidator.isValid(string)) {
                    address = string;
                } else if (string.equalsIgnoreCase("TO") || string.equalsIgnoreCase("CC") ||
                        string.equalsIgnoreCase("BCC")) {
                    sendType = switch (string.toLowerCase(Locale.ROOT)) {
                        case "to" -> {
                            yield Message.RecipientType.TO;
                        }
                        case "cc" -> {
                            yield Message.RecipientType.CC;
                        }
                        case "bcc" -> {
                            yield Message.RecipientType.BCC;
                        }
                        default -> {yield null;}
                    };

                } else {
                    name = string;
                }
            }
            if(address == null){
                System.out.println("No valid email address found");
                return getRecipients(user, in);
            }
            if(sendType == null){
                sendType = Message.RecipientType.TO;
            }
            recipients.add(new Recipient(name,address,sendType));
            System.out.println("Add more recipients?(y/n)");
            char userInput = in.nextLine().toLowerCase(Locale.ROOT).charAt(0);
            if (userInput == 'n') {
                keepGoing = false;
            }
        }
        return recipients.toArray(Recipient[]::new);
    }
}
