package src.view;

import javax.swing.*;

import src.controller.Controller;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterScreen {
    JFrame frame = new JFrame();
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField emailField;
    private JTextField phoneNumField;
    private JTextField addressField;

    public RegisterScreen() {
        frame.setTitle("Register");

        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        JLabel emailLabel = new JLabel("Email:");
        JLabel phoneNumLabel = new JLabel("Phone Number:");
        JLabel addressLabel = new JLabel("Address:");
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        emailField = new JTextField(20);
        phoneNumField = new JTextField(20);
        addressField = new JTextField(20);
        JButton registerButton = new JButton("Register");

        JPanel panel = new JPanel(new GridLayout(6, 2));

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(phoneNumLabel);
        panel.add(phoneNumField);
        panel.add(addressLabel);
        panel.add(addressField);
        panel.add(new JLabel()); 
        panel.add(registerButton);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = passwordField.getText();
                String email = emailField.getText();
                String phoneNum = phoneNumField.getText();
                String address = addressField.getText();
                int user = new Controller().register(username, password, email, phoneNum, address);
                if(user == -6) {
                    JOptionPane.showMessageDialog(null, "Username already exists!");
                    return;
                }
                JOptionPane.showMessageDialog(null, "Registration " + username +" successful!");
                frame.dispose();
            }
        });

        frame.add(panel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLocationRelativeTo(null); 
        frame.setVisible(true);
    }
}
