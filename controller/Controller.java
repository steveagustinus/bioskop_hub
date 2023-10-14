package controller;

import java.sql.*;
import java.util.ArrayList;

public class Controller {
    public Controller() {

    }

    // return list of string nama cinema
    public ArrayList<String> getCinemaList() {
        ArrayList<String> cinemaList = new ArrayList<String>();
        try {
            Connection conn = null;
            Class.forName(Config.Database.JDBC_DRIVER);
            conn = DriverManager.getConnection(Config.Database.URL, Config.Database.USER, Config.Database.PASSWORD);

            Statement statement = conn.createStatement();
            ResultSet rows = statement.executeQuery(
                "SELECT `nama` FROM `cinema`"
            );

            while (rows.next()) {
                cinemaList.add(rows.getString("nama"));
            }

            rows.close();
            statement.close();
            conn.close();
        } catch (Exception ex) {
            new Error(ex.getMessage());
        }

        return cinemaList;
    }

    public void getData() {
        try {
            Connection conn = null;
            Class.forName(Config.Database.JDBC_DRIVER);
            conn = DriverManager.getConnection(Config.Database.URL, Config.Database.USER, Config.Database.PASSWORD);

            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(
                "SELECT * FROM movie"
            );

            while (resultSet.next()) {
                System.out.print(resultSet.getString("judul"));
                System.out.print(" " + resultSet.getString("director"));
            }

            resultSet.close();
            statement.close();
            conn.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
