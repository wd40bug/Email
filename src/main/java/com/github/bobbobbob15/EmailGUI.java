package com.github.bobbobbob15;

import org.simplejavamail.api.mailer.config.TransportStrategy;

import javax.swing.*;
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
    private JTextField textField1;
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
    private JTextField IMAPPort;
    private JTextField POPPort;
    private JTextField SMTPPort;
    private JButton applyButton;
    private JTextField TransferProtocol;
    private JLabel AttachmentsLabel;
    private JTextField cc;
    private JTextField bcc;
    CardLayout cl = (CardLayout)rootPanel.getLayout();
    Person person;
    ArrayList<File> attachments = new ArrayList<File>();

    public EmailGUI() {
        checkBox1.addItemListener(e -> {
            if(e.getStateChange() == ItemEvent.SELECTED){
                passwordField1.setEchoChar((char)0);
            }else if(e.getStateChange() == ItemEvent.DESELECTED){
                passwordField1.setEchoChar('\u2022');
            }
        });
        loginButton.addActionListener(e -> cl.show(rootPanel,"Card4"));
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
                cl.show(rootPanel,"Card2");
            }
        });
        applyButton.addActionListener(e -> {
            person.setImapHost(ImapHost.getText());
            person.setImapPort(Integer.parseInt(IMAPPort.getText()));
            person.setPopHost(POPHost.getText());
            person.setPopPort(Integer.parseInt(POPPort.getText()));
            person.setSmtpHost(SMTPHost.getText());
            person.setSmtpPort(Integer.parseInt(SMTPPort.getText()));
            person.setTransportStrategy(TransportStrategy.valueOf(TransferProtocol.getText()));

            cl.show(rootPanel,"Card3");
        });
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                var email = SendEmails.writeBlankEmail(person,recipients.getText(),textPane1.getText(),
                        subject.getText(),cc.getText(),bcc.getText(),attachments.toArray(new File[0]));
            }
        });
        attachFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jFileChooser = new JFileChooser("Downloads");
                int r = jFileChooser.showOpenDialog(null);
                if(r==JFileChooser.APPROVE_OPTION){
                    var file = jFileChooser.getSelectedFile();
                    attachments.add(file);
                    AttachmentsLabel.setText(AttachmentsLabel.getText()+file.getPath()+", ");
                }
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("EmailGUI");
        frame.setContentPane(new EmailGUI().rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(400,300));
        frame.pack();
        frame.setVisible(true);

    }
}
