package src.view.admin.manage_cinema;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import src.controller.Controller;
import src.controller.OperationCode;
import src.model.Cinema;

public class EditCinemaDialog extends CinemaFormDialog {
    Controller controller = new Controller();
    
    public EditCinemaDialog(Window owner, String idCinema) {
        super(owner);
        String title = "Admin: Update cinema";
        this.setTitle(title);
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
        buttonSubmit.setSize(
            (this.getWidth() - 15) / 2,
            buttonSubmit.getHeight()
        );
        buttonSubmit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int status = controller.editCinema(
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
                    if (status == OperationCode.EditCinema.EMPTYNAMA) {
                        JOptionPane.showMessageDialog(
                            owner, "Field Nama tidak boleh kosong!", title, JOptionPane.ERROR_MESSAGE
                        );
                    }
                    else if (status == OperationCode.EditCinema.EMPTYALAMAT) {
                        JOptionPane.showMessageDialog(
                            owner, "Field Alamat tidak boleh kosong!", title, JOptionPane.ERROR_MESSAGE
                        );
                    }
                    else if (status == OperationCode.EditCinema.EMPTYKOTA) {
                        JOptionPane.showMessageDialog(
                            owner, "Field Kota tidak boleh kosong!", title, JOptionPane.ERROR_MESSAGE
                        );
                    }
                    else if (status == OperationCode.EditCinema.EMPTYFOTO) {
                        JOptionPane.showMessageDialog(
                            owner, "Harap memasukkan foto cinema!", title, JOptionPane.ERROR_MESSAGE
                        );
                    }
                    else if (status == OperationCode.EditCinema.ANYEXCEPTION) {
                        JOptionPane.showMessageDialog(
                            owner, "Terjadi error!", title, JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            }
        });

        JButton buttonDelete = new JButton("Delete cinema");
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
                        "Apakah anda yakin ingin menghapus cinema " + cinema.getNama(),
                        title,
                        JOptionPane.YES_NO_OPTION 
                    ) == JOptionPane.YES_OPTION
                ) {
                    int status = controller.deleteCinema(idCinema);

                    if (status == OperationCode.DeleteCinema.SUCCESS) {
                        JOptionPane.showMessageDialog(
                            owner,
                            "Cinema \"" + cinema.getNama() + "\" berhasil dihapus!",
                            title,
                            JOptionPane.INFORMATION_MESSAGE
                        );
                        close();
                    } else {
                        if (status == OperationCode.DeleteCinema.EMPTYIDCINEMA) {
                            JOptionPane.showMessageDialog(
                                owner, "Field ID tidak boleh kosong!", title, JOptionPane.ERROR_MESSAGE
                            );
                        }
                        else if (status == OperationCode.DeleteCinema.ANYEXCEPTION) {
                            JOptionPane.showMessageDialog(
                                owner, "Terjadi error!", title, JOptionPane.ERROR_MESSAGE
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