package com.github.bobbobbob15;

import org.simplejavamail.api.mailer.config.TransportStrategy;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
    private JTable table1;
    private JPanel EmailGUI;
//    private JTable table2;
    private JEditorPane editorPane1;
    private JLabel Author;
    private JLabel Subject;
    private JButton backButton;
    private JButton forwardButton;
    private JButton replyButton;
    private JPanel ReplyGUI;
    private JTextPane textPane2;
    private JButton sendButton1;
    private DefaultTableModel defaultTableModel;
    CardLayout cl = (CardLayout)rootPanel.getLayout();
    Person person;
    ArrayList<File> attachments = new ArrayList<>();
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
                System.out.println(person);
                cl.show(rootPanel,"Card2");
            }


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
            defaultTableModel.addColumn("Message");
            defaultTableModel.setRowCount(0);
            table1.setModel(defaultTableModel);
            for(var message : messages){
                var row = new Object[4];
                String messageString = null;
                try {
                    row[0] = message.getFrom()[0].toString();
                    row[1]=message.getSubject();
                    var content = GetInboundEmails.getMessageContent(message);
                    row[2] = content;
                    row[3] = message;
                } catch (MessagingException | IOException ex) {
                    ex.printStackTrace();
                }
                defaultTableModel.addRow(row);
            }
            table1.removeColumn(table1.getColumnModel().getColumn(3));
        });
        table1.getSelectionModel().addListSelectionListener(e->{
            try {
                setEmailView((Message) table1.getModel().getValueAt(table1.getSelectedRow(),3));
            } catch (MessagingException | IOException ex) {
                ex.printStackTrace();
            }
        });
        backButton.addActionListener(e -> cl.show(rootPanel,"Card2"));
        replyButton.addActionListener(e -> {
            cl.show(rootPanel,"Card6");
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("EmailGUI");
        frame.setContentPane(new EmailGUI().rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setPreferredSize(screenSize);
        frame.pack();
        frame.setVisible(true);

    }

    private void createUIComponents() {
        defaultTableModel = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        defaultTableModel.addColumn("From");
        defaultTableModel.addColumn("Subject");
        defaultTableModel.addColumn("Content");
        table1 = new JTable();
        IMAPPort = new JFormattedTextField(555);
        POPPort = new JFormattedTextField(555);
        SMTPPort = new JFormattedTextField(555);
    }
    private void setEmailView(Message message) throws MessagingException, IOException {
        Author.setText(message.getFrom()[0].toString());
        Subject.setText(message.getSubject());
        var mime = GetInboundEmails.messageToMimeMessage(message);
        var messageToHtml = GetInboundEmails.mimeMessageToHtml(mime);
//        if(messageToHtml.contains("<!DOCTYPE")) {
//            messageToHtml = messageToHtml.substring(messageToHtml.indexOf("<!DOCTYPE") - 2);
//        } else if(messageToHtml.contains("<html>")){
//            messageToHtml = messageToHtml.substring(messageToHtml.indexOf("<html>")-2);
//        }
        editorPane1.setText(messageToHtml);
        System.out.println(messageToHtml);
        cl.show(rootPanel,"Card5");
    }
}
