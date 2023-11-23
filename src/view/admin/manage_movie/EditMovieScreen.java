package src.view.admin.manage_movie;

import java.awt.Window;

import javax.swing.JOptionPane;

import src.controller.Controller;

public class EditMovieScreen {
    private Controller controller = new Controller();

    public EditMovieScreen(Window parent) {
        showEditMovieScreen(parent);
    }

    public void showEditMovieScreen(Window parent) {
        String inputID = JOptionPane.showInputDialog(
            null,
            "Masukkan id movie: ",
            "Admin: Edit movie",
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
        
        new EditMovieDialog(parent, inputID);
    }
}
