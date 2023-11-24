package src.view.admin.hitung_pendapatan;

import src.controller.Controller;

import java.text.DecimalFormat;

import javax.swing.JOptionPane;

public class HitungPendapatanSemua {
    public HitungPendapatanSemua() {
        Controller controller = new Controller();
        // JOptionPane.showMessageDialog(null, "Hitung Pendapatan Semua", "Hitung Pendapatan",
        //         JOptionPane.INFORMATION_MESSAGE);
        int totalPendapatan = controller.hitungPendapatanTotal();
        JOptionPane.showMessageDialog(null, "Total Pendapatan : Rp. " + new DecimalFormat("###,###").format(totalPendapatan), "Hitung Pendapatan",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
