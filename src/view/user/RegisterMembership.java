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
        JFrame frame = new JFrame("Register Membership");
        frame.setSize(300, 100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel mainLabel = new JLabel("Register Membership");
        mainLabel.setBounds(50, 10, 250, 25);
        mainLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(mainLabel);

        JButton registerButton = new JButton("Register");
        registerButton.setBounds(150, 35, 80, 25);
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
        backButton.setBounds(50, 35, 80, 25);
        panel.add(backButton);
        backButton.addActionListener(e -> {
            frame.setVisible(false);
        });

        frame.add(panel);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
    public static void main(String[] args) {
        new RegisterMembership();
    }
}
