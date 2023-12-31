package src.view;

import javax.swing.*;

import src.controller.Controller;
import src.view.user.MainMenuUserScreen;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterScreen implements MainInterface {
    JFrame frame = new JFrame();
    Controller controller = new Controller();

    public RegisterScreen() {
        frame.setTitle("Register");
        frame.setBackground(FRAME_BACKGROUND);

        JLabel mainlabel = new JLabel("Register to Bioskop HUB!");
        mainlabel.setBounds(30, 10, 500, 50);
        mainlabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainlabel.setForeground(TEXT_BACKGROUND);

        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setBounds(20, 50, 80, 25);
        usernameLabel.setForeground(TEXT_BACKGROUND);

        JTextField usernameField = new JTextField(20);
        usernameField.setBounds(20, 70, 230, 25);
        usernameField.setText("Enter your Username");
        usernameField.setBackground(FRAME_BACKGROUND);
        usernameField.setForeground(Color.GRAY);
        usernameField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (usernameField.getText().equals("Enter your Username")) {
                    usernameField.setText("");
                    usernameField.setForeground(TEXT_BACKGROUND);
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (usernameField.getText().equals("")) {
                    usernameField.setText("Enter your Username");
                    usernameField.setForeground(Color.GRAY);
                }
            }
        });

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(20, 90, 80, 25);
        passwordLabel.setForeground(TEXT_BACKGROUND);

        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setBounds(20, 110, 230, 25);
        passwordField.setBackground(FRAME_BACKGROUND);
        controller.setPlaceholder(passwordField, "Enter your Password");

        JLabel confirmPasswordLabel = new JLabel("Confirm Password");
        confirmPasswordLabel.setBounds(20, 130, 160, 25);
        confirmPasswordLabel.setForeground(TEXT_BACKGROUND);

        JPasswordField confirmPasswordField = new JPasswordField(20);
        confirmPasswordField.setBounds(20, 150, 230, 25);
        confirmPasswordField.setBackground(FRAME_BACKGROUND);
        controller.setPlaceholder(confirmPasswordField, "Re-enter your Password");

        JLabel emailLabel = new JLabel("Email");
        emailLabel.setBounds(20, 170, 80, 25);
        emailLabel.setForeground(TEXT_BACKGROUND);

        JTextField emailField = new JTextField(20);
        emailField.setBounds(20, 190, 230, 25);
        emailField.setText("Enter your Email");
        emailField.setBackground(FRAME_BACKGROUND);
        emailField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (emailField.getText().equals("Enter your Email")) {
                    emailField.setText("");
                    emailField.setForeground(TEXT_BACKGROUND);
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (emailField.getText().equals("")) {
                    emailField.setText("Enter your Email");
                    emailField.setForeground(TEXT_BACKGROUND);
                }
            }
        });

        JLabel phoneNumLabel = new JLabel("Phone Number");
        phoneNumLabel.setBounds(20, 210, 120, 25);
        phoneNumLabel.setForeground(TEXT_BACKGROUND);

        JTextField phoneNumField = new JTextField(20);
        phoneNumField.setBounds(20, 230, 230, 25);
        phoneNumField.setText("Enter your Phone Number");
        phoneNumField.setBackground(FRAME_BACKGROUND);
        phoneNumField.setForeground(Color.GRAY);
        phoneNumField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (phoneNumField.getText().equals("Enter your Phone Number")) {
                    phoneNumField.setText("");
                    phoneNumField.setForeground(TEXT_BACKGROUND);
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (phoneNumField.getText().equals("")) {
                    phoneNumField.setText("Enter your Phone Number");
                    phoneNumField.setForeground(Color.GRAY);
                }
            }
        });

        JLabel addressLabel = new JLabel("Address");
        addressLabel.setBounds(20, 250, 80, 25);
        addressLabel.setForeground(TEXT_BACKGROUND);

        JTextField addressField = new JTextField(20);
        addressField.setBounds(20, 270, 230, 25);
        addressField.setText("Enter your Address");
        addressField.setBackground(FRAME_BACKGROUND);
        addressField.setForeground(Color.GRAY);
        addressField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (addressField.getText().equals("Enter your Address")) {
                    addressField.setText("");
                    addressField.setForeground(TEXT_BACKGROUND);
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (addressField.getText().equals("")) {
                    addressField.setText("Enter your Address");
                    addressField.setForeground(Color.GRAY);
                }
            }
        });

        JButton registerButton = new JButton("Register");
        registerButton.setBackground(BUTTON_BACKGROUND);
        registerButton.setForeground(BUTTON_FOREGROUND);
        registerButton.setBounds(20, 320, 230, 25);

        JLabel loginLabel = new JLabel("Already have an account?");
        loginLabel.setBounds(60, 350, 150, 25);
        loginLabel.setForeground(TEXT_BACKGROUND);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(20, 370, 230, 25);
        loginButton.setBackground(BUTTON_BACKGROUND);
        loginButton.setForeground(BUTTON_FOREGROUND);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginScreen();
                frame.dispose();
            }
        });

        JPanel panel = new JPanel();
        panel.setBackground(FRAME_BACKGROUND);
        panel.setLayout(null);

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
        panel.add(mainlabel);
        panel.add(confirmPasswordLabel);
        panel.add(confirmPasswordField);
        panel.add(loginLabel);
        panel.add(loginButton);
        panel.add(registerButton);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = "";
                char[] pass = passwordField.getPassword();
                String email = emailField.getText();
                String phoneNum = phoneNumField.getText();
                String address = addressField.getText();

                for (char chr : pass) {
                    password += chr;
                }
                
                String confirmPassword = "";
                for (char c : confirmPasswordField.getPassword()) {
                    confirmPassword += c;
                }

                int check = new Controller().checkEmptyFields(username, password, email, phoneNum, address);
                if (check == 0){
                    JOptionPane.showMessageDialog(null, "Please fill all the fields!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }else{
                    if (password.equals(confirmPassword)) {
                        int user = new Controller().register(username, password, email, phoneNum, address);
                        if (user == 0) {
                            JOptionPane.showMessageDialog(null, "Username already exists!");
                            return;
                        } else if (user == 1) {
                            int singleton = new Controller().insertToSingelton(username);
                            if (singleton == -1) {
                                JOptionPane.showMessageDialog(null, "Failed to insert to singleton!");
                                return;
                            }
                            JOptionPane.showMessageDialog(null, "Registration " + username + " successful!");
                            new MainMenuUserScreen();
                            frame.dispose();
                        }
                    }else{
                        JOptionPane.showMessageDialog(null, "Password doesn't match!" , "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            }
        });

        frame.add(panel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 450);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}