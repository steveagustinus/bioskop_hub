package src.view.admin.manage_fnb;

import javax.swing.*;
import java.awt.*;

public class AddFnBScreen {
    public AddFnBScreen(){
        JFrame frame = new JFrame();
        frame.setTitle("Add FnB");
        frame.setSize(400,200);
        JTextField namaFnB = new JTextField(40);
        JTextField harga = new JTextField(40);;
        JTextArea description= new JTextArea();
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4,2));
        JLabel namaFnBlabel = new JLabel("Nama FnB : ");
        JLabel hargaFnBlabel = new JLabel("Harga FnB : ");
        JLabel descriptionFnBlabel = new JLabel("Deskripsi FnB : ");
        JButton submitButton = new JButton("Submit");
        submitButton.setSize(100,50);

        panel.add(namaFnBlabel);
        panel.add(namaFnB);

        panel.add(hargaFnBlabel);
        panel.add(harga);

        panel.add(descriptionFnBlabel);
        panel.add(description);

        panel.add(submitButton);

        frame.add(panel);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new AddFnBScreen();
    }
}
