package src.view.user;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import src.controller.Controller;
import src.controller.UserDataSingleton;

public class PesanFnb {
    public PesanFnb(){
        viewPesanFnb();
    }

    public static void main(String[] args) {
        new PesanFnb();
    }

    public int status;
    public String response;
    private void viewPesanFnb(){
        Controller controller = new Controller();

        JFrame f = new JFrame("Pesan FNB");
        f.setLayout(null);
        f.setSize(500, 400);

        JLabel labelKota = new JLabel("Kota : ");
        labelKota.setBounds(10, 15, 200, 30);

        JComboBox<String> boxKota = new JComboBox<>(controller.listKota());
        boxKota.setBounds(170, 15, 200, 30);

        JLabel labelCinema = new JLabel("Cinema : ");
        labelCinema.setBounds(10, 60, 200, 30);
        
        JComboBox<String> boxCinema = new JComboBox<>(controller.listCinema(boxKota.getSelectedItem().toString()));
        boxCinema.setBounds(170, 60, 200, 30);

        JLabel menu =  new JLabel("Pilih Menu : ");
        menu.setBounds(10, 105, 200, 30);

        JComboBox<String> boxMenu = new JComboBox<>(controller.listFNB());
        boxMenu.setBounds(170, 105, 200, 30);

        JLabel labelHarga = new JLabel("Harga Menu: ");
        labelHarga.setBounds(10, 150, 200, 30);

        JLabel hargaPerFnb = new JLabel();
        hargaPerFnb.setText(controller.hargaPerFnb((String) boxMenu.getSelectedItem()));
        hargaPerFnb.setBounds(170, 150, 200, 30);

        JLabel quantityLabel = new JLabel("Jumlah Barang:");
        quantityLabel.setBounds(10, 195, 200, 30);

        JTextField quantityField = new JTextField();
        quantityField.setBounds(170, 195, 200, 30);

        JLabel totalTunaiLabel = new JLabel("Total tunai: ");
        totalTunaiLabel.setBounds(10, 240, 200, 30);

        JLabel totalTunaiHasil = new JLabel();
        totalTunaiHasil.setBounds(170, 240, 200, 30);

        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(250, 320, 200, 30);

        JButton buttonToMainMenu = new JButton("Back to Main Menu");
        buttonToMainMenu.setBounds(10, 320, 200, 30);

        JLabel labelTogglePoin = new JLabel("Pakai 10 poin (?):");
        labelTogglePoin.setBounds(10, 272, 200, 30);
        JLabel labelBenefit = new JLabel("untuk dapatkan diskon 10%");
        labelBenefit.setBounds(10, 288, 200, 30);

        JCheckBox checkBoxDiskon = new JCheckBox();
        checkBoxDiskon.setBounds(190, 280, 200, 30);
        
        totalTunaiHasil.setText(controller.totalHasilTransaksiFnb(
            hargaPerFnb.getText(), quantityField.getText()
        ));

        boxKota.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent arg0) {
                boxCinema.removeAllItems();
                String[] listCinema = controller.listCinema((String) boxKota.getSelectedItem());
                for (String cinema : listCinema) {
                    boxCinema.addItem(cinema);
                }
            }

        });

        boxMenu.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                hargaPerFnb.setText(controller.hargaPerFnb(boxMenu.getSelectedItem().toString()));
            }

        });

        quantityField.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void changedUpdate(DocumentEvent e) {
                totalTunaiHasil.setText(controller.totalHasilTransaksiFnb(
                    hargaPerFnb.getText(), quantityField.getText()
                ));
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                totalTunaiHasil.setText(controller.totalHasilTransaksiFnb(
                    hargaPerFnb.getText(), quantityField.getText()
                ));
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                totalTunaiHasil.setText(controller.totalHasilTransaksiFnb(
                    hargaPerFnb.getText(), quantityField.getText()
                ));
            }
            
        });
        
        checkBoxDiskon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean isSelected = checkBoxDiskon.isSelected();
                status = isSelected ? 1 : 0;
            }
        });

        buttonToMainMenu.addActionListener(e -> {
            f.dispose();
        });

        submitButton.addActionListener(new ActionListener() {
            @Override
                public void actionPerformed(ActionEvent e) {
                    if (Integer.parseInt(quantityField.getText()) == 0) {
                        JOptionPane.showMessageDialog(null, "Jumlah tidak boleh kosong", "Peringatan", JOptionPane.WARNING_MESSAGE);
                    } else {
                        int hargaBaru = 0;
                        String confirmationText = "Kota : "+boxKota.getSelectedItem().toString()+
                                                "\n Cinema : "+boxCinema.getSelectedItem().toString()+
                                                "\n Menu : "+boxMenu.getSelectedItem().toString()+
                                                "\n Harga Menu : "+controller.hargaPerFnb((String) boxMenu.getSelectedItem())+
                                                "\n Jumlah barang : "+quantityField.getText();
                        if(status == 1){
                            if(controller.checkSufficientPoint(UserDataSingleton.getInstance().getMembership_point(), 10)==true){
                                hargaBaru = Integer.parseInt(hargaPerFnb.getText()) / 100 * 90 ;
                                confirmationText+="\n Pakai 10 poin membership : Ya"+
                                                    "\n Total tunai : "+hargaBaru;
                                int input = JOptionPane.showConfirmDialog(null, confirmationText, "Konfirmasi Pesanan FNB",JOptionPane.YES_NO_CANCEL_OPTION);
                                if (input == 0) {
                                    response = controller.insertTransaksiFnb(boxMenu.getSelectedItem().toString(), Integer.parseInt(quantityField.getText()), (String) boxCinema.getSelectedItem(), UserDataSingleton.getInstance().getId());
                                    JOptionPane.showMessageDialog(null, response, "Terima kasih", JOptionPane.INFORMATION_MESSAGE);
                                    response = controller.decreasePoinMembership(UserDataSingleton.getInstance().getUsername(),controller.checkMembership(UserDataSingleton.getInstance().getUsername()), 10);
                                    JOptionPane.showMessageDialog(null, response, "Terima kasih", JOptionPane.INFORMATION_MESSAGE);
                                    response = controller.increasePoinMembership(UserDataSingleton.getInstance().getUsername(),controller.checkMembership(UserDataSingleton.getInstance().getUsername()),5);
                                    JOptionPane.showMessageDialog(null, response, "Terima kasih", JOptionPane.INFORMATION_MESSAGE);
                                    int result = JOptionPane.showOptionDialog(null,"Apakah Anda ingin pesan lagi?","Konfirmasi",JOptionPane.YES_NO_OPTION,
                                    JOptionPane.QUESTION_MESSAGE,null,new Object[]{"Ya", "Tidak"},"Ya");
                                    if (result == JOptionPane.YES_OPTION) {
                                        new PesanFnb();
                                        f.dispose();
                                    } else {
                                        new MainMenuUserScreen();
                                        f.dispose();
                                    }
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "Maaf Poin Membership kamu tidak cukup, tambah lagi pesanan untuk tambah poin membershipmu", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            confirmationText+="\n Pakai 10 poin membership : Tidak"+
                                            "\n Total tunai : "+controller.totalHasilTransaksiFnb(hargaPerFnb.getText(), quantityField.getText());
                            int input = JOptionPane.showConfirmDialog(null, confirmationText, "Konfirmasi Pesanan FNB",JOptionPane.YES_NO_CANCEL_OPTION);
                                if (input == 0) {
                                    response = controller.insertTransaksiFnb(boxMenu.getSelectedItem().toString() ,Integer.parseInt(quantityField.getText()), (String) boxCinema.getSelectedItem(), UserDataSingleton.getInstance().getId());
                                    JOptionPane.showMessageDialog(null, response, "Terima kasih", JOptionPane.INFORMATION_MESSAGE);
                                    response = controller.increasePoinMembership(UserDataSingleton.getInstance().getUsername(),controller.checkMembership(UserDataSingleton.getInstance().getUsername()), 5);
                                    JOptionPane.showMessageDialog(null, response, "Terima kasih", JOptionPane.INFORMATION_MESSAGE);
                                    int result = JOptionPane.showOptionDialog(null,"Apakah Anda ingin pesan lagi?","Konfirmasi",JOptionPane.YES_NO_OPTION,
                                    JOptionPane.QUESTION_MESSAGE,null,new Object[]{"Ya", "Tidak"},"Ya");
                                    if (result == JOptionPane.YES_OPTION) {
                                        new PesanFnb();
                                        f.dispose();
                                    } else {
                                        new MainMenuUserScreen();
                                        f.dispose();
                                    }
                                }                        
                        }
                    }
                }            
            });

        f.add(labelKota);
        f.add(boxKota);
        f.add(labelCinema);
        f.add(boxCinema);
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
        f.add(labelTogglePoin);
        f.add(labelBenefit);
        f.getContentPane().add(checkBoxDiskon);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}
