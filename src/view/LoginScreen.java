package src.view;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginScreen {
    public LoginScreen() {
        JFrame loginJFrame = new JFrame();

        JTextField usernameField;
        JPasswordField passwordField;
        

        loginJFrame.setTitle("Login");
        loginJFrame.setSize(300, 150);

        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");

        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        JButton loginButton = new JButton("Login");

        JPanel panel = new JPanel(new GridLayout(3, 2));

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(new JLabel());
        panel.add(loginButton);

        loginJFrame.add(panel);
        loginJFrame.setVisible(true);
    }
}