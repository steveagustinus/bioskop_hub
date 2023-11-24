package src.view.user;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import src.controller.Controller;
import src.controller.UserDataSingleton;
import src.view.user.payment.PaymentPanel;

public class PesanFnb {
    public PesanFnb(){
        viewPesanFnb();
    };

    public int status;
    public String response;

    private void viewPesanFnb() {
        Controller controller = new Controller();

        JDialog f = new JDialog();
        f.setTitle("Pesan FNB");
        f.setModalityType(ModalityType.DOCUMENT_MODAL);
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
        labelTogglePoin.setBounds(40, 272, 200, 30);
        JLabel labelBenefit = new JLabel("untuk dapatkan potongan Rp. 100,000");
        labelBenefit.setBounds(40, 288, 200, 30);

        JCheckBox checkBoxDiskon = new JCheckBox();
        checkBoxDiskon.setBounds(10, 280, 30, 30);

        if (UserDataSingleton.getInstance().getMembership_status() == 0) {
            checkBoxDiskon.setVisible(false);
            labelTogglePoin.setVisible(false);
            labelBenefit.setVisible(false);
        }

        totalTunaiHasil.setText(
            controller.formatCurrency(
                controller.totalHasilTransaksiFnb(hargaPerFnb.getText(), quantityField.getText(), checkBoxDiskon.isSelected())
            )
        );

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
                totalTunaiHasil.setText(
                    controller.formatCurrency(
                        controller.totalHasilTransaksiFnb(hargaPerFnb.getText(), quantityField.getText(), checkBoxDiskon.isSelected())
                    )
                );
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                totalTunaiHasil.setText(
                    controller.formatCurrency(
                        controller.totalHasilTransaksiFnb(hargaPerFnb.getText(), quantityField.getText(), checkBoxDiskon.isSelected())
                    )
                );
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                totalTunaiHasil.setText(
                    controller.formatCurrency(
                        controller.totalHasilTransaksiFnb(hargaPerFnb.getText(), quantityField.getText(), checkBoxDiskon.isSelected())
                    )
                );
            }
            
        });
        
        checkBoxDiskon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(controller.checkSufficientPoint(UserDataSingleton.getInstance().getMembership_point(), 10)) {
                    boolean isSelected = checkBoxDiskon.isSelected();
                    status = isSelected ? 1 : 0;
                    
                    totalTunaiHasil.setText(
                        controller.formatCurrency(
                            controller.totalHasilTransaksiFnb(hargaPerFnb.getText(), quantityField.getText(), checkBoxDiskon.isSelected())
                        )
                    );
                } else {
                    checkBoxDiskon.setSelected(false);
                    JOptionPane.showMessageDialog(null, "Maaf Poin Membership kamu tidak cukup, tambah lagi pesanan untuk tambah poin membershipmu", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        buttonToMainMenu.addActionListener(e -> {
            f.setVisible(false);
            f.dispose();
        });

        submitButton.addActionListener(new ActionListener() {
            @Override
                public void actionPerformed(ActionEvent e) {
                    if (Integer.parseInt(quantityField.getText()) == 0) {
                        JOptionPane.showMessageDialog(null, "Jumlah tidak boleh kosong", "Peringatan", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    String confirmationText = "Kota : "+boxKota.getSelectedItem().toString()+
                                            "\n Cinema : "+boxCinema.getSelectedItem().toString()+
                                            "\n Menu : "+boxMenu.getSelectedItem().toString()+
                                            "\n Harga Menu : "+controller.hargaPerFnb((String) boxMenu.getSelectedItem())+
                                            "\n Jumlah barang : "+quantityField.getText();
                    
                    // Pakai poin
                    if(status == 1) {
                        confirmationText+="\n Pakai 10 poin membership : Ya";
                    } else {
                        confirmationText+="\n Pakai 10 poin membership : Tidak";
                    }

                    int totalTunai = controller.totalHasilTransaksiFnb(hargaPerFnb.getText(), quantityField.getText(), checkBoxDiskon.isSelected());
                    confirmationText+="\n Total tunai : "+controller.formatCurrency(totalTunai);

                    int input = JOptionPane.showConfirmDialog(null, confirmationText, "Konfirmasi Pesanan FNB",JOptionPane.YES_NO_CANCEL_OPTION);
                    if (input == 0) {
                        response = controller.insertTransaksiFnb(boxMenu.getSelectedItem().toString() ,Integer.parseInt(quantityField.getText()), (String) boxCinema.getSelectedItem(), UserDataSingleton.getInstance().getId());
                        JOptionPane.showMessageDialog(null, response, "Terima kasih", JOptionPane.INFORMATION_MESSAGE);
                        
                        if (status == 1) {
                            response = controller.increasePoinMembership(UserDataSingleton.getInstance().getUsername(),controller.checkMembership(UserDataSingleton.getInstance().getUsername()), totalTunai / 100000);
                            JOptionPane.showMessageDialog(null, response, "Terima kasih", JOptionPane.INFORMATION_MESSAGE);
                        }

                        int result = JOptionPane.showOptionDialog(null,"Apakah Anda ingin pesan lagi?","Konfirmasi",JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,null,new Object[]{"Ya", "Tidak"},"Ya");
                        
                        if (result == JOptionPane.YES_OPTION) {
                            f.setVisible(false);
                            f.dispose();
                            new PesanFnb();
                        } else {
                            f.setVisible(false);
                            f.dispose();
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
