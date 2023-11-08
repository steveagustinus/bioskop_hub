package src.view;

import java.awt.GridLayout;
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
import src.controller.Controller;

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

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = passwordField.getText();
                User user = new Controller().login(username, password);
                if(user == null) {
                    JOptionPane.showMessageDialog(null, "Invalid username or password!");
                    return;
                }
                JOptionPane.showMessageDialog(null, "Welcome, " + username + "!");
                loginJFrame.dispose();
                new MainMenuScreen();
            }
        });
        loginJFrame.add(panel);

        loginJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginJFrame.setSize(300, 150);
        loginJFrame.setLocationRelativeTo(null); 
        loginJFrame.setVisible(true);
    }
}