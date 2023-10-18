package src.view.admin.manage_movie;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;

import src.controller.Controller;
import src.model.Cinema;

public class EditCinemaDialog extends MovieFormDialog {
    Controller controller = new Controller();
    
    public EditCinemaDialog(Window owner, String idCinema) {
        super(owner);
        this.setTitle("Admin: Update cinema");
        Cinema cinema = controller.getCinemaById(idCinema, false);

        fieldID.setText(cinema.getId()); 
        fieldID.setEditable(false);   
        fieldJudul.setText(cinema.getNama());
        //fieldKota.setText(cinema.getKota());
        fieldAlamat.setText(cinema.getAlamat());

        fotoMovie = cinema.getImage();
        fChooser.setSelectedFile(fotoMovie);
        labelDisplayFoto.setIcon(new ImageIcon(
            new ImageIcon(fotoMovie.getAbsolutePath())
            .getImage()
            .getScaledInstance(450, 250,java.awt.Image.SCALE_SMOOTH)
        ));

        buttonSubmit.setText("Update cinema");
        buttonSubmit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
            }
        });
        this.setVisible(true);
    }
}