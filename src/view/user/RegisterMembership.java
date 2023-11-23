package src.view.user;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import src.controller.Controller;

public class RegisterMembership {
    public RegisterMembership(){
        regisMembership();
    }

    public static void main(String[] args) {
        new RegisterMembership();
    }

    private void regisMembership(){
        Controller controller = new Controller();
        boolean checkStatus = false; //controller.checkMembership(UserDataSingleton.getInstance().getUsername());

        if(checkStatus == true){
            JOptionPane.showMessageDialog(null, "Anda sudah terdaftar menjadi member, kembali ke menu", "", JOptionPane.INFORMATION_MESSAGE);
            new MainMenuUserScreen();
        } else {
            JFrame f = new JFrame();
            f.setTitle("Menu Register");
            f.setSize(720, 480);
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            

            JButton buttonRegis = new JButton("Register Membership");
            buttonRegis.setBounds(170, 220, 150, 50);

            JButton buttonBack = new JButton("< Back to Main Menu");
            buttonBack.setBounds(170, 290, 150, 50);
            
            buttonRegis.addActionListener(e -> {
                // PANGGIL REGISTER MEMBERSHIP DISINI
                JOptionPane.showMessageDialog(null, "Anda sudah terdaftar menjadi member, kembali ke menu", "", JOptionPane.INFORMATION_MESSAGE);
                f.dispose();
                new MainMenuUserScreen();
            });

            buttonBack.addActionListener(e -> {
                new MainMenuUserScreen();
                f.dispose();
            });
            f.add(buttonRegis);
            f.add(buttonBack);
            f.setLocationRelativeTo(null);
            f.setVisible(true);
            
        }
    }
}
