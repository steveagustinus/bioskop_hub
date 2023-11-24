package src.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import src.model.user.User;
import src.view.admin.MainMenuScreen;
import src.view.user.MainMenuUserScreen;
import src.controller.Controller;

public class LoginScreen {
    public LoginScreen() {
        Controller controller = new Controller();
        controller.programStart();

        JFrame loginJFrame = new JFrame();

        loginJFrame.setTitle("Login");
        loginJFrame.setSize(350, 290);
        loginJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel mainlabel = new JLabel("Welcome to Bioskop HUB!");
        mainlabel.setBounds(30, 10, 500, 50);
        mainlabel.setFont(new Font("Arial", Font.BOLD, 18));

        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setBounds(30, 50, 80, 25);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(30, 100, 80, 25);

        JTextField usernameField = new JTextField(20);
        usernameField.setBounds(30, 70, 250, 25);
        usernameField.setText("Enter your Username");
        usernameField.setForeground(Color.GRAY);
        usernameField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (usernameField.getText().equals("Enter your Username")) {
                    usernameField.setText("");
                    usernameField.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (usernameField.getText().equals("")) {
                    usernameField.setText("Enter your Username");
                    usernameField.setForeground(Color.GRAY);
                }
            }
        });

        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setBounds(30, 120, 250, 25);
        controller.setPlaceholder(passwordField, "Enter your Password");

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(210, 170, 80, 25);

        JLabel registerLabel = new JLabel("Don't have an account?");
        registerLabel.setBounds(10, 150, 150, 25);

        JButton registerButton = new JButton("Register");
        registerButton.setBounds(20, 170, 100, 25);
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RegisterScreen();
                loginJFrame.dispose();
            }
        });

        JButton exiButton = new JButton("Exit");
        exiButton.setBounds(120, 210, 80, 25);
        exiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int temp = JOptionPane.showOptionDialog(null, "Are you sure you want to exit?", "Exit",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
                if (temp == 0) {
                    JOptionPane.showMessageDialog(null, "Thank you for using Bioskop HUB!");
                    loginJFrame.dispose();
                    System.exit(0);
                }
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(null);

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(registerButton);
        panel.add(registerLabel);
        panel.add(exiButton);
        panel.add(mainlabel);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = "";
                char[] pass = passwordField.getPassword();
                for (char chr : pass) {
                    password += chr;
                }
                User user = new Controller().login(username, password);
                if (user == null) {
                    JOptionPane.showMessageDialog(null, "Invalid username or password!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int checkUserType = new Controller().checkUserType(user);
                int insertToSingelton = new Controller().insertToSingelton(user.getUsername());
                if (insertToSingelton == -1) {
                    JOptionPane.showMessageDialog(null, "Failed to insert to singleton!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (checkUserType == 0) {
                    JOptionPane.showMessageDialog(null, "Welcome to admin menu!");
                    new MainMenuScreen();
                    loginJFrame.dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Welcome, " + usernameField.getText() + "!");
                    int checkMembership = new Controller().checkMembership(username);
                    if (checkMembership == 1) {
                        int checkExpired = new Controller().checkExpiredMembership(username);
                        if (checkExpired == 1) {
                            JOptionPane.showMessageDialog(null, "Membership has expired!", "Warning",
                                    JOptionPane.WARNING_MESSAGE);
                            controller.revokeMembership(username);
                        } else if (checkExpired <= 7) {
                            JOptionPane.showMessageDialog(null,
                                    "Membership will expire in " + checkExpired + " days!",
                                    "Warning", JOptionPane.WARNING_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "Membership is still active!");
                        }
                    }
                    new MainMenuUserScreen();
                    loginJFrame.dispose();
                }
            }
        });
        loginJFrame.add(panel);
        loginJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginJFrame.setLocationRelativeTo(null);
        loginJFrame.setVisible(true);
    }
}