package src.view.admin.manage_studio;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import src.controller.Controller;
import src.model.studio.Studio;

public class EditStudioDialog extends StudioFormDialog {
    Controller controller = new Controller();
    
    public EditStudioDialog(Window owner, String idStudio) {
        super(owner);
        this.setTitle("Admin: Update studio");
        Studio studio = controller.getStudioById(idStudio);

        fieldIdStudio.setText(studio.getIdStudio()); 
        fieldIdStudio.setEditable(false);   
        fieldIdCinema.setText(studio.getIdCinema());
        fieldStudioClass.setSelectedItem(controller.getStudioClassString(studio.getStudioClass()).toUpperCase());
        fieldStudioType.setSelectedItem(controller.getStudioTypeString(studio.getStudioType()));

        buttonSubmit.setText("Update studio");
        buttonSubmit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (controller.editStudio(
                    fieldIdStudio.getText(),
                    fieldIdCinema.getText(),
                    (String) fieldStudioClass.getSelectedItem(),
                    (String) fieldStudioType.getSelectedItem()
                ) == 0) {
                    JOptionPane.showMessageDialog(
                        owner,
                        "Operation success",
                        "Admin: Edit studio",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                } else {
                    JOptionPane.showMessageDialog(
                        owner,
                        "Update gagal",
                        "Admin: Edit studio",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
        this.setVisible(true);
    }
}