package src.view.admin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import src.view.admin.hitung_pendapatan.HitungPendapataCabangScreen;
import src.view.admin.hitung_pendapatan.HitungPendapatanKotaScreen;
import src.view.admin.hitung_pendapatan.HitungPendapatanSemua;

public class HitungPendapatanScreen {
    public HitungPendapatanScreen(){
        JFrame frame = new JFrame();
        frame.setTitle("Hitung Pendapatan");
        frame.setSize(400, 175);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel label = new JLabel("Hitung Pendapatan : ");
        label.setBounds(25, 0, 250, 20);
        JButton cabangButton = new JButton("Hitung Pendapatan per Cabang");
        cabangButton.setBounds(75, 25, 250, 20);
        JButton kotaButton = new JButton("Hitung Pendapatan per Kota");
        kotaButton.setBounds(75, 50, 250, 20);
        JButton semuaButton = new JButton("Hitung Semua Pendapatan");
        semuaButton.setBounds(75, 75, 250, 20);

        JButton backButton = new JButton("Back");
        backButton.setBounds(250, 110, 100, 20);

        panel.add(label);
        panel.add(cabangButton);
        panel.add(kotaButton);
        panel.add(semuaButton);
        panel.add(backButton);

         cabangButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new HitungPendapataCabangScreen();
            }
        });
        kotaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new HitungPendapatanKotaScreen();
            }
        });

        semuaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new HitungPendapatanSemua();
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                //Tinggal diarahin ke mana
            }
        });

        frame.add(panel);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
}
