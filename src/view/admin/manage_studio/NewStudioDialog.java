package src.view.admin.manage_studio;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import src.controller.OperationCode;

public class NewStudioDialog extends StudioFormDialog {
    public NewStudioDialog(Window owner) {
        super(owner);
        String title = "Admin: Add new studio";
        this.setTitle(title);
        buttonSubmit.setText("Add new studio");
        buttonSubmit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int status = controller.addNewStudio(
                    fieldIdStudio.getText(),
                    fieldIdCinema.getText(),
                    (String) fieldStudioClass.getSelectedItem(),
                    (String) fieldStudioType.getSelectedItem()
                );
                
                if (status == OperationCode.AddNewStudio.SUCCESS) {
                    JOptionPane.showMessageDialog(
                        owner,
                        "Operation success",
                        title,
                        JOptionPane.INFORMATION_MESSAGE
                    );
                    close();
                } else {
                    if (status == OperationCode.AddNewStudio.EMPTYIDSTUDIO) {
                        JOptionPane.showMessageDialog(
                            owner, "Field ID tidak boleh kosong!", title, JOptionPane.ERROR_MESSAGE);
                    }
                    else if (status == OperationCode.AddNewStudio.INVALIDIDSTUDIO) {
                        JOptionPane.showMessageDialog(
                            owner, "Field ID studio harus berisi 10 karakter", title, JOptionPane.ERROR_MESSAGE);
                    }
                    else if (status == OperationCode.AddNewStudio.IDSTUDIOEXISTS) {
                        JOptionPane.showMessageDialog(
                            owner, "ID studio sudah digunakan", title, JOptionPane.ERROR_MESSAGE);
                    }
                    else if (status == OperationCode.AddNewStudio.EMPTYIDCINEMA) {
                        JOptionPane.showMessageDialog(
                            owner, "Field ID cinema tidak boleh kosong!", title, JOptionPane.ERROR_MESSAGE);
                    }
                    else if (status == OperationCode.AddNewStudio.INVALIDIDCINEMA) {
                        JOptionPane.showMessageDialog(
                            owner, "Field ID cinema harus berisi 10 karakter", title, JOptionPane.ERROR_MESSAGE);
                    }
                    else if (status == OperationCode.AddNewStudio.EMPTYSTUDIOCLASS) {
                        JOptionPane.showMessageDialog(
                            owner, "Field Studio Class tidak boleh kosong", title, JOptionPane.ERROR_MESSAGE);
                    }
                    else if (status == OperationCode.AddNewStudio.EMPTYSTUDIOTYPE) {
                        JOptionPane.showMessageDialog(
                            owner, "Field Studio Type tidak boleh kosong", title, JOptionPane.ERROR_MESSAGE);
                    }
                    else if (status == OperationCode.AddNewStudio.FAILONSEATGENERATION) {
                        JOptionPane.showMessageDialog(
                            owner, "Gagal memasukkan data kursi ke database", title, JOptionPane.ERROR_MESSAGE);
                    }
                    else if (status == OperationCode.AddNewStudio.ANYEXCEPTION) {
                        JOptionPane.showMessageDialog(
                            owner, "Terjadi error!", title, JOptionPane.ERROR_MESSAGE);
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