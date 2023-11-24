package src.view.admin;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.*;

import src.controller.Controller;

public class RaiseRevokeMembership {
    public RaiseRevokeMembership() {
        raiseRevokeMembership();
    }

    private void raiseRevokeMembership() {
        Controller controller = new Controller();
        JFrame frame = new JFrame();
        frame.setTitle("Revoke dan Raise Membership");
        frame.setSize(400,400);
        frame.setVisible(true);
        JPanel panel = new JPanel();
        panel.setLayout(null);
        
        JLabel mainLabel = new JLabel("Revoke dan Raise Membership");
        mainLabel.setBounds(50, 10, 300, 25);
        mainLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(mainLabel);

        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setBounds(50, 50, 80, 25);
        panel.add(usernameLabel);

        String[] usernameArr = controller.listUser();
        JComboBox<String> usernameList = new JComboBox<>(usernameArr);
        usernameList.setBounds(50, 100, 250, 25);
        panel.add(usernameList);
        
        JTextField searchField = new JTextField(20);
        searchField.setBounds(50, 70, 250, 25);
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterComboBox();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                filterComboBox();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                filterComboBox();
            }
            public void filterComboBox(){
                String searchName = searchField.getText();
                DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
                for (String name : usernameArr) {
                    if (name.toLowerCase().contains(searchName.toLowerCase())) {
                        model.addElement(name);
                    }
                }
                usernameList.setModel(model);
                usernameList.setPopupVisible(true);
            }
        });
        panel.add(searchField);
        


        JButton buttonRevoke = new JButton("Revoke Membership");
        buttonRevoke.setBounds(50, 180, 250, 25);
        panel.add(buttonRevoke);

        JButton buttonRaise = new JButton("Raise Membership");
        buttonRaise.setBounds(50, 220, 250, 25);
        panel.add(buttonRaise);

        JButton backButton = new JButton("Back");
        backButton.setBounds(50, 270, 250, 25);
        panel.add(backButton);
       

        buttonRaise.addActionListener(e -> {
            int confirmation = JOptionPane.showConfirmDialog(buttonRaise, "Apakah anda yakin untuk melakukan perubahan ?", "Confrimation", 0);
            String username = usernameList.getSelectedItem().toString();
            if (username==null) {
                JOptionPane.showMessageDialog(null, "Username Kosong !" );
            }else{
                if(confirmation==0){
                    int result= controller.raiseMembership(username);
                    if (result==1) {
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
            String username = usernameList.getSelectedItem().toString();
            if (username==null) {
                JOptionPane.showMessageDialog(null, "Username Kosong !" );
            }else{
                if(confirmation==0){
                    int result= controller.revokeMembership(username);
                    if (result == 1) {
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
        frame.setLocationRelativeTo(null);
    }
}
