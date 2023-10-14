package controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Error {
    public Error(String message) {
        try {
            File file = new File("error.log");
            file.createNewFile();

            FileWriter fileWriter = new FileWriter("error.log");
            String content = "[" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) + "]: " + message;
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException ioEx) {
            System.out.println(ioEx);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
