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
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400,400);
        // JTextField namaFnB = new JTextField(40);
        // JTextField harga = new JTextField(40);;
        // JTextArea description= new JTextArea();
        // JPanel panel = new JPanel();
        // panel.setLayout(new GridLayout(4,2));
        // JLabel namaFnBlabel = new JLabel("Nama FnB : ");
        // JLabel hargaFnBlabel = new JLabel("Harga FnB : ");
        // JLabel descriptionFnBlabel = new JLabel("Deskripsi FnB : ");
        // JButton submitButton = new JButton("Submit");
        // submitButton.setSize(100,50);

        // panel.add(namaFnBlabel);
        // panel.add(namaFnB);

        // panel.add(hargaFnBlabel);
        // panel.add(harga);

        // panel.add(descriptionFnBlabel);
        // panel.add(description);
        
        // panel.add(new JLabel());
        // panel.add(submitButton);
        
        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel mainLabel = new JLabel("Add Food and Beverages");
        mainLabel.setBounds(70, 10, 350, 30);
        mainLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(mainLabel);


        JLabel namaFnBlabel = new JLabel("Nama FnB : ");
        namaFnBlabel.setBounds(10, 50, 100, 30);
        panel.add(namaFnBlabel);

        JTextField namaFnB = new JTextField(40);
        namaFnB.setBounds(120, 50, 200, 30);
        namaFnB.setText("Enter FnB Name");
        namaFnB.setForeground(Color.GRAY);
        panel.add(namaFnB);
        namaFnB.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (namaFnB.getText().equals("Enter FnB Name")) {
                    namaFnB.setText("");
                    namaFnB.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (namaFnB.getText().equals("")) {
                    namaFnB.setText("Enter FnB Name");
                    namaFnB.setForeground(Color.GRAY);
                }
            }
        });

        JLabel hargaFnBlabel = new JLabel("Harga FnB : ");
        hargaFnBlabel.setBounds(10, 100, 100, 30);
        panel.add(hargaFnBlabel);

        JTextField harga = new JTextField(40);
        harga.setBounds(120, 100, 200, 30);
        harga.setText("Enter FnB Price");
        harga.setForeground(Color.GRAY);
        panel.add(harga);
        harga.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (harga.getText().equals("Enter FnB Price")) {
                    harga.setText("");
                    harga.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (harga.getText().equals("")) {
                    harga.setText("Enter FnB Price");
                    harga.setForeground(Color.GRAY);
                }
            }
        });

        JLabel descriptionFnBlabel = new JLabel("Deskripsi FnB : ");
        descriptionFnBlabel.setBounds(10, 150, 100, 30);
        panel.add(descriptionFnBlabel);

        JTextArea description= new JTextArea();
        description.setBounds(120, 150, 200, 100);
        description.setText("Enter FnB Description");
        description.setForeground(Color.GRAY);
        panel.add(description);
        description.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (description.getText().equals("Enter FnB Description")) {
                    description.setText("");
                    description.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (description.getText().equals("")) {
                    description.setText("Enter FnB Description");
                    description.setForeground(Color.GRAY);
                }
            }
        });
        JButton backButton = new JButton("Back");
        backButton.setBounds(30, 270, 100, 30);
        panel.add(backButton);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                // new ManageFnBScreen();
            }
        });

        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(200, 270, 100, 30);
        panel.add(submitButton);
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String [] data = new String[3];
                data[0]=namaFnB.getText();
                data[1]=harga.getText();
                data[2]=description.getText();
                int konfirmasi = controller.addFnB(data);
                String status="";
                if(konfirmasi==OperationCode.AddFnB.SUCCESS){
                    status="Berhasil!";
                    JOptionPane.showMessageDialog(null,status);
                    frame.dispose();
                    new MainMenuScreen();
                }else if(konfirmasi==OperationCode.AddFnB.EMPTYNAME){
                    status="Nama Kosong!";
                }else if (konfirmasi==OperationCode.AddFnB.EMPTYHARGA){
                    status="Harga Kosong!";
                }else if(konfirmasi==OperationCode.AddFnB.ANYEXCEPTION){
                    status="Error!";
                }
                 JOptionPane.showMessageDialog(null,status);
                
                
            }
        });


        frame.add(panel);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
    public static void main(String[] args) {
        new AddFnBScreen();
    }
}