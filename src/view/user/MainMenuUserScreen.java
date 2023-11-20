package src.view.user;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;

import src.controller.UserDataSingleton;
import src.model.button.MyButton;
import src.model.button.MyButton;
import src.view.LoginScreen;

public class MainMenuUserScreen {
    UserDataSingleton userData = UserDataSingleton.getInstance();

    public MainMenuUserScreen() {
        JFrame frame = new JFrame();
        frame.setTitle("Main Menu");
        frame.setSize(720, 480);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel mainlabel = new JLabel("Welcome " + userData.getProfile_name() + " to the Main Menu!");
        mainlabel.setBounds(175, 10, 500, 50);
        panel.add(mainlabel);
        mainlabel.setFont(new Font("Arial", Font.BOLD, 24));

        JLabel usernameLabel = new JLabel("Username : " + userData.getUsername());
        usernameLabel.setBounds(10, 70, 500, 50);
        panel.add(usernameLabel);

        JButton pesanTikerButton = new JButton("Pesan Tiket");
        pesanTikerButton.setBounds(170, 150, 150, 50);
        panel.add(pesanTikerButton);
        pesanTikerButton.addActionListener(e -> {
            new PesanTiket();
            frame.dispose();
        });

        JButton pesanFnBButton = new JButton("Pesan FnB");
        pesanFnBButton.setBounds(370, 150, 150, 50);
        panel.add(pesanFnBButton);
        pesanFnBButton.addActionListener(e -> {
            new PesanFnb();
            frame.dispose();
        });

        JButton joinMembershipButton = new JButton("Join Membership");
        joinMembershipButton.setBounds(170, 220, 150, 50);
        panel.add(joinMembershipButton);
        joinMembershipButton.addActionListener(e -> {
            // new JoinMembership();
            frame.dispose();
        });

        JButton transactionHitoryButton = new JButton("Transaction History");
        transactionHitoryButton.setBounds(370, 220, 150, 50);
        panel.add(transactionHitoryButton);
        transactionHitoryButton.addActionListener(e -> {
            new TransactionHistoryScreen();
            frame.dispose();
        });

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBounds(275, 330, 150, 50);
        panel.add(logoutButton);
        logoutButton.addActionListener(e -> {
            new LoginScreen();
            frame.dispose();
        });

        // background color
        float[] hsb = Color.RGBtoHSB(238, 238, 238, null);
        Color backgroundColor = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);

        MyButton roundButton = new MyButton();
        roundButton.setText(userData.getUsername().substring(0, 1).toUpperCase());
        roundButton.setFont(new Font("Arial", Font.PLAIN, 18));
        roundButton.setPreferredSize(new Dimension(120, 120));
        roundButton.setBackground(backgroundColor);
        roundButton.setBounds(620, 20, 50, 50);
        panel.add(roundButton);
        roundButton.addActionListener(e -> {
            new CheckUserProfileScreen();
            frame.dispose();
        });

        frame.add(panel);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    // test area
    public static void main(String[] args) {
        new MainMenuUserScreen();
    }
}