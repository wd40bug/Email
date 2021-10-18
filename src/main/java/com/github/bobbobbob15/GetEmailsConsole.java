package com.github.bobbobbob15;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;

public class GetEmailsConsole {
    public static void main(String[] args) throws MessagingException, IOException {
        var file = new File("src/main/java/com/github/bobbobbob15/login.json");
        var user = Main.getUserFromFile(file);
        System.out.println(user);
        String protocol = null;
        String host = null;
        int port = 0;
        if(!user.getImapHost().isEmpty()){
            protocol = "imap";
            host = user.getImapHost();
            port = user.getImapPort();
        } else if(!user.getPopHost().isEmpty()){
            protocol = "pop3";
            host = user.getPopHost();
            port = user.getPopPort();
        }
        var messages = GetInboundEmails.downloadEmails(protocol,host,port,user.getUsername(),user.getPassword());
        var messagesString = GetInboundEmails.messagesToString(messages);
        System.out.println(messagesString);
    }
}
