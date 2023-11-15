package src.view.admin.manage_studio;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

public class NewStudioDialog extends StudioFormDialog {
    public NewStudioDialog(Window owner) {
        super(owner);
        this.setTitle("Admin: Add new studio");
        buttonSubmit.setText("Add new studio");
        buttonSubmit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int status = controller.addNewStudio(
                    fieldIdStudio.getText(),
                    fieldIdCinema.getText(),
                    (String) fieldStudioClass.getSelectedItem(),
                    (String) fieldStudioType.getSelectedItem()
                );
                System.out.println(status);
                if (status == 0) {
                    JOptionPane.showMessageDialog(
                        owner,
                        "Operation success",
                        "Admin: Add new studio",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                } else {
                    JOptionPane.showMessageDialog(
                        owner,
                        "Insert gagal",
                        "Admin: Edit studio",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
        this.setVisible(true);
    }
}