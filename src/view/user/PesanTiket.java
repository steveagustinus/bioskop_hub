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

public class PesanTiket {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Cinema Booking App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 900);

//        JPanel frame = new JPanel();
//        panel.setLayout(new GridLayout(5, 2));

        JLabel pilihKota = new JLabel("Pilih Kota:");
        JComboBox<String> listKota = new JComboBox<>(new String[]{"Jakarta", "Bandung"});
        pilihKota.setBounds(10, 15, 200, 30);
        listKota.setBounds(170, 15, 200, 30);
        
        JLabel pilihCabang = new JLabel("Pilih Cabang:");
        JComboBox<String> listCabang = new JComboBox<>(new String[]{"Mall Istana Plaza", "Mall Festival Citylink"});
        pilihCabang.setBounds(10, 50, 200, 30);
        listCabang.setBounds(170, 50, 200, 30);
        
        JLabel tanggalLabel = new JLabel("Pilih Tanggal:");
        SqlDateModel dateModel2 = new SqlDateModel();
        JDatePanelImpl datePanel2 = new JDatePanelImpl(dateModel2);
        JDatePickerImpl inputTanggalPembuatan = new JDatePickerImpl(datePanel2);
        tanggalLabel.setBounds(10, 85, 200, 30);
        inputTanggalPembuatan.setBounds(170, 85, 200, 30);
        
        JLabel filmLabel = new JLabel("Pilih Film:");
        JRadioButton film1 = new JRadioButton("Iron Man");
        JRadioButton film2 = new JRadioButton("Iron Man 2");
        filmLabel.setBounds(10, 155, 200, 30);
        film1.setBounds(170, 155, 100, 30);
        film2.setBounds(270, 155, 100, 30);
        
        JLabel jamLabel = new JLabel("Pilih Jam Tayang:");
        JRadioButton jam1 = new JRadioButton("08.00");
        JRadioButton jam2 = new JRadioButton("10.00");

        ButtonGroup filmGroup = new ButtonGroup();
        filmGroup.add(film1);
        filmGroup.add(film2);
        filmGroup.add(jam1);
        filmGroup.add(jam2);

        frame.add(pilihKota);
        frame.add(listKota);
        frame.add(pilihCabang);
        frame.add(listCabang);
        frame.add(tanggalLabel);
        frame.add(inputTanggalPembuatan);
        frame.add(filmLabel);
        frame.add(film1);
        frame.add(film2);
        frame.add(jamLabel);
        frame.add(jam1);
        frame.add(jam2);
        
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 5));

       // Create an array of toggleable seat buttons
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

