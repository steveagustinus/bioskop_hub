package src.view.admin.manage_fnb;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import src.controller.Controller;
import src.controller.OperationCode;
import src.view.admin.MainMenuScreen;
public class EditFnBScreen {
    public  EditFnBScreen(){
        Controller controller = new Controller();
        JFrame frame = new JFrame();
        frame.setTitle("Edit FnB");
        frame.setSize(400,200);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6,2));
        JLabel namaFnB = new JLabel("Nama-nama Food and Beverages : ");
        JLabel namaFnBlabel = new JLabel("Update nama FnB : ");
        JLabel hargaFnBlabel = new JLabel("Update Harga FnB : ");
        JLabel descriptionFnBlabel = new JLabel("Update Deskripsi FnB : ");
        String [] namaFoodAndBevs = controller.listFNB();
        JComboBox<String> foodsnbevs = new JComboBox<>(namaFoodAndBevs);
        JTextField nama = new JTextField(20);
        JTextField harga = new JTextField(20);;
        JTextArea description= new JTextArea();
        JButton button = new JButton("Edit FnB");

        foodsnbevs.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                nama.removeAll();
                harga.removeAll();
                description.removeAll();
                nama.setText(controller.getFnBbyName(foodsnbevs.getSelectedItem().toString()).getNama());
                harga.setText(String.valueOf(controller.getFnBbyName(foodsnbevs.getSelectedItem().toString()).getHarga()));
                description.setText(controller.getFnBbyName(foodsnbevs.getSelectedItem().toString()).getDescription());
            }

        });

        panel.add(namaFnB);
        panel.add(foodsnbevs);
        //Nama
        panel.add(namaFnBlabel);
        panel.add((nama));

        //Harga
        panel.add(hargaFnBlabel);
        panel.add(harga);

        //Deskripsi
        panel.add(descriptionFnBlabel);
        panel.add(description);

        //Button
        panel.add(new JLabel());
        panel.add(button);

        frame.add(panel);
        frame.setVisible(true);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String oldNameFnB = foodsnbevs.getSelectedItem().toString();
                String [] data = new String[3];
                data[0]=nama.getText();
                data[1]=harga.getText();
                data[2]=description.getText();
                int konfirmasi = controller.editFnB(oldNameFnB,data);
                String status="";
                if(konfirmasi==OperationCode.addFnB.SUCCESS){
                    status="Berhasil!";
                    JOptionPane.showMessageDialog(null,status);
                    frame.dispose();
                    new MainMenuScreen();
                }else if(konfirmasi==OperationCode.EditFnB.EMPTYNAME){
                    status="Nama Kosong!";
                }else if (konfirmasi==OperationCode.EditFnB.EMPTYHARGA){
                    status="Harga Kosong!";
                }else if(konfirmasi==OperationCode.EditFnB.EMPTYDESCRIPTION){
                    status="Deskripsi Kosong!";
                }else if(konfirmasi==OperationCode.EditFnB.ANYEXCEPTION){
                    status="Error!";
                }
                
                 JOptionPane.showMessageDialog(null,status);
                frame.dispose();
            }
        });

    }

}