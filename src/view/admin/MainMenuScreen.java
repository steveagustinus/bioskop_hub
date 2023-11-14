package src.view.admin;

import javax.swing.JOptionPane;

import src.controller.Controller;
import src.model.user.User;
import src.view.MainInterface;
import src.view.admin.manage_cinema.EditCinemaScreen;
import src.view.admin.manage_cinema.NewCinemaDialog;
import src.view.admin.manage_movie.EditMovieScreen;
import src.view.admin.manage_movie.NewMovieDialog;

public class MainMenuScreen implements MainInterface {
    Controller controller = new Controller();
    
    public MainMenuScreen() {
        controller.programStart();
        showMainMenuScreen();
    }

    public void showMainMenuScreen() {
        // test area
        User user = controller.login("admin", "123");
        System.out.println(user.getUsername());
        // test area

        boolean exit = false;

        while (!exit) {
            String userInput = JOptionPane.showInputDialog(
                mainFrame,
                "Selamat datang di menu admin\r\n\r\n" +
                    "1. Tambah cinema baru\r\n" +
                    "2. Edit cinema\r\n" +
                    "3. Tambah movie baru\r\n" +
                    "4. Edit movie\r\n" +
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
                case "3": new NewMovieDialog(mainFrame); break;
                case "4": new EditMovieScreen(mainFrame); break;
                case "5": exit = true; break;
            }
        }
        mainFrame.dispose();
    }
}
