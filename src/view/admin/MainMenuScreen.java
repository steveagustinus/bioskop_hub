package src.view.admin;

import javax.swing.JOptionPane;

import src.controller.Controller;
import src.model.user.User;
import src.view.MainInterface;
import src.view.admin.manage_cinema.EditCinemaScreen;
import src.view.admin.manage_cinema.NewCinemaDialog;
import src.view.admin.manage_movie.EditMovieDialog;
import src.view.admin.manage_movie.EditMovieScreen;
import src.view.admin.manage_movie.NewMovieDialog;
import src.view.admin.manage_studio.EditStudioScreen;
import src.view.admin.manage_studio.NewStudioDialog;

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

        System.out.println(controller.createTransactionId());
        // test area

        boolean exit = false;

        while (!exit) {
            String userInput = JOptionPane.showInputDialog(
                mainFrame,
                "1. Tambah cinema baru\r\n" +
                    "2. Edit cinema\r\n" +
                    "3. Tambah movie baru\r\n" +
                    "4. Edit movie\r\n" +
                    "5. Tambah studio baru\r\n" +
                    "6. Edit studio\r\n" + 
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
                case "4": new EditCinemaScreen(mainFrame); break;
                case "5": new NewStudioDialog(mainFrame); break;
                case "6": new EditStudioScreen(mainFrame); break;
                case "7": exit = true; break;

            }
        }
        mainFrame.dispose();
    }
}
