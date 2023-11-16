package src.view.admin.manage_cinema;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import src.controller.OperationCode;

public class NewCinemaDialog extends CinemaFormDialog {
    public NewCinemaDialog(Window owner) {
        super(owner);
        String title = "Admin: Add new cinema";
        this.setTitle(title);
        buttonSubmit.setText("Add new cinema");
        buttonSubmit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int status = controller.addNewCinema(
                    fieldID.getText(),
                    fieldNama.getText(),
                    fieldAlamat.getText(),
                    fieldKota.getText(),
                    fotoCinema
                );
                if (status == OperationCode.AddNewCinema.SUCCESS) {
                    JOptionPane.showMessageDialog(
                        owner,
                        "Operation success",
                        title,
                        JOptionPane.INFORMATION_MESSAGE
                    );
                    close();
                } else {
                    if (status == OperationCode.AddNewCinema.EMPTYIDCINEMA) {
                        JOptionPane.showMessageDialog(
                            owner, "Field ID tidak boleh kosong!", title, JOptionPane.ERROR_MESSAGE
                        );
                    }
                    else if (status == OperationCode.AddNewCinema.INVALIDIDCINEMA) {
                        JOptionPane.showMessageDialog(
                            owner, "Field ID harus berisi 10 karakter!", title, JOptionPane.ERROR_MESSAGE
                        );
                    }
                    else if (status == OperationCode.AddNewCinema.EMPTYNAMA) {
                        JOptionPane.showMessageDialog(
                            owner, "Field Nama tidak boleh kosong!", title, JOptionPane.ERROR_MESSAGE
                        );
                    }
                    else if (status == OperationCode.AddNewCinema.EMPTYALAMAT) {
                        JOptionPane.showMessageDialog(
                            owner, "Field Alamat tidak boleh kosong!", title, JOptionPane.ERROR_MESSAGE
                        );
                    }
                    else if (status == OperationCode.AddNewCinema.EMPTYKOTA) {
                        JOptionPane.showMessageDialog(
                            owner, "Field Kota tidak boleh kosong!", title, JOptionPane.ERROR_MESSAGE
                        );
                    }
                    else if (status == OperationCode.AddNewCinema.EMPTYFOTO) {
                        JOptionPane.showMessageDialog(
                            owner, "Harap memasukkan foto cinema!", title, JOptionPane.ERROR_MESSAGE
                        );
                    }
                    else if (status == OperationCode.AddNewCinema.IDCINEMAEXISTS) {
                        JOptionPane.showMessageDialog(
                            owner, "ID Cinema sudah digunakan!", title, JOptionPane.ERROR_MESSAGE
                        );
                    }
                    else if (status == OperationCode.AddNewCinema.ANYEXCEPTION) {
                        JOptionPane.showMessageDialog(
                            owner, "Terjadi error!", title, JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            }
        });
        this.setVisible(true);
    }

    public void close() {
        this.setVisible(false);
        this.dispose();
    }
}