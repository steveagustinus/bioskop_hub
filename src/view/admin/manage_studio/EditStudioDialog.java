package src.view.admin.manage_studio;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import src.controller.Controller;
import src.controller.OperationCode;
import src.model.studio.Studio;

public class EditStudioDialog extends StudioFormDialog {
    Controller controller = new Controller();
    
    public EditStudioDialog(Window owner, String idStudio) {
        super(owner);
        String title = "Admin: Update studio";
        this.setTitle(title);
        Studio studio = controller.getStudioById(idStudio);

        fieldIdStudio.setText(studio.getIdStudio()); 
        fieldIdStudio.setEditable(false);   
        fieldIdCinema.setSize(
            fieldIdCinema.getWidth() + buttonSelectCinema.getWidth() + 5,
            fieldIdCinema.getHeight()
        );
        fieldIdCinema.setText(studio.getIdCinema());
        buttonSelectCinema.setVisible(false);
        fieldStudioClass.setSelectedItem(controller.getStudioClassString(studio.getStudioClass()).toUpperCase());
        fieldStudioType.setSelectedItem(controller.getStudioTypeString(studio.getStudioType()));

        buttonSubmit.setText("Update studio");
        buttonSubmit.setSize(
            (this.getWidth() - 15) / 2,
            buttonSubmit.getHeight()
        );
        buttonSubmit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int status = controller.editStudio(
                    fieldIdStudio.getText(),
                    (String) fieldStudioClass.getSelectedItem(),
                    (String) fieldStudioType.getSelectedItem()
                );
                
                if (status == OperationCode.EditStudio.SUCCESS) {
                    JOptionPane.showMessageDialog(
                        owner,
                        "Operation success",
                        title,
                        JOptionPane.INFORMATION_MESSAGE
                    );
                    close();
                } else {
                    if (status == OperationCode.EditStudio.EMPTYIDSTUDIO) {
                        JOptionPane.showMessageDialog(
                            owner, "Field ID tidak boleh kosong!", title, JOptionPane.ERROR_MESSAGE);
                    }
                    else if (status == OperationCode.EditStudio.EMPTYSTUDIOCLASS) {
                        JOptionPane.showMessageDialog(
                            owner, "Field Studio Class tidak boleh kosong", title, JOptionPane.ERROR_MESSAGE);
                    }
                    else if (status == OperationCode.EditStudio.EMPTYSTUDIOTYPE) {
                        JOptionPane.showMessageDialog(
                            owner, "Field Studio Type tidak boleh kosong", title, JOptionPane.ERROR_MESSAGE);
                    }
                    else if (status == OperationCode.EditStudio.ANYEXCEPTION) {
                        JOptionPane.showMessageDialog(
                            owner, "Terjadi kesalahan!", title, JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        JButton buttonDelete = new JButton("Delete studio");
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
                        "Apakah anda yakin ingin menghapus studio " + studio.getIdStudio(),
                        title,
                        JOptionPane.YES_NO_OPTION 
                    ) == JOptionPane.YES_OPTION
                ) {
                    int status = controller.deleteStudio(idStudio);

                    if (status == OperationCode.DeleteStudio.SUCCESS) {
                        JOptionPane.showMessageDialog(
                            owner,
                            "Studio \"" + studio.getIdStudio() + "\" berhasil dihapus!",
                            title,
                            JOptionPane.INFORMATION_MESSAGE
                        );
                        close();
                    }
                    else {
                        if (status == OperationCode.DeleteStudio.EMPTYIDSTUDIO) {
                            JOptionPane.showMessageDialog(owner, "ID Studio tidak boleh kosong!", title, JOptionPane.ERROR_MESSAGE
                            );
                        }
                        else if (status == OperationCode.DeleteStudio.ANYEXCEPTION) {
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