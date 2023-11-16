package src.view.admin;

import javax.swing.JOptionPane;

import src.controller.Controller;
import src.model.user.User;
import src.view.MainInterface;
import src.view.admin.manage_cinema.EditCinemaScreen;
import src.view.admin.manage_cinema.NewCinemaDialog;
import src.view.admin.manage_jadwal.EditJadwalScreen;
import src.view.admin.manage_jadwal.NewJadwalDialog;
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
                    "7. Tambah jadwal baru\r\n" +
                    "8. Edit jadwal\r\n" + 
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
                case "3": new NewJadwalDialog(mainFrame); break;
                case "4": new EditJadwalScreen(mainFrame); break;
                case "5": new NewStudioDialog(mainFrame); break;
                case "6": new EditStudioScreen(mainFrame); break;
                case "7": new NewJadwalDialog(mainFrame); break;
                case "8": new EditStudioScreen(mainFrame); break;
                case "9": exit = true; break;
            }
        }
        mainFrame.dispose();
    }
}
