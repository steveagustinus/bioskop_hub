package src.view.admin;

import java.awt.Font;
import java.awt.Panel;

import javax.swing.JButton;
import javax.swing.JLabel;

import src.controller.Controller;
import src.view.LoginScreen;
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
        mainFrame.setSize(500, 500);

        Panel panel = new Panel();
        panel.setLayout(null);

        JLabel mainlabel = new JLabel("Admin Main Menu!");
        mainlabel.setBounds(130, 10, 500, 50);
        panel.add(mainlabel);
        mainlabel.setFont(new Font("Arial", Font.BOLD, 24));

        JButton manageCinemaButton = new JButton("Tambah Cinema");
        manageCinemaButton.setBounds(40, 70, 200, 30);
        manageCinemaButton.addActionListener(e -> {
            new NewCinemaDialog(mainFrame);
        });

        JButton manageStudioButton = new JButton("Tambah Studio");
        manageStudioButton.setBounds(40, 120, 200, 30);
        manageStudioButton.addActionListener(e -> {
            new NewStudioDialog(mainFrame);
        });

        JButton manageMovieButton = new JButton("Tambah Movie");
        manageMovieButton.setBounds(40, 170, 200, 30);
        manageMovieButton.addActionListener(e -> {
            //new NewMovieDialog(mainFrame);
        });

        JButton manageJadwalButton = new JButton("Tambah Jadwal");
        manageJadwalButton.setBounds(40, 220, 200, 30);
        manageJadwalButton.addActionListener(e -> {
            new NewJadwalDialog(mainFrame);
        });

        JButton manageCinemaButton2 = new JButton("Edit Cinema");
        manageCinemaButton2.setBounds(260, 70, 200, 30);
        manageCinemaButton2.addActionListener(e -> {
            new EditCinemaScreen(mainFrame);
        });

        JButton manageStudioButton2 = new JButton("Edit Studio");
        manageStudioButton2.setBounds(260, 120, 200, 30);
        manageStudioButton2.addActionListener(e -> {
            new EditStudioScreen(mainFrame);
        });

        JButton manageMovieButton2 = new JButton("Edit Movie");
        manageMovieButton2.setBounds(260, 170, 200, 30);
        manageMovieButton2.addActionListener(e -> {
            //new EditMovieScreen(mainFrame);
        });

        JButton manageJadwalButton2 = new JButton("Edit Jadwal");
        manageJadwalButton2.setBounds(260, 220, 200, 30);
        manageJadwalButton2.addActionListener(e -> {
            new EditJadwalScreen(mainFrame);
        });

        JButton hitungPendapatanButton = new JButton("Hitung Pendapatan");
        hitungPendapatanButton.setBounds(40, 270, 200, 30);
        hitungPendapatanButton.addActionListener(e -> {
            new HitungPendapatanScreen();
        });

        JButton raiseRevokeButton = new JButton("Raise/Revoke Membership");
        raiseRevokeButton.setBounds(260, 270, 200, 30);
        raiseRevokeButton.addActionListener(e -> {
            // new RaiseRevokeMembershipScreen();
        });

        
        panel.add(manageCinemaButton);
        panel.add(manageStudioButton);
        panel.add(manageMovieButton);
        panel.add(manageJadwalButton);
        panel.add(manageCinemaButton2);
        panel.add(manageStudioButton2);
        panel.add(manageMovieButton2);
        panel.add(manageJadwalButton2);
        panel.add(raiseRevokeButton);
        panel.add(hitungPendapatanButton);



        JButton logoutButton = new JButton("Logout");
        logoutButton.setBounds(150, 410, 200, 30);
        logoutButton.addActionListener(e -> {
            mainFrame.dispose();
            new LoginScreen();
        });
        panel.add(logoutButton);
        mainFrame.add(panel);
        mainFrame.setVisible(true);
        mainFrame.setLocationRelativeTo(null);
    }

    // test area
    public static void main(String[] args) {
        new MainMenuScreen();
    }
}
