package src.view.admin;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.*;
import src.controller.Controller;
import src.model.user.User;

public class RaiseRevokeMembership {
    public RaiseRevokeMembership() {
        raiseRevokeMembership();
    }

    private void raiseRevokeMembership() {
        Controller controller = new Controller();
        JFrame frame = new JFrame();
        frame.setTitle("Revoke dan Raise Membership");
        frame.setSize(400,150);
        frame.setVisible(true);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));
        JLabel namaUser = new JLabel("Username :");
        JTextField inputUsername = new JTextField(1);
        JButton buttonRevoke = new JButton("Revoke Membership");
        JButton buttonRaise = new JButton("Raise Membership");
        JButton backButton = new JButton("Back");

        panel.add(namaUser);
        panel.add((inputUsername));
        panel.add(buttonRevoke);
        panel.add(buttonRaise);
        panel.add(new Label());
        panel.add(backButton);

        frame.add(panel);
        frame.setVisible(true);
       
    }
    public static void main(String[] args) {
        new RaiseRevokeMembership();
    }
}
