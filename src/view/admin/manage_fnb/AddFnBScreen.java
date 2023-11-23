package src.view.admin.manage_fnb;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import src.controller.Controller;
import src.controller.OperationCode;
import src.view.admin.MainMenuScreen;

public class AddFnBScreen {
    public AddFnBScreen(){
        Controller controller = new Controller();
        JFrame frame = new JFrame();
        frame.setTitle("Add FnB");
        frame.setSize(400,200);
        JTextField namaFnB = new JTextField(40);
        JTextField harga = new JTextField(40);;
        JTextArea description= new JTextArea();
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4,2));
        JLabel namaFnBlabel = new JLabel("Nama FnB : ");
        JLabel hargaFnBlabel = new JLabel("Harga FnB : ");
        JLabel descriptionFnBlabel = new JLabel("Deskripsi FnB : ");
        JButton submitButton = new JButton("Submit");
        submitButton.setSize(100,50);

        panel.add(namaFnBlabel);
        panel.add(namaFnB);

        panel.add(hargaFnBlabel);
        panel.add(harga);

        panel.add(descriptionFnBlabel);
        panel.add(description);

        panel.add(new JLabel());
        panel.add(submitButton);

        frame.add(panel);
        frame.setVisible(true);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String [] data = new String[3];
                data[0]=namaFnB.getText();
                data[1]=harga.getText();
                data[2]=description.getText();
                int konfirmasi = controller.addFnB(data);
                String status="";
                if(konfirmasi==OperationCode.addFnB.SUCCESS){
                    status="Berhasil!";
                    JOptionPane.showMessageDialog(null,status);
                    frame.dispose();
                    new MainMenuScreen();
                }else if(konfirmasi==OperationCode.addFnB.EMPTYNAME){
                    status="Nama Kosong!";
                }else if (konfirmasi==OperationCode.addFnB.EMPTYHARGA){
                    status="Harga Kosong!";
                }else if(konfirmasi==OperationCode.addFnB.ANYEXCEPTION){
                    status="Error!";
                }
                 JOptionPane.showMessageDialog(null,status);
                
                
            }
        });

    }
}