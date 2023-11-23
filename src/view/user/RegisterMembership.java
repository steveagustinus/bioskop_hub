package src.view.user;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import src.controller.Controller;
import src.controller.UserDataSingleton;

public class RegisterMembership {
    public RegisterMembership(){
        regisMembership();
    }

    public static void main(String[] args) {
        new RegisterMembership();
    }

    private void regisMembership(){
        Controller controller = new Controller();
        boolean checkStatus = controller.checkMembership(UserDataSingleton.getInstance().getUsername());

        if(checkStatus == true){
            JOptionPane.showMessageDialog(null, "Anda sudah terdaftar menjadi member, kembali ke menu", "", JOptionPane.INFORMATION_MESSAGE);
            new MainMenuUserScreen();
        } else {
            JFrame f = new JFrame();
            f.setTitle("Menu Register");
            f.setSize(720, 480);
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JButton buttonRegis = new JButton("Register Membership");
            buttonRegis.setBounds(50, 200, 250, 50);

            JButton buttonBack = new JButton("< Back to Main Menu");
            buttonBack.setBounds(400, 200, 250, 50);
            
            buttonRegis.addActionListener(e -> {
                controller.registerMembership(UserDataSingleton.getInstance().getUsername(), UserDataSingleton.getInstance().getPassword(), UserDataSingleton.getInstance().getPassword(), UserDataSingleton.getInstance().getEmail(), UserDataSingleton.getInstance().getPhone_no(), UserDataSingleton.getInstance().getAddress(), 0);
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
            f.setLayout(null);
            f.setLocationRelativeTo(null);
            f.setVisible(true);
            
        }
    }
}
