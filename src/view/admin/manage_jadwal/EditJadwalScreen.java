package src.view.admin.manage_jadwal;

import java.awt.Window;

import javax.swing.JOptionPane;

import src.controller.Controller;

public class EditJadwalScreen {
    private Controller controller = new Controller();

    public EditJadwalScreen(Window parent) {
        showEditJadwalScreen(parent);
    }

    public void showEditJadwalScreen(Window parent) {
        String inputID = JOptionPane.showInputDialog(
            parent,
            "Masukkan id jadwal: ",
            "Admin: Edit jadwal",
            JOptionPane.QUESTION_MESSAGE
        );

        if (!controller.isMovieExists(inputID)) {
            JOptionPane.showMessageDialog(
                parent,
                "Movie dengan id " + inputID + " tidak ada!",
                "Admin: Edit movie",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        
        new EditJadwalDialog(parent, inputID);
    }
}
