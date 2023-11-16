package src.view.admin.manage_jadwal;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import src.controller.Controller;


public class NewJadwalDialog extends JadwalFormDialog {
    Controller controller = new Controller();

    public NewJadwalDialog(Window owner) {
        super(owner);
        String title = "Admin: Add new jadwal";
        this.setTitle(title);
        buttonSubmit.setText("Add new jadwal");
        buttonSubmit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
            }
        });
        this.setVisible(true);
    }

    public void close() {
        this.setVisible(false);
        this.dispose();
    }
}