package src.view.admin.hitung_pendapatan;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import src.controller.Controller;

public class HitungPendapatanPerKotaPerCabangScreen {
    JComboBox<String> kotaComboBox;
    JComboBox<String> cabangComboBox;
    String[] listCabang;
    public HitungPendapatanPerKotaPerCabangScreen(){
        JFrame frame = new JFrame();
        frame.setTitle("Hitung Pendapatan per Cabang per Kota");
        frame.setSize(425, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel kotaLabel = new JLabel("Kota : ");
        kotaLabel.setBounds(25, 10, 275, 20);

        String[] listKota = new Controller().listKotaHP();
        JComboBox<String> kotaComboBox = new JComboBox<String>(listKota);
        kotaComboBox.setBounds(100, 10, 275, 20);
        kotaComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String kota = (String) kotaComboBox.getSelectedItem();
                String[] listCabang = new Controller().listCabangHP(kota);
                cabangComboBox.removeAllItems();
                for (String cabang : listCabang) {
                    cabangComboBox.addItem(cabang);
                }
            }
        });

        JLabel cabangLabel = new JLabel("Cabang : ");
        cabangLabel.setBounds(25, 40, 275, 20);
        
        String[] listCabang = new Controller().listCabangHP(kotaComboBox.getSelectedItem().toString());
        cabangComboBox = new JComboBox<>(listCabang);
        cabangComboBox.setBounds(100, 40, 275, 20);

        JButton hitungButton = new JButton("Hitung");
        hitungButton.setBounds(165, 75, 100, 20);
        hitungButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String kota = (String) kotaComboBox.getSelectedItem();
                String cabang = (String) cabangComboBox.getSelectedItem();
                int result = new Controller().hitungPendapatanPerKotaPerCabang(kota, cabang);
                JOptionPane.showMessageDialog(null, "Pendapatan Kota : " + kota + "\n Cabang : " + cabang + "\n Pendapatan : Rp. " + new DecimalFormat("###,###").format(result));
                frame.dispose();
            }
        });

        panel.add(kotaLabel);
        panel.add(kotaComboBox);
        panel.add(cabangLabel);
        panel.add(cabangComboBox);
        panel.add(hitungButton);

        frame.add(panel);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
    public static void main(String[] args) {
        new HitungPendapatanPerKotaPerCabangScreen();
    }
}
