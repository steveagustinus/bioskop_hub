package src.view.admin.manage_fnb;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import src.controller.Controller;
import src.controller.OperationCode;
import src.model.FnB;

public class EditFnBScreen {
    public EditFnBScreen(Window owner) {
        Controller controller = new Controller();
        JDialog frame = new JDialog(owner);
        frame.setTitle("Edit and Delete FnB");
        frame.setSize(400, 600);
        frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel mainLabel = new JLabel("Edit and Delete Food and Beverages");
        mainLabel.setBounds(10, 10, 350, 30);
        mainLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(mainLabel);

        JLabel namaFnB = new JLabel("Nama-nama Food and Beverages : ");
        namaFnB.setBounds(10, 50, 350, 30);
        panel.add(namaFnB);

        String[] namaFoodAndBevs = controller.listFNB();
        JComboBox<String> foodsnbevs = new JComboBox<>(namaFoodAndBevs);
        foodsnbevs.setBounds(10, 120, 350, 30);
        panel.add(foodsnbevs);
        // JTextField searchFnB = new JTextField(20);
        // searchFnB.setBounds(10, 80, 350, 30);
        // panel.add(searchFnB);
        // searchFnB.getDocument().addDocumentListener(new DocumentListener() {
        //     @Override
        //     public void insertUpdate(DocumentEvent e) {
        //         filterComboBox();
        //     }

        //     @Override
        //     public void removeUpdate(DocumentEvent e) {
        //         filterComboBox();
        //     }

        //     @Override
        //     public void changedUpdate(DocumentEvent e) {
        //         filterComboBox();
        //     }

        //     private void filterComboBox() {
        //         String filter = searchFnB.getText();
        //         DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) foodsnbevs.getModel();
        //         model.removeAllElements();
        //         if (!filter.isEmpty()) {
        //             for (String s : namaFoodAndBevs) {
        //                 if (s.toLowerCase().contains(filter.toLowerCase())) {
        //                     model.addElement(s);
        //                 }
        //             }
        //         } else { 
        //             for (String s : namaFoodAndBevs) {
        //                 model.addElement(s);
        //             }
        //         }
        //     }
        // });

        JRadioButton edit = new JRadioButton("Edit");
        edit.setBounds(10, 160, 100, 30);
        panel.add(edit);
        JRadioButton delete = new JRadioButton("Delete");
        delete.setBounds(110, 160, 100, 30);
        delete.setSelected(true);
        panel.add(delete);
        ButtonGroup group = new ButtonGroup();
        group.add(edit);
        group.add(delete);

        JLabel namaFnBlabel = new JLabel("Nama : ");
        namaFnBlabel.setBounds(10, 200, 100, 30);
        namaFnBlabel.setVisible(false);
        panel.add(namaFnBlabel);

        JTextField nama = new JTextField(20);
        nama.setBounds(120, 200, 200, 30);
        nama.setVisible(false);
        panel.add(nama);

        JLabel hargaFnBlabel = new JLabel("Harga : ");
        hargaFnBlabel.setBounds(10, 240, 100, 30);
        hargaFnBlabel.setVisible(false);
        panel.add(hargaFnBlabel);

        JTextField harga = new JTextField(20);
        harga.setBounds(120, 240, 200, 30);
        harga.setVisible(false);
        panel.add(harga);

        JLabel descriptionFnBlabel = new JLabel("Deskripsi : ");
        descriptionFnBlabel.setBounds(10, 280, 100, 30);
        descriptionFnBlabel.setVisible(false);
        panel.add(descriptionFnBlabel);

        JTextArea description = new JTextArea();
        description.setBounds(120, 280, 200, 70);
        description.setVisible(false);
        panel.add(description);

        JButton backButton = new JButton("Back");
        backButton.setBounds(10, 350, 100, 30);
        panel.add(backButton);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(220, 350, 100, 30);
        panel.add(submitButton);

        foodsnbevs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                FnB fnb = controller.getFnBbyName(foodsnbevs.getSelectedItem().toString());
                // searchFnB.setText(fnb.getNama());
                nama.setText(fnb.getNama());
                harga.setText(String.valueOf(fnb.getHarga()));
                description.setText(fnb.getDescription());
            }
        });

        edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (edit.isSelected()) {
                    namaFnBlabel.setVisible(true);
                    hargaFnBlabel.setVisible(true);
                    descriptionFnBlabel.setVisible(true);
                    nama.setVisible(true);
                    harga.setVisible(true);
                    description.setVisible(true);
                } else {
                    namaFnBlabel.setVisible(false);
                    hargaFnBlabel.setVisible(false);
                    descriptionFnBlabel.setVisible(false);
                    nama.setVisible(false);
                    harga.setVisible(false);
                    description.setVisible(false);
                }
            }
        });
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (delete.isSelected()) {
                    namaFnBlabel.setVisible(false);
                    hargaFnBlabel.setVisible(false);
                    descriptionFnBlabel.setVisible(false);
                    nama.setVisible(false);
                    harga.setVisible(false);
                    description.setVisible(false);
                } else {
                    namaFnBlabel.setVisible(true);
                    hargaFnBlabel.setVisible(true);
                    descriptionFnBlabel.setVisible(true);
                    nama.setVisible(true);
                    harga.setVisible(true);
                    description.setVisible(true);
                }
            }
        });
        submitButton.addActionListener(e -> {
            if(delete.isSelected()){
                int statusCode = controller.deleteFnB(foodsnbevs.getSelectedItem().toString());
                if(statusCode==OperationCode.EditFnB.SUCCESS){
                    JOptionPane.showMessageDialog(null,"Berhasil!");

                    frame.setVisible(false);
                    frame.dispose();
                }else if(statusCode==OperationCode.EditFnB.EMPTYNAME){
                    JOptionPane.showMessageDialog(null,"Terjadi error!");
                }

            }else if(edit.isSelected()){
                String[] tempFnB = { nama.getText() , harga.getText() , description.getText() };

                int konfirmasi = controller.editFnB(foodsnbevs.getSelectedItem().toString(), tempFnB);
                String status="";
                if(konfirmasi==OperationCode.EditFnB.SUCCESS){
                    status="Berhasil!";
                    JOptionPane.showMessageDialog(null,status);

                    frame.setVisible(false);
                    frame.dispose();
                }else{
                    if(konfirmasi==OperationCode.EditFnB.EMPTYNAME){
                        status="Nama Kosong!";
                    }else if (konfirmasi==OperationCode.EditFnB.EMPTYHARGA){
                        status="Harga Kosong!";
                    }else if(konfirmasi==OperationCode.EditFnB.ANYEXCEPTION){
                        status="Error!";
                    }else if(konfirmasi==OperationCode.EditFnB.INVALIDHARGA){
                        status="Input harga tidak valid!";
                    }
                    JOptionPane.showMessageDialog(null,status);
                }
            }
        });

        frame.add(panel);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

    }
}