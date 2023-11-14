package src.view.admin.manage_fnb;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import src.controller.Controller;
import src.view.admin.MainMenuScreen;

public class DeleteFnBScreen {
    public DeleteFnBScreen(){
        Controller controller = new Controller();
        JFrame frame = new JFrame();
        frame.setTitle("Delete FnB");
        frame.setSize(400,200);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4,1));
        JLabel namaFnB = new JLabel("Nama-nama Food and Beverages : ");
        JLabel namaKota = new JLabel("Kota : ");
        JLabel cinema = new JLabel("Cinema : ");
        JButton submitButton = new JButton("Delete FnB");

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

        //FnB
        panel.add(namaFnB);
        String [] namaFoodAndBevs = controller.listFNB();
        JComboBox foodsnbevs = new JComboBox<>(namaFoodAndBevs);
        panel.add(foodsnbevs);

        panel.add(new JLabel());
        panel.add(submitButton);

        frame.add(panel);
        frame.setVisible(true);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              int input = JOptionPane.showConfirmDialog(null,"Apakah anda yakin untuk menghapus FnB : "+foodsnbevs.getSelectedItem().toString()+" ?");
                if(input == 0){
                    String data = foodsnbevs.getSelectedItem().toString();
                    String konfirmasi = controller.deleteFnB(data);
                    JOptionPane.showMessageDialog(null,konfirmasi);
                    frame.dispose();


                }else if (input==1){

                }else {
                    frame.dispose();
                    new MainMenuScreen();
                }
            }
        });

    }

    //test area
    public static void main(String[] args) {
        new DeleteFnBScreen();
    }
    //test area
}
