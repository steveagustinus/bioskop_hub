package src.view.admin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class HitungPendapatanScreen {
    public HitungPendapatanScreen(){
        JFrame frame = new JFrame();
        frame.setTitle("Hitung Pendapatan");
        frame.setSize(400, 125);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        JLabel label = new JLabel("Hitung Pendapatan : ");
        JButton cabangButton = new JButton("Hitung Harga per Cabang");
        JButton kotaButton = new JButton("Hitung Harga per Kota");
        JButton semuaButton = new JButton("Hitung Harga Semua");

        panel.add(label);
        panel.add(cabangButton);
        panel.add(kotaButton);
        panel.add(semuaButton);

         cabangButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
            }
        });
        kotaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    
            }
        });

        semuaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
            }
        });

        frame.add(panel);

        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        
    }
    
}
