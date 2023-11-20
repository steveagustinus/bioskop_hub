package src.view.user;

import javax.swing.JButton;
import javax.swing.JFrame;

import src.controller.Controller;
import src.controller.UserDataSingleton;

public class RegisterMembership {
    public RegisterMembership(){
        regisMembership();
    }

    private void regisMembership(){
        UserDataSingleton userData = UserDataSingleton.getInstance();
        Controller controller = new Controller();
        JFrame f = new JFrame();
        
        f.setTitle("Main Menu");
        f.setSize(720, 480);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);

        JButton buttonRegis = new JButton("Register Membership");
        buttonRegis.setBounds(170, 220, 150, 50);
        f.add(buttonRegis);
        buttonRegis.addActionListener(e -> {
            controller.registerMembership(UserDataSingleton.getInstance().getUsername(), UserDataSingleton.getInstance().getPassword(), UserDataSingleton.getInstance().getPassword(), UserDataSingleton.getInstance().getEmail(), UserDataSingleton.getInstance().getPhone_no(), UserDataSingleton.getInstance().getAddress(), 0);
            f.dispose();
        });

        JButton buttonBack = new JButton("< Back to Main Menu");
        buttonBack.setBounds(170, 290, 150, 50);
        f.add(buttonBack);
        buttonBack.addActionListener(e -> {
            new MainMenuUser();
            f.dispose();
        });


    }
}
