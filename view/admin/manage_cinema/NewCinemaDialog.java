package view.admin.manage_cinema;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

public class NewCinemaDialog extends CinemaFormDialog {
    public NewCinemaDialog(Window owner) {
        super(owner);
        this.setTitle("Admin: Add new cinema");
        buttonSubmit.setText("Add new cinema");
        buttonSubmit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buttonSubmit.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (controller.addNewCinema(
                            fieldID.getText(),
                            fieldNama.getText(),
                            fieldAlamat.getText(),
                            fieldKota.getText(),
                            fotoCinema
                        ) == 0) {
                            JOptionPane.showMessageDialog(
                                owner,
                                "Operation success",
                                "Admin: Add new cinema",
                                JOptionPane.INFORMATION_MESSAGE
                            );
                        }
                    }
                });
            }
        });
        this.setVisible(true);
    }
}