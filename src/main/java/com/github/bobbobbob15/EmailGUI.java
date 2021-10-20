package com.github.bobbobbob15;

import org.simplejavamail.api.mailer.config.TransportStrategy;

import javax.mail.MessagingException;
import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

@SuppressWarnings("unused")
public class EmailGUI {
    private JPanel rootPanel;
    private JPanel loginPanel;
    private JPasswordField passwordField1;
    private JTextField username;
    private JCheckBox checkBox1;
    private JButton loginButton;
    private JPanel Inbox;
    private JPanel InboxLayout;
    private JButton composeButton;
    private JPanel Compose;
    private JPanel ComposePanel;
    private JTextField recipients;
    private JTextField subject;
    private JTextPane textPane1;
    private JButton attachFileButton;
    private JButton sendButton;
    private JList<String> list1;
    private JButton loginFromFileButton;
    private JButton refreshButton;
    private JPanel HostsAndStuff;
    private JTextField ImapHost;
    private JTextField POPHost;
    private JTextField SMTPHost;
    private JFormattedTextField IMAPPort;
    private JFormattedTextField POPPort;
    private JFormattedTextField SMTPPort;
    private JButton applyButton;
    private JTextField TransferProtocol;
    private JTextField cc;
    private JTextField bcc;
    private JTextField attachmentText;
    private DefaultListModel<String> defaultListModel;
    CardLayout cl = (CardLayout)rootPanel.getLayout();
    Person person;
    ArrayList<File> attachments = new ArrayList<File>();
    private final JTextComponent[] textFieldsCompose = {recipients,subject,cc,bcc,textPane1,attachmentText};
    public EmailGUI() {
        checkBox1.addItemListener(e -> {
            if(e.getStateChange() == ItemEvent.SELECTED){
                passwordField1.setEchoChar((char)0);
            }else if(e.getStateChange() == ItemEvent.DESELECTED){
                passwordField1.setEchoChar('\u2022');
            }
        });
        loginButton.addActionListener(e -> {
            person = new Person(username.getText(),new String(passwordField1.getPassword()));
            cl.show(rootPanel,"Card4");
        });
        composeButton.addActionListener(e -> cl.show(rootPanel,"Card3"));
        loginFromFileButton.addActionListener(e -> {
            JFileChooser jFileChooser = new JFileChooser("Documents");
//            jFileChooser.showOpenDialog(null);
            int r = jFileChooser.showOpenDialog(null);

            // if the user selects a file
            if (r == JFileChooser.APPROVE_OPTION) {
                File file = jFileChooser.getSelectedFile();
                try {
                    person = Main.getUserFromFile(file);
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }

            }

            cl.show(rootPanel,"Card2");
        });
        applyButton.addActionListener(e -> {
            person.setImapHost(ImapHost.getText());
            person.setImapPort((int)IMAPPort.getValue());
            person.setPopHost(POPHost.getText());
            person.setPopPort((int)POPPort.getValue());
            person.setSmtpHost(SMTPHost.getText());
            person.setSmtpPort((int)SMTPPort.getValue());
            person.setTransportStrategy(TransportStrategy.valueOf(TransferProtocol.getText()));

            cl.show(rootPanel,"Card2");
        });
        sendButton.addActionListener(e -> {
            var email = SendEmails.writeBlankEmail(person,recipients.getText(),textPane1.getText(),
                    subject.getText(),cc.getText(),bcc.getText(),attachments.toArray(new File[0]));
            SendEmails.sendEmail(email,person);
            subject.setText("");
            for(var comp : textFieldsCompose){
                comp.setText("");

            }
            cl.show(rootPanel,"Card2");
        });
        attachFileButton.addActionListener(e -> {
            JFileChooser jFileChooser = new JFileChooser("Downloads");
            int r = jFileChooser.showOpenDialog(null);
            if(r==JFileChooser.APPROVE_OPTION){
                var file = jFileChooser.getSelectedFile();
                attachments.add(file);
                attachmentText.setText(attachmentText.getText()+file.getPath()+", ");
            }
        });
        refreshButton.addActionListener(e -> {
            var messages = GetInboundEmails.downloadEmails(person);
            for(var message : messages){
                String messageString = null;
                try {
                    messageString = message.getFrom()[1].toString();
                } catch (MessagingException ex) {
                    ex.printStackTrace();
                }
                defaultListModel.addElement(messageString);
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("EmailGUI");
        frame.setContentPane(new EmailGUI().rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(800,600));
        frame.pack();
        frame.setVisible(true);

    }

    private void createUIComponents() {
        list1 = new JList<String>(defaultListModel);
        IMAPPort = new JFormattedTextField(555);
        POPPort = new JFormattedTextField(555);
        SMTPPort = new JFormattedTextField(555);
    }
}
