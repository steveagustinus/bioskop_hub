package src.view.admin.manage_movie;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import src.controller.Controller;


public class NewMovieDialog extends MovieFormDialog {
    Controller controller = new Controller();
    public NewMovieDialog(Window owner) {
        super(owner);
        this.setTitle("Admin: Add new movie");
        buttonSubmit.setText("Add new movie");
        buttonSubmit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (controller.addNewMovie(
                    fieldID.getText(),
                    fieldJudul.getText(),
                    controller.stringToLocalDate(datePickerReleaseDate.getJFormattedTextField().getText(), "MMM d, yyyy"),
                    fieldDirector.getText(),
                    Integer.parseInt(fieldLanguage.getText()),
                    Integer.parseInt(fieldDurasi.getText()),
                    fieldSinopsis.getText(),
                    fChooser.getSelectedFile()
                ) == 0) {
                    JOptionPane.showMessageDialog(
                        owner,
                        "Operation success",
                        "Admin: Add movie",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                }
            }
        });
        this.setVisible(true);
    }
}