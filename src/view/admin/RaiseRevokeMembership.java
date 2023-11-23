package src.view.admin;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.*;

import src.controller.Controller;
import src.controller.OperationCode;

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
           String username = inputUsername.getText().toString();
            if (username.equals("")) {
                username=null;
            }
            int checker = controller.isNameExist(username);
            if (username!=null) {
                if(checker==0){
                JOptionPane.showMessageDialog(null,"Username TIDAK di temukan !");      
                }else if (checker ==1) {
                    int confirmation = JOptionPane.showConfirmDialog(buttonRaise, "Apakah anda yakin untuk melakukan perubahan ?", "Confrimation", 0);
                    if (confirmation == 0) {
                        int status = controller.raiseMembership(username);
                    String alert="";
                    if (status == OperationCode.RaiseRevokeMembership.SUCCESS) {
                         alert="Berhasil!";
                    }else if(status == OperationCode.RaiseRevokeMembership.ALREADYMEMBER){
                        alert="Sudah menjadi Member!";
                    }else if (status == OperationCode.RaiseRevokeMembership.ANYEXCEPTION) {
                        alert="Error!";
                    }
                    JOptionPane.showMessageDialog(null,alert);  
                    }else if (confirmation == 1) {
                        
                    }
                    
                }   
            }else{
                JOptionPane.showMessageDialog(null,"Username TIDAK boleh kosong !");
            }
        });

        buttonRevoke.addActionListener(e -> { 
            String username = inputUsername.getText().toString();
            if (username.equals("")) {
                username=null;
            }
            int checker = controller.isNameExist(username);
            if (username!=null) {
                if(checker==0){
                JOptionPane.showMessageDialog(null,"Username TIDAK di temukan !");      
                }else if (checker ==1) {
                    int confirmation = JOptionPane.showConfirmDialog(buttonRaise, "Apakah anda yakin untuk melakukan perubahan ?", "Confrimation", 0);
                    if (confirmation == 0) {
                        int status = controller.revokeMembership(username);
                        String alert="";
                    if (status == OperationCode.RaiseRevokeMembership.SUCCESS) {
                         alert="Berhasil!";
                    }else if(status == OperationCode.RaiseRevokeMembership.ALREADYUSER){
                        alert="Sudah menjadi User biasa!";
                    }else if (status == OperationCode.RaiseRevokeMembership.ANYEXCEPTION) {
                        alert="Error!";
                    }
                    JOptionPane.showMessageDialog(null,alert);  
                    }else if (confirmation == 1) {
                        
                    }
                    
                }   
            }else{
                JOptionPane.showMessageDialog(null,"Username TIDAK boleh kosong !");
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
