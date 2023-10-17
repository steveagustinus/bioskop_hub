package view.admin.manage_cinema;

import java.awt.Window;

import javax.swing.JOptionPane;

import controller.Controller;

public class EditCinemaScreen {
    private Controller controller = new Controller();

    public EditCinemaScreen(Window parent) {
        showCinemaScreen(parent);
    }

    public void showCinemaScreen(Window parent) {
        String inputID = JOptionPane.showInputDialog(
            null,
            "Masukkan id cinema: ",
            "Admin: Edit cinema",
            JOptionPane.QUESTION_MESSAGE
        );

        if (!controller.isCinemaExists(inputID)) {
            JOptionPane.showMessageDialog(
                parent,
                "Cinema dengan id " + inputID + " tidak ada!",
                "Admin: Edit cinema",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        
        new EditCinemaDialog(parent, inputID);
    }
}
