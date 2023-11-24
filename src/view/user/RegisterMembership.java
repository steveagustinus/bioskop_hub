package src.view.user;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import src.controller.Controller;
import src.controller.UserDataSingleton;

import java.awt.Font;
import java.awt.event.ActionListener;

public class RegisterMembership {
    UserDataSingleton user;
    Controller controller = new Controller();
    public RegisterMembership(){
        JFrame frame = new JFrame("Membership");
        frame.setSize(270, 260);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel mainLabel = new JLabel("Membership Menu");
        mainLabel.setBounds(43, 10, 250, 25);
        mainLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(mainLabel);

        JButton registerButton = new JButton("Register");
        registerButton.setBounds(40, 50, 170, 25);
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
        panel.add(backButton);
        backButton.addActionListener(e -> {
            frame.setVisible(false);
            new MainMenuUserScreen();
        });

        JButton extendButton = new JButton("Extend");
        extendButton.setBounds(40, 90, 170, 25);
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

        frame.add(panel);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
}
