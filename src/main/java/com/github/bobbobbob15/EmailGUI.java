package com.github.bobbobbob15;

import javax.swing.*;
import java.awt.event.*;

public class EmailGUI {
    private JPanel rootPanel;
    private JPanel loginPanel;
    private JPasswordField passwordField1;
    private JTextField textField1;
    private JCheckBox checkBox1;
    private JButton loginButton;


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
                var frame = new JFrame();
                var text = new JLabel("Login Successful!");
                frame.add(text);
                frame.setVisible(true);
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("EmailGUI");
        frame.setContentPane(new EmailGUI().rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
