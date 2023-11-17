package src.view.admin;

import javax.swing.JOptionPane;

import src.controller.Controller;
import src.model.Jadwal;
import src.model.seat.Seat;
import src.model.user.Customer;
import src.model.user.User;
import src.view.MainInterface;
import src.view.admin.manage_cinema.EditCinemaScreen;
import src.view.admin.manage_cinema.NewCinemaDialog;
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

        User user2 = controller.login("steve", "123");
        System.out.println(user2.getUsername());

        System.out.println(
            controller.pesanTiket(
                (Customer) user2,
                new Jadwal(
                    "SH_LPKBAL-001_PERJANGAIB_2312010900",
                    null,
                    null,
                    50000,
                    null,
                    null
                ),
                new Seat[] {
                    new Seat("27", null, 1),
                    new Seat("28", null, 1),
                })
        );
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
                case "4": new EditMovieScreen(mainFrame); break;
                case "5": new NewStudioDialog(mainFrame); break;
                case "6": new EditStudioScreen(mainFrame); break;
                case "7": exit = true; break;
            }
        }
        mainFrame.dispose();
    }
}
