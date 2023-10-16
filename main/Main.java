package main;

import controller.Controller;
import view.admin.NewCinemaScreen;

public class Main {
    public static void main(String[] args) {
        Controller controller = new Controller();
        controller.getCinemaList();

        new NewCinemaScreen();
    }
}