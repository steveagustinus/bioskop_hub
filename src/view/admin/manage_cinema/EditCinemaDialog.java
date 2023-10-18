package src.view.admin.manage_cinema;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import src.controller.Controller;
import src.model.Cinema;

public class EditCinemaDialog extends CinemaFormDialog {
    Controller controller = new Controller();
    
    public EditCinemaDialog(Window owner, String idCinema) {
        super(owner);
        this.setTitle("Admin: Update cinema");
        Cinema cinema = controller.getCinemaById(idCinema);

        fieldID.setText(cinema.getId()); 
        fieldID.setEditable(false);   
        fieldNama.setText(cinema.getNama());
        fieldKota.setText(cinema.getKota());
        fieldAlamat.setText(cinema.getAlamat());

        fotoCinema = cinema.getImage();
        fChooser.setSelectedFile(fotoCinema);
        labelDisplayFoto.setIcon(new ImageIcon(
            new ImageIcon(fotoCinema.getAbsolutePath())
            .getImage()
            .getScaledInstance(450, 250,java.awt.Image.SCALE_SMOOTH)
        ));

        buttonSubmit.setText("Update cinema");
        buttonSubmit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (controller.editCinema(
                    fieldID.getText(),
                    fieldNama.getText(),
                    fieldAlamat.getText(),
                    fieldKota.getText(),
                    fotoCinema) == 0
                ) {
                    JOptionPane.showMessageDialog(
                        owner,
                        "Operation success",
                        "Admin: Edit cinema",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                }
            }
        });
        this.setVisible(true);
    }
}