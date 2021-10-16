package com.github.bobbobbob15;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.mail.*;
import java.io.IOException;
import java.util.Properties;

public class GetInboundEmails {
    private static final Logger logger = LoggerFactory.getLogger(GetInboundEmails.class);
    public static Message[] downloadEmails(String protocol, String host, int port, String username, String password){
        Properties prop = getServerProperties(host,port,protocol);
        Session session = Session.getDefaultInstance(prop);
        Message[] messages = new Message[0];
        try{
            Store store = session.getStore(protocol);
            store.connect(username,password);
            Folder folderInbox = store.getFolder("INBOX");
            folderInbox.open(Folder.READ_ONLY);
            messages = folderInbox.getMessages();
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return messages;
    }
    private static Properties getServerProperties(String host, int port, String protocol){
        Properties properties = new Properties();
        properties.put(String.format("mail.%s.host", protocol), host);
        properties.put(String.format("mail.%s.port", protocol), port);

        properties.setProperty(
                String.format("mail.%s.socketFactory.class", protocol),
                "javax.net.ssl.SSLSocketFactory");
        properties.setProperty(
                String.format("mail.%s.socketFactory.fallback", protocol),
                "false");
        properties.setProperty(
                String.format("mail.%s.socketFactory.port", protocol),
                String.valueOf(port));
        return properties;
    }
    public static String messageToString(Message message) throws MessagingException, IOException {

        var from = message.getFrom()[0].toString();
        var subject = message.getSubject();
        var newline = System.getProperty("line.separator");
        var content = "";
        if(message.getContentType().contains("text/plain")||message.getContentType().contains("text/html")){
            content = message.getContent().toString();
        }
        return subject+" from "+from+newline+message.getContent().toString()+newline+"----------------------------------------------"+newline;


    }
    public static String messagesToString(Message[] messages) throws MessagingException, IOException {
        StringBuilder finishedString = new StringBuilder();
        for(var message : messages){
            finishedString.append(messageToString(message));
        }
        return finishedString.toString();
    }


}
