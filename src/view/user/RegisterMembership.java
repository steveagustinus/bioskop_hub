package src.view.user;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import src.controller.Controller;
import src.controller.UserDataSingleton;
import src.view.MainInterface;

import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionListener;

public class RegisterMembership extends JDialog implements MainInterface {
    UserDataSingleton user;
    Controller controller = new Controller();
    public RegisterMembership(Window owner){
        super(owner, ModalityType.DOCUMENT_MODAL);
        this.setLocationRelativeTo(owner);
        this.setTitle("Membership");
        this.setSize(270, 260);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setBackground(getForeground());
        panel.setLayout(null);

        JLabel mainLabel = new JLabel("Membership Menu");
        mainLabel.setBounds(43, 10, 250, 25);
        mainLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainLabel.setForeground(TEXT_BACKGROUND);
        panel.add(mainLabel);

        JButton registerButton = new JButton("Register");
        registerButton.setBounds(40, 50, 170, 25);
        registerButton.setFocusPainted(false);
        registerButton.setBackground(BUTTON_BACKGROUND);
        registerButton.setForeground(BUTTON_FOREGROUND);
        panel.add(registerButton);
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                user = UserDataSingleton.getInstance();
                int isMember = controller.checkMembership(user.getUsername());
                if(isMember == 1){
                    JOptionPane.showMessageDialog(null, "You already have a membership!");
                }else{
                    controller.raiseMembership(user.getUsername());
                    JOptionPane.showMessageDialog(null, "Membership registered!");
                }
            }
        });

        JButton backButton = new JButton("Back");
        backButton.setBounds(40, 170, 170, 25);
        backButton.setFocusPainted(false);
        backButton.setBackground(BUTTON_BACKGROUND);
        backButton.setForeground(BUTTON_FOREGROUND);
        panel.add(backButton);
        backButton.addActionListener(e -> {
            close();
        });

        JButton extendButton = new JButton("Extend");
        extendButton.setBounds(40, 90, 170, 25);
        extendButton.setFocusPainted(false);
        extendButton.setBackground(BUTTON_BACKGROUND);
        extendButton.setForeground(BUTTON_FOREGROUND);
        panel.add(extendButton);
        extendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                user = UserDataSingleton.getInstance();
                int isMember = controller.checkMembership(user.getUsername());
                if(isMember == 1){
                    controller.extendMembership(user.getUsername());
                    JOptionPane.showMessageDialog(null, "Membership extended!");
                }else{
                    JOptionPane.showMessageDialog(null, "You don't have a membership!");
                }
            }
        });

        JButton unsubscribeButton = new JButton("Unsubscribe");
        unsubscribeButton.setBounds(40, 130, 170, 25);
        unsubscribeButton.setBackground(BUTTON_BACKGROUND);
        unsubscribeButton.setForeground(BUTTON_FOREGROUND);
        panel.add(unsubscribeButton);
        unsubscribeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                user = UserDataSingleton.getInstance();
                int isMember = controller.checkMembership(user.getUsername());
                if(isMember == 1){
                    controller.revokeMembership(user.getUsername());
                    JOptionPane.showMessageDialog(null, "Membership unsubscribed!");
                }else{
                    JOptionPane.showMessageDialog(null, "You don't have a membership!");
                }
            }
        });

        this.add(panel);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

    public void close() {
        this.setVisible(false);
        this.dispose();
    }
}
