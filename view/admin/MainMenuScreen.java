package view.admin;

import javax.swing.JOptionPane;

import controller.Controller;
import view.MainInterface;
import view.admin.manage_cinema.EditCinemaScreen;
import view.admin.manage_cinema.NewCinemaDialog;

public class MainMenuScreen implements MainInterface {
    Controller controller = new Controller();
    
    public MainMenuScreen() {
        controller.programStart();
        showMainMenuScreen();
    }

    public void showMainMenuScreen() {
        boolean exit = false;

        while (!exit) {
            String userInput = JOptionPane.showInputDialog(
                mainFrame,
                "1. Tambah cinema baru\r\n" +
                    "2. Edit cinema\r\n",
                "Admin",
                JOptionPane.QUESTION_MESSAGE
            );

            if (userInput == null) {
                exit = true;
                break;
            }

            switch (userInput) {
                case "1": new NewCinemaDialog(mainFrame); break;
                case "2": new EditCinemaScreen(mainFrame); break;
                case "3": exit = true; break;
            }
        }

        mainFrame.dispose();
    }
}
