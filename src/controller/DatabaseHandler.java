package src.controller;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseHandler {
    public Connection connection;

    private Connection logOn() {
        try {
            Class.forName(Config.Database.JDBC_DRIVER);
            connection = DriverManager.getConnection(Config.Database.URL, Config.Database.USER, Config.Database.PASSWORD);
        } catch (Exception ex) {
            new ExceptionLogger(ex.getMessage());
        }
        return connection;
    }

    private void logOff() {
        try {
            connection = logOn();
        } catch (Exception ex) {
            new ExceptionLogger(ex.getMessage());
        }
    }

    public void open() {
        try {
            connection = logOn();
        } catch (Exception ex) {
            new ExceptionLogger(ex.getMessage());
        }
    }

    public void close() {
        try {
            logOff();
        } catch (Exception ex) {
            new ExceptionLogger(ex.getMessage());
        }
    }
}
