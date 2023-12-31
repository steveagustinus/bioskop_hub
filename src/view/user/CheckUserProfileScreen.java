package src.view.user;

import java.awt.Font;
import java.awt.Window;
import java.awt.Dialog.ModalityType;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import src.controller.UserDataSingleton;

public class CheckUserProfileScreen {
    public CheckUserProfileScreen(Window owner){
        JDialog frame = new JDialog(owner);

        frame.setTitle("Check User Profile");
        frame.setModalityType(ModalityType.DOCUMENT_MODAL);
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(owner);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        
        JLabel usernameLabel = new JLabel("Username : ");
        usernameLabel.setBounds(10, 50, 100, 25);

        JLabel mainlabel = new JLabel("Check Profile Menu");
        mainlabel.setBounds(125, 10, 500, 50);
        panel.add(mainlabel);
        mainlabel.setFont(new Font("Arial", Font.BOLD, 24));

        JLabel username = new JLabel(UserDataSingleton.getInstance().getUsername());
        username.setBounds(100, 50, 165, 25);

        JLabel passwordLabel = new JLabel("Password : ");
        passwordLabel.setBounds(10, 80, 100, 25);

        JLabel password = new JLabel(UserDataSingleton.getInstance().getPassword());
        password.setBounds(100, 80, 165, 25);

        JLabel profileNameLabel = new JLabel("Profile Name : ");
        profileNameLabel.setBounds(10, 110, 100, 25);

        JLabel profileName = new JLabel(UserDataSingleton.getInstance().getProfile_name());
        profileName.setBounds(100, 110, 165, 25);

        JLabel emailLabel = new JLabel("Email : ");
        emailLabel.setBounds(10, 140, 100, 25);

        JLabel email = new JLabel(UserDataSingleton.getInstance().getEmail());
        email.setBounds(100, 140, 165, 25);

        JLabel phoneNoLabel = new JLabel("Phone No : ");
        phoneNoLabel.setBounds(10,170 , 100, 25);

        JLabel phoneNo = new JLabel(UserDataSingleton.getInstance().getPhone_no());
        phoneNo.setBounds(100, 170, 165, 25);

        JLabel addressLabel = new JLabel("Address : ");
        addressLabel.setBounds(10, 200, 100, 25);

        JLabel address = new JLabel(UserDataSingleton.getInstance().getAddress());
        address.setBounds(100, 200, 165, 25);

        int statusMembership = UserDataSingleton.getInstance().getMembership_status();
        String statusMembershipString = "";
        if(statusMembership == 0){
            statusMembershipString = "Not Member";
        }
        else{
            statusMembershipString = "Membership";
        }
        JLabel membershipStatus = new JLabel("Membership Status : " + statusMembershipString);
        membershipStatus.setBounds(10, 230, 300, 25);
        
        JButton backButton = new JButton("Back");
        backButton.setBounds(10, 280, 80, 25);

        backButton.addActionListener(e -> {
            frame.dispose();
        });

        JButton editButton = new JButton("Edit");
        editButton.setBounds(200, 280, 80, 25);

        editButton.addActionListener(e -> {
            frame.setVisible(false);
            new EditUserProfileScreen(owner);
            frame.setVisible(false);
            
        });

        panel.add(usernameLabel);
        panel.add(username);
        panel.add(passwordLabel);
        panel.add(password);
        panel.add(profileNameLabel);
        panel.add(profileName);
        panel.add(emailLabel);
        panel.add(email);
        panel.add(phoneNoLabel);
        panel.add(phoneNo);
        panel.add(addressLabel);
        panel.add(address);
        panel.add(membershipStatus);
        panel.add(backButton);
        panel.add(editButton);
        frame.add(panel);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
}
