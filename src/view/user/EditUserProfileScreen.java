package src.view.user;

import java.awt.Font;
import java.awt.Window;
import java.awt.Dialog.ModalityType;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import src.controller.Controller;
import src.controller.UserDataSingleton;

public class EditUserProfileScreen {
    public EditUserProfileScreen(Window owner) {
        JDialog frame = new JDialog(owner);
        frame.setTitle("Edit User Profile");
        frame.setModalityType(ModalityType.DOCUMENT_MODAL);
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(frame);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel mainlabel = new JLabel("Edit Profile Menu");
        mainlabel.setBounds(175, 10, 500, 50);
        panel.add(mainlabel);
        mainlabel.setFont(new Font("Arial", Font.BOLD, 24));

        JLabel usernameLabel = new JLabel("Username : ");
        usernameLabel.setBounds(10, 50, 100, 25);

        JTextField username = new JTextField(UserDataSingleton.getInstance().getUsername());
        username.setBounds(150, 50, 165, 25);

        JLabel oldPasswordLabel = new JLabel("Password : ");
        oldPasswordLabel.setBounds(10, 80, 100, 25);

        JPasswordField oldPassword = new JPasswordField("");
        oldPassword.setBounds(150, 80, 165, 25);

        JLabel newPasswordLabel = new JLabel("New Password : ");
        newPasswordLabel.setBounds(10, 110, 100, 25);

        JPasswordField newPassword = new JPasswordField("");
        newPassword.setBounds(150, 110, 165, 25);

        JLabel profileNameLabel = new JLabel("Profile Name : ");
        profileNameLabel.setBounds(10, 140, 100, 25);

        JTextField profileName = new JTextField(UserDataSingleton.getInstance().getProfile_name());
        profileName.setBounds(150, 140, 165, 25);

        JLabel emailLabel = new JLabel("Email : ");
        emailLabel.setBounds(10, 170, 100, 25);

        JTextField email = new JTextField(UserDataSingleton.getInstance().getEmail());
        email.setBounds(150, 170, 165, 25);

        JLabel phoneNoLabel = new JLabel("Phone No : ");
        phoneNoLabel.setBounds(10, 200, 100, 25);

        JTextField phoneNo = new JTextField(UserDataSingleton.getInstance().getPhone_no());
        phoneNo.setBounds(150, 200, 165, 25);

        JLabel addressLabel = new JLabel("Address : ");
        addressLabel.setBounds(10, 230, 100, 25);

        JTextField address = new JTextField(UserDataSingleton.getInstance().getAddress());
        address.setBounds(150, 230, 165, 25);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBounds(10, 280, 80, 25);

        cancelButton.addActionListener(e -> {
            frame.dispose();
        });

        JButton saveButton = new JButton("Save");
        saveButton.setBounds(150, 280, 80, 25);

        saveButton.addActionListener(e -> {
            String usernameText = username.getText();
            char[] oldPasswordChars = oldPassword.getPassword();
            char[] newPasswordChars = newPassword.getPassword();
            String oldPasswordText = new String(oldPasswordChars);
            String newPasswordText = new String(newPasswordChars);
            String profileNameText = profileName.getText();
            String emailText = email.getText();
            String phoneNoText = phoneNo.getText();
            String addressText = address.getText();
            int user = new Controller().editProfile(usernameText, oldPasswordText, newPasswordText, profileNameText,
                    emailText, phoneNoText, addressText);
            if (user == -1) {
                JOptionPane.showMessageDialog(null, "Username tidak boleh kosong!");
            } else if (user == -2) {
                JOptionPane.showMessageDialog(null, "Password tidak boleh kosong!");
            } else if (user == -3) {
                JOptionPane.showMessageDialog(null, "Email tidak boleh kosong!");
            } else if (user == -4) {
                JOptionPane.showMessageDialog(null, "Phone Number tidak boleh kosong!");
            } else if (user == -5) {
                JOptionPane.showMessageDialog(null, "Address tidak boleh kosong!");
            } else if (user == 0) {
                JOptionPane.showMessageDialog(null, "Password lama salah!", "Password salah!",
                        JOptionPane.ERROR_MESSAGE);
            } else if (user == 1) {
                JOptionPane.showMessageDialog(null,
                        "Update " + UserDataSingleton.getInstance().getUsername() + " successful!");
                frame.dispose();
            }
        });

        panel.add(usernameLabel);
        panel.add(username);
        panel.add(oldPasswordLabel);
        panel.add(oldPassword);
        panel.add(newPasswordLabel);
        panel.add(newPassword);
        panel.add(profileNameLabel);
        panel.add(profileName);
        panel.add(emailLabel);
        panel.add(email);
        panel.add(phoneNoLabel);
        panel.add(phoneNo);
        panel.add(addressLabel);
        panel.add(address);
        panel.add(cancelButton);
        panel.add(saveButton);

        frame.add(panel);

        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
}
