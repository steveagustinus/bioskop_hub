package src.view.user;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;

import src.controller.Controller;
import src.controller.UserDataSingleton;
import src.model.button.MyButton;
import src.view.LoginScreen;
import src.view.MainInterface;

public class MainMenuUserScreen implements MainInterface {
    Controller controller = new Controller();

    private JLabel mainlabel;
    private JLabel usernameLabel;
    private JLabel membershipPointLabel;
    private JLabel membershipDateLabel;


    public MainMenuUserScreen() {
        JFrame frame = new JFrame();
        frame.setTitle("Main Menu");
        frame.setSize(720, 480);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setBackground(FRAME_BACKGROUND);
        panel.setLayout(null);

        mainlabel = new JLabel("Welcome " + UserDataSingleton.getInstance().getProfile_name() + " to the Main Menu!");
        mainlabel.setBounds(175, 10, 500, 50);
        panel.add(mainlabel);
        mainlabel.setFont(new Font(FONTFAMILY, Font.BOLD, 24));
        mainlabel.setForeground(TEXT_BACKGROUND);

        usernameLabel = new JLabel("Username : " + UserDataSingleton.getInstance().getUsername());
        usernameLabel.setBounds(10, 70, 500, 50);
        usernameLabel.setForeground(TEXT_BACKGROUND);
        panel.add(usernameLabel);
        
        membershipPointLabel = new JLabel("Membership Point : " + UserDataSingleton.getInstance().getMembership_point());
        membershipPointLabel.setBounds(10, 90, 500, 50);
        membershipPointLabel.setForeground(TEXT_BACKGROUND);
        panel.add(membershipPointLabel);

        membershipDateLabel = new JLabel("Membership Date : " + UserDataSingleton.getInstance().getMembership_expiry_date());
        membershipDateLabel.setBounds(10, 110, 500, 50);
        membershipDateLabel.setForeground(TEXT_BACKGROUND);
        panel.add(membershipDateLabel);

        int isMember = controller.checkMembership(UserDataSingleton.getInstance().getUsername());
        if(isMember == 1){
            membershipPointLabel.setVisible(true);
            membershipDateLabel.setVisible(true);
        }else{
            membershipPointLabel.setVisible(false);
            membershipDateLabel.setVisible(false);
        }

        JButton pesanTiketButton = new JButton("Pesan Tiket");
        pesanTiketButton.setBounds(170, 150, 150, 50);
        pesanTiketButton.setOpaque(true);
        pesanTiketButton.setBackground(BUTTON_BACKGROUND);
        pesanTiketButton.setForeground(BUTTON_FOREGROUND);
        pesanTiketButton.setFocusPainted(false);
        panel.add(pesanTiketButton);
        pesanTiketButton.addActionListener(e -> {
            frame.setVisible(false);
            new PesanTiket(mainFrame);
            frame.setVisible(true);
        });

        JButton pesanFnBButton = new JButton("Pesan FnB");
        pesanFnBButton.setBounds(370, 150, 150, 50);
        pesanFnBButton.setOpaque(true);
        pesanFnBButton.setBackground(BUTTON_BACKGROUND);
        pesanFnBButton.setForeground(BUTTON_FOREGROUND);
        pesanFnBButton.setFocusPainted(false);
        panel.add(pesanFnBButton);
        pesanFnBButton.addActionListener(e -> {
            frame.setVisible(false);
            new PesanFnb();
            updateData();
            frame.setVisible(true);
        });

        JButton joinMembershipButton = new JButton("Membership");
        joinMembershipButton.setBounds(170, 220, 150, 50);
        joinMembershipButton.setOpaque(true);
        joinMembershipButton.setBackground(BUTTON_BACKGROUND);
        joinMembershipButton.setForeground(BUTTON_FOREGROUND);
        joinMembershipButton.setFocusPainted(false);
        panel.add(joinMembershipButton);
        joinMembershipButton.addActionListener(e -> {
            new RegisterMembership(frame);
            updateData();
        });

        JButton transactionHitoryButton = new JButton("Transaction History");
        transactionHitoryButton.setBounds(370, 220, 150, 50);
        transactionHitoryButton.setOpaque(true);
        transactionHitoryButton.setBackground(BUTTON_BACKGROUND);
        transactionHitoryButton.setForeground(BUTTON_FOREGROUND);
        transactionHitoryButton.setFocusPainted(false);
        panel.add(transactionHitoryButton);
        transactionHitoryButton.addActionListener(e -> {
            frame.setVisible(false);
            new TransactionHistoryScreen();
            frame.setVisible(true);
        });

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBounds(275, 330, 150, 50);
        logoutButton.setOpaque(true);
        logoutButton.setBackground(BUTTON_BACKGROUND);
        logoutButton.setForeground(BUTTON_FOREGROUND);
        logoutButton.setFocusPainted(false);
        panel.add(logoutButton);
        logoutButton.addActionListener(e -> {
            new LoginScreen();
            frame.dispose();
        });

        // background color

        MyButton roundButton = new MyButton();
        roundButton.setText(UserDataSingleton.getInstance().getUsername().substring(0, 1).toUpperCase());
        roundButton.setFont(new Font(FONTFAMILY, Font.PLAIN, 18));
        roundButton.setPreferredSize(new Dimension(120, 120));
        roundButton.setBackground(BUTTON_BACKGROUND);
        roundButton.setBounds(620, 20, 50, 50);
        roundButton.setFocusPainted(false);
        roundButton.setBorder(BorderFactory.createEmptyBorder());
        panel.add(roundButton);
        roundButton.addActionListener(e -> {
            frame.setVisible(false);
            new CheckUserProfileScreen(mainFrame);
            updateData();
            frame.setVisible(true);
        });

        frame.add(panel);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    public void updateData() {
        mainlabel.setText("Welcome " + UserDataSingleton.getInstance().getProfile_name() + " to the Main Menu!");
        usernameLabel.setText("Username : " + UserDataSingleton.getInstance().getUsername());
        membershipPointLabel.setText("Membership Point : " + UserDataSingleton.getInstance().getMembership_point());
        membershipDateLabel.setText("Membership Date : " + UserDataSingleton.getInstance().getMembership_expiry_date());

        int isMember = UserDataSingleton.getInstance().getMembership_status();
        if(isMember == 1){
            membershipPointLabel.setVisible(true);
            membershipDateLabel.setVisible(true);
        } else {
            membershipPointLabel.setVisible(false);
            membershipDateLabel.setVisible(false);
        }
    }
}
