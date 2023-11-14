package src.view.admin.manage_movie;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class NewMovieDialog extends MovieFormDialog {
    public NewMovieDialog(Window owner) {
        super(owner);
        this.setTitle("Admin: Add new movie");
        buttonSubmit.setText("Add new movie");
        buttonSubmit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // INSERT NEW MOVIE
            }
        });
        this.setVisible(true);
    }
}