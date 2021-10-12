package com.github.bobbobbob15;

import com.google.gson.Gson;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import javax.jnlp.*;

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
    private JList list1;
    private JButton loginFromFileButton;
    CardLayout cl = (CardLayout)rootPanel.getLayout();
    Person person;

    public EmailGUI() {
        checkBox1.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED){
                    passwordField1.setEchoChar((char)0);
                }else if(e.getStateChange() == ItemEvent.DESELECTED){
                    passwordField1.setEchoChar('\u2022');
                }
            }
        });
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cl.show(rootPanel,"Card2");
                rootPanel.revalidate();
            }
        });
        composeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cl.show(rootPanel,"Card3");
            }
        });
        loginFromFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
