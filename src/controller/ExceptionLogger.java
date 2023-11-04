package src.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExceptionLogger {
    public ExceptionLogger(String message) {
        // try {
        //     File file = new File("controller/exception.log");
        //     file.createNewFile();

        //     FileWriter fileWriter = new FileWriter(file.getAbsolutePath());
        //     String content = "[" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) + "]: " + message;
        //     fileWriter.write(content);
        //     fileWriter.close();
        // } catch (IOException ioEx) {
        //     System.out.println(ioEx);
        // } catch (Exception ex) {
        //     System.out.println(ex);
        // }

        try {
            File file = new File("src/controller/exception.log");
            file.createNewFile();

            String content = "[" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) + "]: " + message + "\r\n";

            BufferedWriter out = new BufferedWriter(new FileWriter(file.getAbsolutePath(), true));
            out.write(content);
            out.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
