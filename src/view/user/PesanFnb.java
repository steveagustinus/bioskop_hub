package src.view.user;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import src.controller.Controller;

public class PesanFnb {
    public PesanFnb(){
        viewPesanFnb();
    }

    private void viewPesanFnb(){
        Controller controller = new Controller();
        JFrame f = new JFrame("Pesan FNB");
        f.setSize(600, 900);
        JLabel labelKota = new JLabel("Kota : ");
        String kota[] = controller.listKota();
        JComboBox<String> boxKota = new JComboBox<>(kota);
        labelKota.setBounds(10, 15, 200, 30);
        boxKota.setBounds(170, 15, 200, 30);

        JLabel labelCinema = new JLabel("Cabang : ");
        JComboBox<String> boxCinema = new JComboBox<>();
        final String[] tempCinema = {""};  // Using an array to make it effectively fina

        boxCinema.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Update the combo box model based on user's selection
                String[] idCinema = controller.listCinema(boxCinema.getSelectedItem().toString());
                boxCinema.setModel(new DefaultComboBoxModel<>(idCinema));

                // Update the tempCinema variable after the user has interacted
                tempCinema[0] = (String) boxCinema.getSelectedItem();
            }
        });

        labelCinema.setBounds(10, 60, 200, 30);
        boxCinema.setBounds(170, 60, 200, 30);


        JLabel studio =  new JLabel("Studio : ");
        JComboBox<String> boxStudio = new JComboBox<>();
        boxCinema.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] idStudio = controller.listStudio(boxCinema.getSelectedItem().toString());
                boxStudio.setModel(new DefaultComboBoxModel<>(idStudio));
            }
        });        
        studio.setBounds(10, 105, 200, 30);
        boxStudio.setBounds(170, 105, 200, 30);

        JLabel menu =  new JLabel("Pilih Menu : ");
        JComboBox<String> boxMenu = new JComboBox<>(controller.listFNB());
        String pilihanMenu = boxMenu.getSelectedItem().toString();
        menu.setBounds(10, 150, 200, 30);
        boxMenu.setBounds(170, 150, 200, 30);

        JLabel labelHarga = new JLabel("Harga Menu: ");
        JLabel hargaPerFnb = new JLabel(""+controller.hargaPerFnb(pilihanMenu));
        long harga  = Long.parseLong(hargaPerFnb.getText());
        labelHarga.setBounds(10, 195, 200, 30);
        hargaPerFnb.setBounds(170, 195, 200, 30);

        JLabel quantityLabel = new JLabel("Enter Quantity:");
        JTextField quantityField = new JTextField();
        int quantity=0;
        try {
            quantity = Integer.parseInt(quantityField.getText());
        } catch (NumberFormatException ex) {
            // Handle the case where parsing fails (e.g., non-integer input)
            ex.printStackTrace(); // or log the error
        }
        quantityLabel.setBounds(10, 240, 200, 30);
        quantityField.setBounds(170, 240, 200, 30);

        JLabel totalTunaiLabel = new JLabel("Total tunai: ");
        JLabel totalTunaiHasil = new JLabel(" "+controller.totalHasilTransaksiFnb(harga, quantity));
        totalTunaiLabel.setBounds(10, 285, 200, 30);
        totalTunaiHasil.setBounds(170, 285, 200, 30);

        JButton buttonToMainMenu = new JButton("Back to Main Menu");
        buttonToMainMenu.setBounds(10, 640, 200, 30);
        buttonToMainMenu.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
                // new MainMenuUser();
                f.dispose();
            }
        });
        
        try {
            quantity = Integer.parseInt(quantityField.getText());
        } catch (NumberFormatException ex) {
            ex.printStackTrace();// or log the error
        }

        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(250, 640, 200, 30);
        submitButton.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
                int quantity = 0;
                try {
                    quantity = Integer.parseInt(quantityField.getText());
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                }
                controller.insertTransaksiFnb(pilihanMenu,quantity, harga,tempCinema[0]);
                f.dispose();
            }
        });

        f.add(labelKota);
        f.add(boxKota);
        f.add(labelCinema);
        f.add(boxCinema);
        f.add(studio);
        f.add(boxStudio);
        f.add(menu);
        f.add(boxMenu);
        f.add(labelHarga);
        f.add(hargaPerFnb);
        f.add(quantityLabel);
        f.add(quantityField);
        f.add(totalTunaiLabel);
        f.add(totalTunaiHasil);
        f.add(buttonToMainMenu);
        f.add(submitButton);


        f.setLayout(null);
        f.setVisible(true);
    }
}