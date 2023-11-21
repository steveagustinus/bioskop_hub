package src.view.admin.manage_jadwal;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import src.controller.Controller;
import src.controller.OperationCode;


public class NewJadwalDialog extends JadwalFormDialog {
    Controller controller = new Controller();

    public NewJadwalDialog(Window owner) {
        super(owner);
        
        String title = "Admin: Add new jadwal";
        this.setTitle(title);

        fieldID.setEditable(false);

        fieldIDStudio.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void changedUpdate(DocumentEvent e) { }

            @Override
            public void insertUpdate(DocumentEvent e) { setFieldIDText(); }

            @Override
            public void removeUpdate(DocumentEvent e) { }
            
        });

        fieldIDMovie.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void changedUpdate(DocumentEvent e) { }

            @Override
            public void insertUpdate(DocumentEvent e) { setFieldIDText(); }

            @Override
            public void removeUpdate(DocumentEvent e) { }
            
        });

        fieldWaktu.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void changedUpdate(DocumentEvent e) { }

            @Override
            public void insertUpdate(DocumentEvent e) { setFieldIDText(); }

            @Override
            public void removeUpdate(DocumentEvent e) { setFieldIDText(); }
            
        });

        buttonSubmit.setText("Add new jadwal");
        buttonSubmit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int status = controller.addNewJadwal(
                    fieldID.getText(),
                    fieldIDStudio.getText(),
                    fieldIDMovie.getText(),
                    fieldHarga.getText(),
                    datePickerTanggal.getJFormattedTextField().getText(),
                    fieldWaktu.getText()
                );
                
                if (status == OperationCode.AddNewJadwal.SUCCESS) {
                    JOptionPane.showMessageDialog(
                        owner,
                        "Operation success",
                        title,
                        JOptionPane.INFORMATION_MESSAGE
                    );
                    close();
                } else {
                    if (status == OperationCode.AddNewJadwal.EMPTYIDJADWAL) {
                        JOptionPane.showMessageDialog(
                            owner, "Field ID tidak boleh kosong!", title, JOptionPane.ERROR_MESSAGE
                        );
                    }
                    else if (status == OperationCode.AddNewJadwal.INVALIDIDJADWAL) {
                        JOptionPane.showMessageDialog(
                            owner, "ID jadwal tidak valid!", title, JOptionPane.ERROR_MESSAGE
                        );
                    }
                    else if (status == OperationCode.AddNewJadwal.EMPTYIDSTUDIO) {
                        JOptionPane.showMessageDialog(
                            owner, "Field ID studio tidak boleh kosong!", title, JOptionPane.ERROR_MESSAGE
                        );
                    }
                    else if (status == OperationCode.AddNewJadwal.STUDIOISNOTEXISTS) {
                        JOptionPane.showMessageDialog(
                            owner, "Studio tidak ditemukan!", title, JOptionPane.ERROR_MESSAGE
                        );
                    }
                    else if (status == OperationCode.AddNewJadwal.EMPTYIDMOVIE) {
                        JOptionPane.showMessageDialog(
                            owner, "Field ID movie tidak boleh kosong!", title, JOptionPane.ERROR_MESSAGE
                        );
                    }
                    else if (status == OperationCode.AddNewJadwal.MOVIEISNOTEXISTS) {
                        JOptionPane.showMessageDialog(
                            owner, "Film tidak ditemukan!", title, JOptionPane.ERROR_MESSAGE
                        );
                    }
                    else if (status == OperationCode.AddNewJadwal.EMPTYHARGA) {
                        JOptionPane.showMessageDialog(
                            owner, "Field harga tidak boleh kosong!", title, JOptionPane.ERROR_MESSAGE
                        );
                    }
                    else if (status == OperationCode.AddNewJadwal.INVALIDHARGA) {
                        JOptionPane.showMessageDialog(
                            owner, "Harga harus berupa angka!", title, JOptionPane.ERROR_MESSAGE
                        );
                    }
                    else if (status == OperationCode.AddNewJadwal.EMPTYTANGGAL) {
                        JOptionPane.showMessageDialog(
                            owner, "Field tanggal tidak boleh kosong!", title, JOptionPane.ERROR_MESSAGE
                        );
                    }
                    else if (status == OperationCode.AddNewJadwal.EMPTYJAM) {
                        JOptionPane.showMessageDialog(
                            owner, "Field jam tidak boleh kosong!", title, JOptionPane.ERROR_MESSAGE
                        );
                    }
                    else if (status == OperationCode.AddNewJadwal.INVALIDWAKTU) {
                        JOptionPane.showMessageDialog(
                            owner, "Field waktu tidak valid!", title, JOptionPane.ERROR_MESSAGE
                        );
                    }
                    else if (status == OperationCode.AddNewJadwal.ANYEXCEPTION) {
                        JOptionPane.showMessageDialog(
                            owner, "Terjadi error!", title, JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            }
        });
        this.setVisible(true);
    }

    public void setFieldIDText() {
        fieldID.setText(controller.generateJadwalID(
            fieldIDStudio.getText(),
            fieldIDMovie.getText(),
            datePickerTanggal.getJFormattedTextField().getText(),
            fieldWaktu.getText()
        ));
    }

    public void close() {
        this.setVisible(false);
        this.dispose();
    }
}