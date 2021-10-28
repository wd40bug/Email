package com.github.bobbobbob15;


import com.google.gson.Gson;
import org.simplejavamail.converter.EmailConverter;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.Properties;

public class GetInboundEmails {
    //private static final Logger logger = LoggerFactory.getLogger(GetInboundEmails.class);
    public static Message[] downloadEmails(Person user){
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
        return downloadEmails(protocol,host,port, user.getUsername(), user.getPassword());
    }
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
        return subject+" from "+from+newline+getMessageContent(message)+newline+"----------------------------------------------"+newline;


    }
    public static String messagesToString(Message[] messages) throws MessagingException, IOException {
        StringBuilder finishedString = new StringBuilder();
        for(var message : messages){
            finishedString.append(messageToString(message));
        }
        return finishedString.toString();
    }
    public static String getMessageContent(Message message) throws MessagingException, IOException {
        String result = "";
        if(message.isMimeType("text/plain")){
            result = message.getContent().toString();
        }else if(message.isMimeType("multipart/*")){
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            result = getTextFromMimeMultipart(mimeMultipart);
        }
        return result;
    }
    public static String messageToHtml(Message message) throws MessagingException, IOException {
        var result = "";
        if(message.isMimeType("text/plain")){
            return message.getContent().toString();
        } else if(message.isMimeType("multipart/*")) {
            var messageContent = (MimeMultipart)message.getContent();
            result+=multipartToHtml(messageContent);
        }
        return result;
    }
    public static MimeMessage messageToMimeMessage(Message message) throws MessagingException, IOException {
        var email = EmailConverter.mimeMessageToEmail((MimeMessage) message);
        return new MimeMessage(message.getSession(),message.getInputStream());
    }
    public static String mimeMessageToHtml(MimeMessage message){
        var email = EmailConverter.mimeMessageToEmail(message);
        return email.getHTMLText();
    }
    public  static String multipartToHtml(MimeMultipart mimeMultipart) throws MessagingException, IOException {
        StringBuilder result = new StringBuilder();
        for(int i = 0; i<mimeMultipart.getCount();i++){
            var bodyPart = mimeMultipart.getBodyPart(i);
            if(bodyPart.getContent() instanceof MimeMultipart){
                result.append("\n").append(multipartToHtml((MimeMultipart) bodyPart.getContent()));
            } else if(bodyPart.isMimeType("text/html")||bodyPart.isMimeType("text/plain")){
                result.append("\n").append((String)bodyPart.getContent());
            }
        }
        return result.toString();
    }
    public static String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws MessagingException, IOException {
        StringBuilder result = new StringBuilder();
        int count = mimeMultipart.getCount();
        var gson = new Gson();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result.append("\n").append(bodyPart.getContent());
                break; // without break same text appears twice in my tests
            } else if (bodyPart.isMimeType("text/html")) {
                String html = (String) bodyPart.getContent();
                result.append("\n").append(gson.fromJson(html, String.class));
            } else if (bodyPart.getContent() instanceof MimeMultipart){
                result.append(getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent()));
            }
        }
        return result.toString();
    }


}
