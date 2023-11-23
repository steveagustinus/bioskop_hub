package src.view.admin;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import src.controller.Controller;
import src.model.user.User;

public class RaiseRevokeMembership {
    public RaiseRevokeMembership() {
        raiseRevokeMembership();
    }

    private void raiseRevokeMembership() {
        Controller controller = new Controller();
        JFrame f = new JFrame();
        f.setTitle("Menu Raise/Revoke Membership");
        f.setSize(720, 480);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel labelInputUsername = new JLabel("Input username user: ");
        labelInputUsername.setBounds(10, 15, 200, 30);
        JTextField inputUsername = new JTextField();
        inputUsername.setBounds(170, 15, 200, 30);

        JButton buttonRaise = new JButton("Raise Membership");
        buttonRaise.setBounds(170, 220, 150, 50);

        JButton buttonRevoke = new JButton("Revoke Membership");
        buttonRevoke.setBounds(170, 290, 150, 50);

        JButton buttonBack = new JButton("< Back to Main Menu");
        buttonBack.setBounds(170, 360, 150, 50);

        buttonRaise.addActionListener(e -> {
            User user = getUserById(inputUsername.getText().toString());

            f.dispose();
        });

        buttonRevoke.addActionListener(e -> {
            new RevokeMembership();
            f.dispose();
        });

        buttonBack.addActionListener(e -> {
            new MainMenuAdminScreen();
            f.dispose();
        });
        f.add(buttonRaise);
        f.add(buttonRevoke);
        f.add(buttonBack);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}
