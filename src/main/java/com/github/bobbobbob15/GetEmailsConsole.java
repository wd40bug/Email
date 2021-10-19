package com.github.bobbobbob15;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;

public class GetEmailsConsole {
    public static void main(String[] args) throws MessagingException, IOException {
        var file = new File("src/main/java/com/github/bobbobbob15/login.json");
        var user = Main.getUserFromFile(file);
        System.out.println(user);
        var messages = GetInboundEmails.downloadEmails(user);
        var messagesString = GetInboundEmails.messagesToString(messages);
        System.out.println(messagesString);
    }
}
