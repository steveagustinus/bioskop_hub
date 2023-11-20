package src.view.admin.hitung_pendapatan;

import javax.swing.JOptionPane;

import src.controller.Controller;

public class HitungPendapataCabangScreen {
    public HitungPendapataCabangScreen() {
        Controller controller = new Controller();
        JOptionPane.showMessageDialog(null, "Hitung Pendapatan per Cabang", "Hitung Pendapatan",
                JOptionPane.INFORMATION_MESSAGE);
        String cabang = JOptionPane.showInputDialog(null, "Masukkan nama Cabang : ", "Hitung Pendapatan",
                JOptionPane.QUESTION_MESSAGE);
        boolean isCabangValid = controller.isCabangExists(cabang);
        if (!isCabangValid) {
            JOptionPane.showMessageDialog(null, "Cabang tidak ditemukan", "Hitung Pendapatan",
                    JOptionPane.ERROR_MESSAGE);
            return;
        } else {
            int pendapatan = controller.hitungPendapatanCabang(cabang);
            JOptionPane.showMessageDialog(null, "Pendapatan Cabang " + cabang + " adalah Rp " + pendapatan + ",00",
                    "Hitung Pendapatan", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
