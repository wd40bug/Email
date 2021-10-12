package com.github.bobbobbob15;

import java.util.Properties;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import com.sun.mail.pop3.POP3Store;

public class ReceiveMail{

    public static Message[] receiveEmail(String pop3Host, String storeType,
                                    String user, String password) {
        Message[] messages = new Message[0];
        try {
            //1) get the session object
            Properties properties = new Properties();
            properties.put("mail.pop3.host", pop3Host);
            Session emailSession = Session.getDefaultInstance(properties);

            //2) create the POP3 store object and connect with the pop server
            POP3Store emailStore = (POP3Store) emailSession.getStore(storeType);
            emailStore.connect(user, password);

            //3) create the folder object and open it
            Folder emailFolder = emailStore.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            //4) retrieve the messages from the folder in an array and print it
            messages = emailFolder.getMessages();

            //5) close the store and folder objects
            emailFolder.close(false);
            emailStore.close();



        } catch (MessagingException e) {e.printStackTrace();}
        return messages;
    }

    public static void main(String[] args) {

        String host = "mail.javatpoint.com";//change accordingly
        String mailStoreType = "pop3";
        String username= "sonoojaiswal@javatpoint.com";
        String password= "xxxxx";//change accordingly

        receiveEmail(host, mailStoreType, username, password);

    }
}  