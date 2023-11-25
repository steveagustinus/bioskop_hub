package src.view.admin.manage_movie;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import src.controller.Controller;
import src.controller.OperationCode;
import src.model.movie.Movie;

public class EditMovieDialog extends MovieFormDialog {
    private Controller controller = new Controller();

    private JButton buttonDelete;
    
    public EditMovieDialog(Window owner, String idMovie) {
        super(owner);
        String title = "Admin: Update movie";
        this.setTitle(title);

        Movie movie = controller.getMovieById(idMovie);
        fieldID.setText(movie.getIdMovie()); 
        fieldID.setEditable(false);
        fieldJudul.setText(movie.getJudul());
        datePickerReleaseDate.getJFormattedTextField().setText(controller.localDateToString(movie.getReleaseDate(), "MMM dd, yyyy"));
        fieldDirector.setText(movie.getDirector());
        fieldLanguage.setSelectedItem(controller.getMovieLanguageString(movie.getLanguage()));
        fieldDurasi.setText(String.valueOf(movie.getDurasi()));
        fieldSinopsis.setText(movie.getSinopsis());
        fotoMovie = movie.getFotoMovie();
        fChooser.setSelectedFile(fotoMovie);
        labelDisplayFoto.setIcon(new ImageIcon(
            new ImageIcon(fotoMovie.getAbsolutePath())
            .getImage()
            .getScaledInstance(225, 400,java.awt.Image.SCALE_SMOOTH)
        ));

        buttonSubmit.setText("Update movie");
        buttonSubmit.setSize(
            (this.getWidth() - 15) / 2,
            buttonSubmit.getHeight()
        );
        buttonSubmit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int status = controller.editMovie(
                    fieldID.getText(),
                    fieldJudul.getText(),
                    datePickerReleaseDate.getJFormattedTextField().getText(),
                    fieldDirector.getText(),
                    (String) fieldLanguage.getSelectedItem(),
                    fieldDurasi.getText(),
                    fieldSinopsis.getText(),
                    fChooser.getSelectedFile()
                );

                if (status == OperationCode.EditMovie.SUCCESS) {
                    JOptionPane.showMessageDialog(
                        owner,
                        "Operation success",
                        title,
                        JOptionPane.INFORMATION_MESSAGE
                    );
                    close();
                } else {
                    if (status == OperationCode.EditMovie.EMPTYIDMOVIE) { 
                        JOptionPane.showMessageDialog(
                            owner, "Field ID tidak boleh kosong!", title, JOptionPane.ERROR_MESSAGE
                        );
                    }
                    else if (status == OperationCode.EditMovie.EMPTYJUDUL) {
                        JOptionPane.showMessageDialog(
                            owner, "Field Judul tidak boleh kosong!", title, JOptionPane.ERROR_MESSAGE
                        );
                    }
                    else if (status == OperationCode.EditMovie.EMPTYRELEASEDATE) {
                        JOptionPane.showMessageDialog(
                            owner, "Field Tanggal Release tidak boleh kosong!", title, JOptionPane.ERROR_MESSAGE
                        );
                    }
                    else if (status == OperationCode.EditMovie.EMPTYDIRECTOR) {
                        JOptionPane.showMessageDialog(
                            owner, "Field Sutradara tidak boleh kosong!", title, JOptionPane.ERROR_MESSAGE
                        );
                    }
                    else if (status == OperationCode.EditMovie.EMPTYLANGUAGE) {
                        JOptionPane.showMessageDialog(
                            owner, "Field Bahasa tidak boleh kosong!", title, JOptionPane.ERROR_MESSAGE
                        );
                    }
                    else if (status == OperationCode.EditMovie.EMPTYDURASI) {
                        JOptionPane.showMessageDialog(
                            owner, "Field Durasi tidak boleh kosong!", title, JOptionPane.ERROR_MESSAGE
                        );
                    }
                    else if (status == OperationCode.EditMovie.INVALIDDURASI) {
                        JOptionPane.showMessageDialog(
                            owner, "Field Durasi harus berupa angka!", title, JOptionPane.ERROR_MESSAGE
                        );
                    }
                    else if (status == OperationCode.EditMovie.EMPTYSINOPSIS) {
                        JOptionPane.showMessageDialog(
                            owner, "Field Sinopsis tidak boleh kosong!", title, JOptionPane.ERROR_MESSAGE
                        );
                    }
                    else if (status == OperationCode.EditMovie.EMPTYFOTO) {
                        JOptionPane.showMessageDialog(
                            owner, "Harap memasukkan foto untuk film!", title, JOptionPane.ERROR_MESSAGE
                        );
                    }
                    else if (status == OperationCode.EditMovie.ANYEXCEPTION) {
                        JOptionPane.showMessageDialog(
                            owner, "Terjadi error", title, JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            }
        });

        buttonDelete = new JButton("Delete movie");
        buttonDelete.setSize(buttonSubmit.getSize());
        buttonDelete.setLocation(
            buttonSubmit.getX() + buttonSubmit.getWidth() + 5,
            buttonSubmit.getY()
        );

        buttonDelete.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (JOptionPane.showConfirmDialog(
                        owner,
                        "Apakah anda yakin ingin menghapus film " + movie.getJudul(),
                        title,
                        JOptionPane.YES_NO_OPTION 
                    ) == JOptionPane.YES_OPTION
                ) {
                    int status = controller.deleteMovie(idMovie);

                    if (status == OperationCode.DeleteMovie.SUCCESS) {
                        JOptionPane.showMessageDialog(
                            owner,
                            "Film \"" + movie.getJudul() + "\" berhasil dihapus!",
                            title,
                            JOptionPane.INFORMATION_MESSAGE
                        );
                        close();
                    }
                    else {
                        if (status == OperationCode.DeleteMovie.EMPTYIDMOVIE) {
                            JOptionPane.showMessageDialog(owner, "ID Movie tidak boleh kosong!", title, JOptionPane.ERROR_MESSAGE
                            );
                        }
                        else if (status == OperationCode.DeleteMovie.ANYEXCEPTION) {
                            JOptionPane.showMessageDialog(owner, "Terjadi error!", title, JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }
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