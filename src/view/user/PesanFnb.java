package src.view.user;
import java.awt.Font;
import java.awt.Window;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import src.controller.Controller;
import src.controller.UserDataSingleton;
import src.view.MainInterface;
import src.view.user.payment.PaymentPanel;

public class PesanFnb implements MainInterface {
    private Controller controller = new Controller();
    private String fontFamily = "Dialog";
    private int totalBayar = 0;

    public class OrderConfirmation extends JDialog implements MainInterface {
        private String[] data;
        public OrderConfirmation(Window owner, String[] data) {
            super(owner, ModalityType.DOCUMENT_MODAL);
            this.data = data;

            this.setLocation(owner.getX(), owner.getY());
            this.setSize(400, 600);
            this.setLayout(null);
            this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            this.getContentPane().setBackground(FRAME_BACKGROUND);

            initializeComponent();
        }

        public void showDialog() {
            this.setVisible(true);
        }

        public void close() {
            this.setVisible(false);
            this.dispose();
        }

        public void initializeComponent() {
            JLabel labelHeader = new JLabel("Order Confirmation");
            labelHeader.setSize(this.getWidth(), 50);
            labelHeader.setLocation(0, 0);
            labelHeader.setFont(new Font(fontFamily, Font.BOLD, 30));
            labelHeader.setHorizontalAlignment(SwingConstants.CENTER);
            labelHeader.setVerticalAlignment(SwingConstants.CENTER);
            labelHeader.setForeground(TEXT_BACKGROUND);

            JLabel labelCinema = new JLabel(data[1] + " - " + data[0]);
            labelCinema.setSize(this.getWidth() - 20, 30);
            labelCinema.setLocation(10, labelHeader.getY() + labelHeader.getHeight() + 20);
            labelCinema.setFont(new Font(fontFamily, Font.BOLD, 20));
            labelCinema.setForeground(TEXT_BACKGROUND);

            JSeparator separator1 = new JSeparator();
            separator1.setOrientation(SwingConstants.HORIZONTAL);
            separator1.setSize(this.getWidth(), 5);
            separator1.setLocation(0, labelCinema.getY() + labelCinema.getHeight() + 5);
            separator1.setFont(new Font(fontFamily, Font.BOLD, 20));

            JLabel labelMenu = new JLabel(data[2]);
            labelMenu.setSize(this.getWidth() - 20, 25);
            labelMenu.setLocation(labelCinema.getX(), separator1.getY() + separator1.getHeight() + 5);
            labelMenu.setFont(new Font(fontFamily, Font.BOLD, 20));
            labelMenu.setForeground(TEXT_BACKGROUND);

            JLabel labelPrice = new JLabel("Harga satuan: " + data[3]);
            labelPrice.setSize(labelMenu.getWidth(), 15);
            labelPrice.setLocation(labelMenu.getX(), labelMenu.getY() + labelMenu.getHeight() + 5);
            labelPrice.setFont(new Font(fontFamily, Font.PLAIN, 15));
            labelPrice.setForeground(TEXT_BACKGROUND);

            JLabel labelQuantity = new JLabel("Jumlah pembelian: " + data[4]);
            labelQuantity.setSize(this.getWidth() - 20, 15);
            labelQuantity.setLocation(labelPrice.getX(), labelPrice.getY() + labelPrice.getHeight() + 5);
            labelQuantity.setFont(new Font(fontFamily, Font.PLAIN, 15));
            labelQuantity.setForeground(TEXT_BACKGROUND);

            JLabel labelTotalBayar = new JLabel("Total pembayaran: ");
            labelTotalBayar.setSize(this.getWidth() - 20, 25);
            labelTotalBayar.setLocation(labelQuantity.getX(), labelQuantity.getY() + labelQuantity.getHeight() + 10);
            labelTotalBayar.setFont(new Font(fontFamily, Font.BOLD, 20));
            labelTotalBayar.setForeground(TEXT_BACKGROUND);

            JLabel labelTotalBayar2 = new JLabel(controller.formatCurrency(totalBayar));
            labelTotalBayar2.setSize(this.getWidth() - 20, 30);
            labelTotalBayar2.setLocation(labelTotalBayar.getX(), labelTotalBayar.getY() + labelTotalBayar.getHeight() + 10);
            labelTotalBayar2.setFont(new Font(fontFamily, Font.BOLD, 25));
            labelTotalBayar.setForeground(TEXT_BACKGROUND);

            JLabel labelInformasiPenggunaanPoin = new JLabel(
                "*Anda menggunakan 10 poin membership anda."
            );
            labelInformasiPenggunaanPoin.setSize(this.getWidth() - 20, 20);
            labelInformasiPenggunaanPoin.setLocation(labelTotalBayar2.getX(), labelTotalBayar2.getY() + labelTotalBayar2.getHeight() + 5);
            labelInformasiPenggunaanPoin.setFont(new Font(fontFamily, Font.BOLD, 13));
            labelInformasiPenggunaanPoin.setForeground(TEXT_BACKGROUND);
            
            JLabel labelInformasiPenggunaanPoin2 = new JLabel(
                " Total pembayaran sudah dipotong Rp. 100,000"
            );
            labelInformasiPenggunaanPoin2.setSize(this.getWidth() - 20, 20);
            labelInformasiPenggunaanPoin2.setLocation(labelInformasiPenggunaanPoin.getX(), labelInformasiPenggunaanPoin.getY() + labelInformasiPenggunaanPoin.getHeight() + 5);
            labelInformasiPenggunaanPoin2.setFont(new Font(fontFamily, Font.BOLD, 13));
            labelInformasiPenggunaanPoin2.setForeground(TEXT_BACKGROUND);
            
            if (data[5].equals("Tidak")) {
                labelInformasiPenggunaanPoin.setVisible(false);
                labelInformasiPenggunaanPoin2.setVisible(false);
            }

            JSeparator separator2 = new JSeparator();
            separator2.setOrientation(SwingConstants.HORIZONTAL);
            separator2.setSize(this.getWidth(), 5);
            separator2.setLocation(0, labelInformasiPenggunaanPoin2.getY() + labelInformasiPenggunaanPoin2.getHeight() + 5);
            separator2.setFont(new Font(fontFamily, Font.BOLD, 20));

            PaymentPanel panelPayment = new PaymentPanel(this.getWidth() - 20, 200);
            panelPayment.setLocation(labelInformasiPenggunaanPoin2.getX(), separator2.getY() + separator2.getHeight() + 10);

            JButton buttonPesan = new JButton("Bayar");
            buttonPesan.setSize(this.getWidth() / 3, 30);
            buttonPesan.setLocation(
                buttonPesan.getWidth(),
                panelPayment.getY() + panelPayment.getHeight() + 10
            );
            buttonPesan.setBackground(BUTTON_BACKGROUND);
            buttonPesan.setForeground(BUTTON_FOREGROUND);

            buttonPesan.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    String response = controller.insertTransaksiFnb(
                        data[2],
                        Integer.parseInt(data[4]),
                        data[1],
                        UserDataSingleton.getInstance().getId(),
                        panelPayment.getPaymentMethod()
                    );

                    JOptionPane.showMessageDialog(null, response, "Terima kasih", JOptionPane.INFORMATION_MESSAGE);

                    if (response.equals("Transaksi Berhasil")) {
                        int membershipStatus = controller.checkMembership(UserDataSingleton.getInstance().getUsername());
                        if (status == 1) {
                            response = controller.decreasePoinMembership(
                                UserDataSingleton.getInstance().getUsername(),
                                membershipStatus,
                                10
                            );
                            JOptionPane.showMessageDialog(null, response, "Terima kasih", JOptionPane.INFORMATION_MESSAGE);
                        }
                        else if (membershipStatus == 1 && totalBayar / 100000 > 0) {
                            response = controller.increasePoinMembership(
                                UserDataSingleton.getInstance().getUsername(),
                                membershipStatus,
                                totalBayar / 100000
                            );
                            JOptionPane.showMessageDialog(null, response, "Terima kasih", JOptionPane.INFORMATION_MESSAGE);
                        }

                        close();
                        pesanLagi();
                    }
                }
                
            });
            this.add(labelHeader);
            this.add(labelCinema);
            this.add(separator1);
            this.add(labelMenu);
            this.add(labelPrice);
            this.add(labelQuantity);
            this.add(labelTotalBayar);
            this.add(labelTotalBayar2);
            this.add(labelInformasiPenggunaanPoin);
            this.add(labelInformasiPenggunaanPoin2);
            this.add(separator2);
            this.add(panelPayment);
            this.add(buttonPesan);
        }
    }

    public PesanFnb(){
        viewPesanFnb();
    };

    private JDialog f = new JDialog();
    public int status;
    public String response;

    private void viewPesanFnb() {
        f.setTitle("Pesan FNB");
        f.setModalityType(ModalityType.DOCUMENT_MODAL);
        f.getContentPane().setBackground(FRAME_BACKGROUND);
        f.setLayout(null);
        f.setSize(500, 400);

        JLabel labelKota = new JLabel("Kota : ");
        labelKota.setBounds(10, 15, 200, 30);
        labelKota.setForeground(TEXT_BACKGROUND);

        JComboBox<String> boxKota = new JComboBox<>(controller.listKota());
        boxKota.setBounds(170, 15, 200, 30);
        boxKota.setBackground(FRAME_BACKGROUND);
        boxKota.setForeground(TEXT_BACKGROUND);

        JLabel labelCinema = new JLabel("Cinema : ");
        labelCinema.setBounds(10, 60, 200, 30);
        labelCinema.setForeground(TEXT_BACKGROUND);
        
        JComboBox<String> boxCinema = new JComboBox<>(controller.listCinema(boxKota.getSelectedItem().toString()));
        boxCinema.setBounds(170, 60, 200, 30);
        boxCinema.setBackground(FRAME_BACKGROUND);
        boxCinema.setForeground(TEXT_BACKGROUND);

        JLabel labelMenu =  new JLabel("Pilih Menu : ");
        labelMenu.setBounds(10, 105, 200, 30);
        labelMenu.setForeground(TEXT_BACKGROUND);

        JComboBox<String> boxMenu = new JComboBox<>(controller.listFNB());
        boxMenu.setBounds(170, 105, 200, 30);
        boxMenu.setBackground(FRAME_BACKGROUND);
        boxMenu.setForeground(TEXT_BACKGROUND);

        JLabel labelHarga = new JLabel("Harga Menu: ");
        labelHarga.setBounds(10, 150, 200, 30);
        labelHarga.setForeground(TEXT_BACKGROUND);

        JLabel hargaPerFnb = new JLabel();
        hargaPerFnb.setText(controller.hargaPerFnb((String) boxMenu.getSelectedItem()));
        hargaPerFnb.setBounds(170, 150, 200, 30);
        hargaPerFnb.setForeground(TEXT_BACKGROUND);

        JLabel quantityLabel = new JLabel("Jumlah Barang:");
        quantityLabel.setBounds(10, 195, 200, 30);
        quantityLabel.setForeground(TEXT_BACKGROUND);

        JTextField quantityField = new JTextField();
        quantityField.setBounds(170, 195, 200, 30);
        quantityField.setBackground(FRAME_BACKGROUND);
        quantityField.setForeground(TEXT_BACKGROUND);

        JLabel totalTunaiLabel = new JLabel("Total tunai: ");
        totalTunaiLabel.setBounds(10, 240, 200, 30);
        totalTunaiLabel.setForeground(TEXT_BACKGROUND);

        JLabel totalTunaiHasil = new JLabel();
        totalTunaiHasil.setBounds(170, 240, 200, 30);
        totalTunaiHasil.setForeground(TEXT_BACKGROUND);

        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(250, 320, 200, 30);
        submitButton.setBackground(BUTTON_BACKGROUND);
        submitButton.setForeground(BUTTON_FOREGROUND);

        JButton buttonToMainMenu = new JButton("Back to Main Menu");
        buttonToMainMenu.setBounds(10, 320, 200, 30);
        buttonToMainMenu.setBackground(BUTTON_BACKGROUND);
        buttonToMainMenu.setForeground(BUTTON_FOREGROUND);

        JLabel labelTogglePoin = new JLabel("Pakai 10 poin (?):");
        labelTogglePoin.setBounds(40, 272, 200, 30);
        labelTogglePoin.setForeground(TEXT_BACKGROUND);
        
        JLabel labelBenefit = new JLabel("untuk dapatkan potongan Rp. 100,000");
        labelBenefit.setBounds(40, 288, 300, 30);
        labelBenefit.setForeground(TEXT_BACKGROUND);

        JCheckBox checkBoxDiskon = new JCheckBox();
        checkBoxDiskon.setBounds(10, 280, 30, 30);
        checkBoxDiskon.setBackground(FRAME_BACKGROUND);
        checkBoxDiskon.setForeground(TEXT_BACKGROUND);

        if (UserDataSingleton.getInstance().getMembership_status() == 0) {
            checkBoxDiskon.setVisible(false);
            labelTogglePoin.setVisible(false);
            labelBenefit.setVisible(false);
        }

        totalBayar = controller.totalHasilTransaksiFnb(hargaPerFnb.getText(), quantityField.getText(), checkBoxDiskon.isSelected());
        totalTunaiHasil.setText(controller.formatCurrency(totalBayar));

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
                totalBayar = controller.totalHasilTransaksiFnb(hargaPerFnb.getText(), quantityField.getText(), checkBoxDiskon.isSelected());
                totalTunaiHasil.setText(controller.formatCurrency(totalBayar));
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                totalBayar = controller.totalHasilTransaksiFnb(hargaPerFnb.getText(), quantityField.getText(), checkBoxDiskon.isSelected());
                totalTunaiHasil.setText(controller.formatCurrency(totalBayar));
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                totalBayar = controller.totalHasilTransaksiFnb(hargaPerFnb.getText(), quantityField.getText(), checkBoxDiskon.isSelected());
                totalTunaiHasil.setText(controller.formatCurrency(totalBayar));
            }
            
        });
        
        checkBoxDiskon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(controller.checkSufficientPoint(UserDataSingleton.getInstance().getMembership_point(), 10)) {
                    boolean isSelected = checkBoxDiskon.isSelected();
                    status = isSelected ? 1 : 0;
                    
                    totalBayar = controller.totalHasilTransaksiFnb(hargaPerFnb.getText(), quantityField.getText(), checkBoxDiskon.isSelected());
                    totalTunaiHasil.setText(controller.formatCurrency(totalBayar));
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
                    if (quantityField.getText().equals("") || Integer.parseInt(quantityField.getText()) == 0) {
                        JOptionPane.showMessageDialog(null, "Jumlah tidak boleh kosong", "Peringatan", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    String[] data = new String[] {
                        boxKota.getSelectedItem().toString(),
                        boxCinema.getSelectedItem().toString(),
                        boxMenu.getSelectedItem().toString(),
                        controller.hargaPerFnb((String) boxMenu.getSelectedItem()),
                        quantityField.getText(),
                        status == 1 ? "Ya" : "Tidak"
                    };

                    OrderConfirmation ocDialog = new OrderConfirmation(f, data);
                    ocDialog.showDialog();
                }            
            });

        f.add(labelKota);
        f.add(boxKota);
        f.add(labelCinema);
        f.add(boxCinema);
        f.add(labelMenu);
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

    public void pesanLagi() {
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
