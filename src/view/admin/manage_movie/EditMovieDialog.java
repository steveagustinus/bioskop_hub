package src.view.admin.manage_movie;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;

import src.controller.Controller;
import src.model.movie.Movie;

public class EditMovieDialog extends MovieFormDialog {
    Controller controller = new Controller();
    
    public EditMovieDialog(Window owner, String idMovie) {
        super(owner);
        this.setTitle("Admin: Update movie");
        Movie movie = controller.getMovieById(idMovie);

        fieldID.setText(movie.getIdMovie()); 
        fieldID.setEditable(false);   
        fieldJudul.setText(movie.getJudul());
        fieldDirector.setText(movie.getDirector());
        fieldBahasa.setSelectedItem(movie.getLanguage());
        fieldDurasi.setText(String.valueOf(movie.getDurasi()));
        fieldSinopsis.setText(movie.getSinopsis());

        fotoMovie = movie.getFotoMovie();
        fChooser.setSelectedFile(fotoMovie);
        labelDisplayFoto.setIcon(new ImageIcon(
            new ImageIcon(fotoMovie.getAbsolutePath())
            .getImage()
            .getScaledInstance(225, 400, java.awt.Image.SCALE_SMOOTH)
        ));

        buttonSubmit.setText("Update movie");
        buttonSubmit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //UPDATE MOVIE
            }
        });
        this.setVisible(true);
    }
}