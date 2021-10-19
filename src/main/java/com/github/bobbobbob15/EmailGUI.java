package com.github.bobbobbob15;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.io.FileNotFoundException;

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
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JTextPane textPane1;
    private JButton attachFileButton;
    private JButton CCButton1;
    private JButton BCCButton;
    private JButton sendButton;
    private JList<String> list1;
    private JButton loginFromFileButton;
    private JButton refreshButton;
    private JPanel HostsAndStuff;
    CardLayout cl = (CardLayout)rootPanel.getLayout();
    Person person;

    public EmailGUI() {
        checkBox1.addItemListener(e -> {
            if(e.getStateChange() == ItemEvent.SELECTED){
                passwordField1.setEchoChar((char)0);
            }else if(e.getStateChange() == ItemEvent.DESELECTED){
                passwordField1.setEchoChar('\u2022');
            }
        });
        loginButton.addActionListener(e -> {
            cl.show(rootPanel,"Card2");
            rootPanel.revalidate();
        });
        composeButton.addActionListener(e -> cl.show(rootPanel,"Card3"));
        loginFromFileButton.addActionListener(e -> {
            JFileChooser jFileChooser = new JFileChooser("Documents");
            jFileChooser.showOpenDialog(null);
            int r = jFileChooser.showOpenDialog(null);

            // if the user selects a file
            if (r == JFileChooser.APPROVE_OPTION) {
                var file = jFileChooser.getSelectedFile();
                try {
                    person = Main.getUserFromFile(file);
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
                cl.show(rootPanel,"Card2");
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("EmailGUI");
        frame.setContentPane(new EmailGUI().rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(400,300));
        frame.pack();
//        frame.setResizable(false);
        frame.setVisible(true);

    }
}
