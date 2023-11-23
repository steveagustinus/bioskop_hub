package src.view.admin;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.*;
import java.awt.event.ActionListener;

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
        
        buttonRaise.addActionListener(e -> {
            int confirmation = JOptionPane.showConfirmDialog(buttonRaise, "Apakah anda yakin untuk melakukan perubahan ?", "Confrimation", 0);
            String username = inputUsername.getText().toString();
            if (username==null) {
                JOptionPane.showMessageDialog(null, "Username Kosong !" );
            }else{
                if(confirmation==0){
                    boolean result= controller.raiseMembership(username);
                    if (result) {
                        JOptionPane.showMessageDialog(null, "Perubahan telah di lakukan!" );
                    }else{
                        JOptionPane.showMessageDialog(null, "Error!" );
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Perubahan di batalkan !" );    
                }
            }
        });

        buttonRevoke.addActionListener(e -> {
            int confirmation = JOptionPane.showConfirmDialog(buttonRaise, "Apakah anda yakin untuk melakukan perubahan ?", "Confrimation", 0);
            String username = inputUsername.getText().toString();
            if (username==null) {
                JOptionPane.showMessageDialog(null, "Username Kosong !" );
            }else{
                if(confirmation==0){
                    boolean result= controller.revokeMembership(username);
                    if (result) {
                        JOptionPane.showMessageDialog(null, "Perubahan telah di lakukan!" );
                    }else{
                        JOptionPane.showMessageDialog(null, "Error!" );
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Perubahan di batalkan !" );    
                }
            }
        });

        backButton.addActionListener(e ->{
            new MainMenuScreen();
            frame.dispose();
            
        });

        frame.add(panel);
        frame.setVisible(true);
       
    }
}
