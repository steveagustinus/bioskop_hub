package src.view.admin;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Window;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import src.controller.Controller;
import src.model.movie.Movie;

public class GetObjectDialog {
    private static Controller controller = new Controller();

    private static void rearrangeMovie(Movie[] movies, JDialog dialog) {
        for (Component comp : dialog.getContentPane().getComponents()) {
            if (comp instanceof JPanel) {
                if (comp.getName() == null) { continue; }
                if (comp.getName().contains("panel_")) {
                    JPanel panel = (JPanel) comp;

                    for (Component panelComp : panel.getComponents()) {
                        if (panelComp.getName().equals("label_movie")) {
                            ((JLabel) panelComp).setText("");
                        }
                    }
                }
            }
        }

        if (movies == null) { return; }

        for (Movie movie : movies) {
            System.out.print(movie.getJudul() + ", ");
        }

        JPanel[] panels = new JPanel[movies.length];

        int movieCounter = 0;
        for (Component comp : dialog.getContentPane().getComponents()) {
            if (movieCounter >= movies.length - 1) { break; }
            if (comp instanceof JPanel) {
                if (comp.getName() == null) { continue; }
                if (comp.getName().equals("panel_" + movieCounter)) {
                    JPanel panel = (JPanel) comp;
                    panels[movieCounter] = panel;

                    for (Component panelComp : panel.getComponents()) {
                        if (panelComp.getName().equals("label_movie")) {
                            System.out.println("Here " + movies[movieCounter].getJudul());
                            ((JLabel) panelComp).setText(movies[movieCounter].getJudul());
                            movieCounter++;
                        }
                    }
                }
            }
        }

        for (int i = movieCounter; i < movies.length; i++) {
            JPanel panel = new JPanel();
            panel.setName("panel_" + i);
            panel.setLayout(null);
            panel.setSize(90, 160);

            if (i == 0) {
                panel.setLocation(5, 70);
            } else {
                panel.setLocation(
                    panels[i - 1].getX() + panels[i - 1].getWidth() + 5,
                    panels[i - 1].getY()
                );

                if (panel.getX() + panel.getWidth() + 5 > dialog.getWidth()) {
                    panel.setLocation(
                        5,
                        panels[i - 1].getY() + panels[i - 1].getHeight() + 5
                    );
                }
            }

            panel.setBackground(Color.CYAN);
            panels[i] = panel;

            JLabel movieLabel = new JLabel(movies[i].getJudul());
            movieLabel.setName("label_movie");
            movieLabel.setSize(panel.getWidth() - 10, 20);
            movieLabel.setLocation(5, 5);

            movieLabel.setText(movies[i].getJudul());
            panel.add(movieLabel);

            dialog.add(panel);
        }
    }

    public static String getMovie(Window owner) {
        JDialog dialog = new JDialog(owner, ModalityType.DOCUMENT_MODAL);
        dialog.setLocation(owner.getX() + 30, owner.getY() + 30);
        dialog.setLayout(null);
        dialog.setSize(1000, 1000);
        
        JLabel labelSearchMovie = new JLabel("Cari film: ");
        labelSearchMovie.setSize(100, 20);
        labelSearchMovie.setLocation(5, 5);

        JTextField fieldSearchMovie = new JTextField();
        fieldSearchMovie.setLocation(5, 30);
        fieldSearchMovie.setSize(dialog.getWidth() - 10, 30);
        fieldSearchMovie.setFont(new Font("Dialog", Font.PLAIN, 20));

        fieldSearchMovie.getDocument().addDocumentListener(new DocumentListener()
        {
            @Override
            public void changedUpdate(DocumentEvent e) {
                rearrangeMovie(controller.searchMovie(fieldSearchMovie.getText(), 100), dialog);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                rearrangeMovie(controller.searchMovie(fieldSearchMovie.getText(), 100), dialog);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                rearrangeMovie(controller.searchMovie(fieldSearchMovie.getText(), 100), dialog);
            }
        });

        dialog.add(labelSearchMovie);
        dialog.add(fieldSearchMovie);

        dialog.setVisible(true);

        return "";
    }

    public static String getStudio() {
        JComboBox<String> inputKota = new JComboBox<String>(controller.listKota());
        JComboBox<String> inputCinema = new JComboBox<String>(controller.listCinema((String) inputKota.getSelectedItem()));
        JComboBox<String> inputStudio = new JComboBox<String>(controller.listStudio((String) inputCinema.getSelectedItem()));
        
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

        inputCinema.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                inputStudio.removeAllItems();
                
                String[] listStudio = controller.listStudio((String) inputCinema.getSelectedItem());
                for (String studio : listStudio) {
                    inputStudio.addItem(studio);
                }
            }
            
        });

        JComponent[] inputs = new JComponent[] {
            new JLabel("Pilih kota"), inputKota,
            new JLabel("Pilih cinema"), inputCinema,
            new JLabel("Pilih studio"), inputStudio
        };

        int result = JOptionPane.showConfirmDialog(null, inputs, "Pilih Studio", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            return (String) inputStudio.getSelectedItem();
        }

        return "";
    }

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
