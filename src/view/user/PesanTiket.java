package src.view.user;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.SqlDateModel;
import src.controller.Controller;

public class PesanTiket {

    public static void main(String[] args) {
        Controller controller = new Controller();
        JFrame frame = new JFrame("Pesan Tiket");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 900);

        JLabel labelKota = new JLabel("Kota : ");
        String kota[] = controller.listKota();
        JComboBox<String> boxKota = new JComboBox<>(kota);
        labelKota.setBounds(10, 15, 200, 30);
        boxKota.setBounds(170, 15, 200, 30);


        JLabel labelCinema = new JLabel("Cabang : ");
        JComboBox<String> boxCinema = new JComboBox<>();
        final String[] tempCinema = {""}; 

        boxCinema.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] idCinema = controller.listCinema(boxCinema.getSelectedItem().toString());
                boxCinema.setModel(new DefaultComboBoxModel<>(idCinema));
                tempCinema[0] = (String) boxCinema.getSelectedItem();
            }
        });

        labelCinema.setBounds(10, 60, 200, 30);
        boxCinema.setBounds(170, 60, 200, 30);

        JLabel studio =  new JLabel("Studio : ");
        JComboBox<String> boxStudio = new JComboBox<>();
        boxCinema.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] idStudio = controller.listStudio(boxCinema.getSelectedItem().toString());
                boxStudio.setModel(new DefaultComboBoxModel<>(idStudio));
            }
        });        
        studio.setBounds(10, 105, 200, 30);
        boxStudio.setBounds(170, 105, 200, 30);

        JLabel labelFilm =  new JLabel("Film : ");
        JComboBox<String> boxFilm = new JComboBox<>();
        boxFilm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] idFilm = controller.listStudio(boxStudio.getSelectedItem().toString());
                boxFilm.setModel(new DefaultComboBoxModel<>(idFilm));
            }
        });        
        labelFilm.setBounds(10, 105, 200, 30);
        boxFilm.setBounds(170, 105, 200, 30);
        
        JLabel tanggalLabel = new JLabel("Pilih Tanggal:");
        SqlDateModel dateModel2 = new SqlDateModel();
        JDatePanelImpl datePanel2 = new JDatePanelImpl(dateModel2);
        JDatePickerImpl inputTanggalPembuatan = new JDatePickerImpl(datePanel2);
        tanggalLabel.setBounds(10, 85, 200, 30);
        inputTanggalPembuatan.setBounds(170, 85, 200, 30);
        
        
        JLabel jamLabel = new JLabel("Pilih Jam Tayang:");
        JRadioButton jam1 = new JRadioButton("08.00");
        JRadioButton jam2 = new JRadioButton("10.00");

        ButtonGroup filmGroup = new ButtonGroup();

        filmGroup.add(jam1);
        filmGroup.add(jam2);

        frame.add(labelKota);
        frame.add(boxKota);
        frame.add(labelCinema);
        frame.add(boxCinema);
        frame.add(labelCinema);
        frame.add(boxCinema);
        frame.add(tanggalLabel);
        frame.add(inputTanggalPembuatan);
        frame.add(labelFilm);
        
        frame.add(jamLabel);
        frame.add(jam1);
        frame.add(jam2);
        
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 5));

        JToggleButton[][] seatButtons = new JToggleButton[5][5];
        JLabel counterLabel = new JLabel("Seats Toggled: 0");
        final int toggleCount = 0;

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                JToggleButton seatButton = new JToggleButton(String.valueOf(row + 1) + (char) ('A' + col));
                seatButton.addItemListener(new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        if (e.getStateChange() == ItemEvent.SELECTED) {
                            // Set background to red when selected
                            seatButton.setBackground(Color.RED);
                            counterLabel.setText("Seats Toggled: " + (toggleCount+1));
                        } else {
                            // Set background to default (transparent) when deselected
                            seatButton.setBackground(null);
                            counterLabel.setText("Seats Toggled: " + toggleCount);
                        }
                        counterLabel.setText("Seats Toggled: " + toggleCount);
                    }
                });
                seatButtons[row][col] = seatButton;
                panel.add(seatButton);
            }
        }

        JPanel containerPanel = new JPanel(new GridBagLayout());
        containerPanel.add(panel, new GridBagConstraints());
        frame.add(containerPanel);
        frame.setVisible(true);
        frame.add(counterLabel);

        JPanel controlPanel = new JPanel();
        controlPanel.add(counterLabel);

        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(200, 620, 200, 30);
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle submit button action here
            }
        });

        frame.add(submitButton);
        frame.setLocationRelativeTo(null); 
        frame.add(containerPanel, BorderLayout.CENTER);
        frame.add(controlPanel, BorderLayout.SOUTH);
//        frame.add(frame);
        frame.setLayout(null);
        frame.setVisible(true);
    }
}

