package com.github.bobbobbob15;

import com.google.gson.Gson;

import javax.mail.MessagingException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, MessagingException {
        var file = new File("src/main/java/com/github/bobbobbob15/login.json");
        var user = getUserFromFile(file);
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
 public static Person getUserFromFile(File file) throws FileNotFoundException {
        var gson = new Gson();
        Scanner sc = new Scanner(file);
        var builder = new StringBuilder();
        while(sc.hasNextLine()){
            builder.append(sc.nextLine());
        }
        return gson.fromJson(builder.toString(), Person.class);
    }
}

