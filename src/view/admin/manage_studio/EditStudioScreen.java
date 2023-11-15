package src.view.admin.manage_studio;

import java.awt.Window;

import javax.swing.JOptionPane;

import src.controller.Controller;

public class EditStudioScreen {
    private Controller controller = new Controller();

    public EditStudioScreen(Window parent) {
        showEditStudioScreen(parent);
    }

    public void showEditStudioScreen(Window parent) {
        String inputID = JOptionPane.showInputDialog(
            parent,
            "Masukkan id studio: ",
            "Admin: Edit studio",
            JOptionPane.QUESTION_MESSAGE
        );

        if (controller.getStudioById(inputID) == null) {
            JOptionPane.showMessageDialog(
                parent,
                "Studio dengan id " + inputID + " tidak ada!",
                "Admin: Edit studio",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        
        new EditStudioDialog(parent, inputID);
    }
}
