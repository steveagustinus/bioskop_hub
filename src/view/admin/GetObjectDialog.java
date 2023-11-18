package src.view.admin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import src.controller.Controller;

public class GetObjectDialog {
    private static Controller controller = new Controller();

    public static String getCinema() {
        JComboBox<String> inputKota = new JComboBox<String>(controller.listKota());
        JComboBox<String> inputCinema = new JComboBox<String>(controller.listCinema((String) inputKota.getSelectedItem()));

        inputKota.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                inputCinema.removeAllItems();
                
                String[] listCinema = controller.listCinema((String) inputKota.getSelectedItem());
                for (String cinema : listCinema) {
                    inputCinema.addItem(cinema);
                }
            }
            
        });
        JComponent[] inputs = new JComponent[] {
            new JLabel("Pilih kota"), inputKota,
            new JLabel("Pilih cinema"), inputCinema
        };

        int result = JOptionPane.showConfirmDialog(null, inputs, "Pilih Cinema", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            return (String) inputCinema.getSelectedItem();
        }

        return "";
    }
}
