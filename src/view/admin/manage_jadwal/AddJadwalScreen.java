package src.view.admin.manage_jadwal;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import src.controller.Controller;

public class AddJadwalScreen {
    public AddJadwalScreen() {
        Controller controller = new Controller();
        JFrame frame = new JFrame();
        frame.setTitle("Add Add Jadwal");
        frame.setSize(400,200);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7    ,2));

        JLabel namaKota = new JLabel("Kota : ");
        JLabel cinema = new JLabel("Cinema : ");
        JLabel studio = new JLabel("Studio : ");
        JLabel movie = new JLabel("Movie : ");
        JLabel waktuJadwal = new JLabel("Waktu : (yyMMddhhmm)");
        JTextField waktu = new JTextField(1);
        JButton submitButton = new JButton("Tambah Jadwal");

        //kota
        panel.add(namaKota);
        String [] kota2 = controller.listKota();
        JComboBox kota = new JComboBox<>(kota2);
        panel.add(kota);

        //cinema
        panel.add(cinema);
        JComboBox cinemas = new JComboBox<>();
        kota.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] idcinema = controller.listCinema(kota.getSelectedItem().toString());
                cinemas.setModel(new DefaultComboBoxModel(idcinema));
            }
        });
        panel.add(cinemas);

        //studio
        panel.add(studio);
        JComboBox studios = new JComboBox();
        cinemas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String [] idStudio = controller.listStudio(cinemas.getSelectedItem().toString());
                studios.setModel(new DefaultComboBoxModel(idStudio));
            }
        });
        panel.add(studios);

        //movie
        panel.add(movie);
        JComboBox movies = new JComboBox();
        studios.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String [] idMovie = controller.listMovie(studios.getSelectedItem().toString());
                movies.setModel(new DefaultComboBoxModel(idMovie));
            }
        });

        panel.add(movies);

        //waktu
        panel.add(waktuJadwal);
        panel.add(waktu);

        //button
        panel.add(submitButton);
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
            }
        });




        frame.add(panel);
        frame.setVisible(true);

    }

    //test area
    public static void main(String[] args) {
        new AddJadwalScreen();

    }
}
