package src.view.admin.manage_jadwal;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import src.controller.Controller;
import src.model.Jadwal;

public class EditJadwalDialog extends JadwalFormDialog {
    Controller controller = new Controller();
    
    public EditJadwalDialog(Window owner, String idJadwal) {
        super(owner);
        String title = "Admin: Update jadwal";
        this.setTitle(title);

        Jadwal jadwal = controller.getJadwalById(idJadwal);
        fieldID.setText(jadwal.getIdJadwal()); 
        fieldID.setEditable(false);

        fieldIDStudio.setText(jadwal.getIdStudio());
        
        datePickerTanggal.getJFormattedTextField().setText(
            controller.localDateToString(jadwal.getWaktu().toLocalDate(), "MMM dd, yyyy")
        );

        fieldWaktu.setText(jadwal.getWaktu().toLocalTime().toString());
        fieldIDMovie.setText(jadwal.getIdMovie());
        fieldHarga.setText(String.valueOf(jadwal.getHarga()));

        buttonSubmit.setText("Update jadwal");
        buttonSubmit.setSize(
            (this.getWidth() - 15) / 2,
            buttonSubmit.getHeight()
        );

        JButton buttonDelete = new JButton("Delete jadwal");
        buttonDelete.setSize(buttonSubmit.getSize());
        buttonDelete.setLocation(
            buttonSubmit.getX() + buttonSubmit.getWidth() + 5,
            buttonSubmit.getY()
        );

        buttonSubmit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                boolean status = controller.deleteJadwal(idJadwal);System.out.println(idJadwal);
                    if(status==true){
                        JOptionPane.showMessageDialog(null, "Masuk ke pengaturan edit jadwal.", "Konfirmasi", JOptionPane.INFORMATION_MESSAGE);
                        close();
                        new NewJadwalDialog(owner);
                    } else {
                        JOptionPane.showMessageDialog(null, "Gagal menghapus jadwal, telah terdapat transaksi user", "Peringatan", JOptionPane.WARNING_MESSAGE);
                        close();
                    }
            }
        });        

        buttonDelete.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String idJadwal = fieldID.getText();
                boolean status = controller.deleteJadwal(idJadwal);System.out.println(idJadwal);
                    if(status==true){
                        JOptionPane.showMessageDialog(null, "Jadwal telah terhapus.", "Konfirmasi", JOptionPane.INFORMATION_MESSAGE);
                        close();
                    } else {
                        JOptionPane.showMessageDialog(null, "Gagal menghapus jadwal, telah terdapat transaksi user", "Peringatan", JOptionPane.WARNING_MESSAGE);
                        close();
                    }
                }
            
        });

        this.add(buttonDelete);

        this.setVisible(true);
    }

    public void close() {
        this.setVisible(false);
        this.dispose();
    }
}