package src.view.admin.manage_movie;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import src.controller.Controller;
import src.controller.OperationCode;


public class NewMovieDialog extends MovieFormDialog {
    Controller controller = new Controller();

    public NewMovieDialog(Window owner) {
        super(owner);
        String title = "Admin: Add new movie";
        this.setTitle(title);
        buttonSubmit.setText("Add new movie");
        buttonSubmit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int status = controller.addNewMovie(
                    fieldID.getText(),
                    fieldJudul.getText(),
                    datePickerReleaseDate.getJFormattedTextField().getText(),
                    fieldDirector.getText(),
                    (String) fieldLanguage.getSelectedItem(),
                    fieldDurasi.getText(),
                    fieldSinopsis.getText(),
                    fChooser.getSelectedFile()
                );

                if (status == OperationCode.AddNewMovie.SUCCESS) {
                    JOptionPane.showMessageDialog(
                        owner,
                        "Operation success",
                        title,
                        JOptionPane.INFORMATION_MESSAGE
                    );
                    close();
                } else {
                    if (status == OperationCode.AddNewMovie.EMPTYIDMOVIE) { 
                        JOptionPane.showMessageDialog(
                            owner, "Field ID tidak boleh kosong!", title, JOptionPane.ERROR_MESSAGE
                        );
                    }
                    else if (status == OperationCode.AddNewMovie.EMPTYJUDUL) {
                        JOptionPane.showMessageDialog(
                            owner, "Field Judul tidak boleh kosong!", title, JOptionPane.ERROR_MESSAGE
                        );
                    }
                    else if (status == OperationCode.AddNewMovie.EMPTYRELEASEDATE) {
                        JOptionPane.showMessageDialog(
                            owner, "Field Tanggal Release tidak boleh kosong!", title, JOptionPane.ERROR_MESSAGE
                        );
                    }
                    else if (status == OperationCode.AddNewMovie.EMPTYDIRECTOR) {
                        JOptionPane.showMessageDialog(
                            owner, "Field Sutradara tidak boleh kosong!", title, JOptionPane.ERROR_MESSAGE
                        );
                    }
                    else if (status == OperationCode.AddNewMovie.EMPTYLANGUAGE) {
                        JOptionPane.showMessageDialog(
                            owner, "Field Bahasa tidak boleh kosong!", title, JOptionPane.ERROR_MESSAGE
                        );
                    }
                    else if (status == OperationCode.AddNewMovie.EMPTYDURASI) {
                        JOptionPane.showMessageDialog(
                            owner, "Field Durasi tidak boleh kosong!", title, JOptionPane.ERROR_MESSAGE
                        );
                    }
                    else if (status == OperationCode.AddNewMovie.INVALIDDURASI) {
                        JOptionPane.showMessageDialog(
                            owner, "Field Durasi harus berupa angka!", title, JOptionPane.ERROR_MESSAGE
                        );
                    }
                    else if (status == OperationCode.AddNewMovie.EMPTYSINOPSIS) {
                        JOptionPane.showMessageDialog(
                            owner, "Field Sinopsis tidak boleh kosong!", title, JOptionPane.ERROR_MESSAGE
                        );
                    }
                    else if (status == OperationCode.AddNewMovie.EMPTYFOTO) {
                        JOptionPane.showMessageDialog(
                            owner, "Harap memasukkan foto untuk film!", title, JOptionPane.ERROR_MESSAGE
                        );
                    }
                    else if (status == OperationCode.AddNewMovie.ANYEXCEPTION) {
                        JOptionPane.showMessageDialog(
                            owner, "Terjadi error", title, JOptionPane.ERROR_MESSAGE
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