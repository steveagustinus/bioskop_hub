package src.view.admin.hitung_pendapatan;

import java.text.DecimalFormat;

import javax.swing.JOptionPane;

import src.controller.Controller;

public class HitungPendapatanKotaScreen {
        public HitungPendapatanKotaScreen() {
                Controller controller = new Controller();
                // JOptionPane.showMessageDialog(null, "Hitung Pendapatan per Kota", "Hitung Pendapatan",
                //                 JOptionPane.INFORMATION_MESSAGE);
                String kota = JOptionPane.showInputDialog(null, "Masukkan nama Kota : ", "Hitung Pendapatan",
                                JOptionPane.QUESTION_MESSAGE);
                boolean isKotaValid = controller.isKotaExists(kota);
                if (!isKotaValid) {
                        JOptionPane.showMessageDialog(null, "Kota tidak ditemukan", "Hitung Pendapatan",
                                        JOptionPane.ERROR_MESSAGE);
                        return;
                } else {
                        int pendapatan = controller.hitungPendapatanKota(kota);
                        JOptionPane.showMessageDialog(null,
                                        "Pendapatan Kota " + kota + " adalah Rp. " + new DecimalFormat("###,###").format(pendapatan),
                                        "Hitung Pendapatan",
                                        JOptionPane.INFORMATION_MESSAGE);
                }
        }
}
