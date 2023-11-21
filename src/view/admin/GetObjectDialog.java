package src.view.admin;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
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
import src.controller.ExceptionLogger;
import src.model.movie.Movie;

public class GetObjectDialog {
    private static Controller controller = new Controller();

    public static class GetMovieDialog extends JDialog {
        private String output = "";

        private int ownerX = 0;
        private int ownerY = 0;

        public GetMovieDialog(Window owner) {
            super(owner, ModalityType.DOCUMENT_MODAL);
            
            ownerX = owner.getX();
            ownerY = owner.getY();
            initializeComponent();
        }

        private void initializeComponent() {
            this.setLocation(ownerX + 30, ownerY + 30);
            this.setLayout(null);
            this.setSize(500, 500);
            
            JLabel labelSearchMovie = new JLabel("Cari film: ");
            labelSearchMovie.setSize(100, 20);
            labelSearchMovie.setLocation(5, 5);

            JTextField fieldSearchMovie = new JTextField();
            fieldSearchMovie.setLocation(5, 30);
            fieldSearchMovie.setSize(this.getWidth() - 10, 30);
            fieldSearchMovie.setFont(new Font("Dialog", Font.PLAIN, 20));


            fieldSearchMovie.getDocument().addDocumentListener(new DocumentListener()
            {
                @Override
                public void changedUpdate(DocumentEvent e) {
                    rearrangeMovie(controller.searchMovie(fieldSearchMovie.getText(), 100));
                }

                @Override
                public void insertUpdate(DocumentEvent e) {
                    rearrangeMovie(controller.searchMovie(fieldSearchMovie.getText(), 100));
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    rearrangeMovie(controller.searchMovie(fieldSearchMovie.getText(), 100));
                }
            });

            this.add(labelSearchMovie);
            this.add(fieldSearchMovie);

            rearrangeMovie(controller.searchMovie(fieldSearchMovie.getText(), 100));
        }

        private void rearrangeMovie(Movie[] movies) {
            for (Component comp : this.getContentPane().getComponents()) {
                if (comp instanceof JPanel) {
                    if (comp.getName() == null) { continue; }
                    if (comp.getName().contains("panel_")) {
                        JPanel panel = (JPanel) comp;
                        panel.setBackground(new Color(238, 238, 238));
                        panel.setVisible(false);
                    }
                }
            }

            if (movies == null) { return; }

            JPanel[] panels = new JPanel[movies.length];

            int movieCounter = 0;

            for (Component comp : this.getContentPane().getComponents()) {
                if (movieCounter == movies.length) { break; }

                if (comp instanceof JPanel) {
                    if (comp.getName() == null) { continue; }
                    if (comp.getName().equals("panel_" + movieCounter)) {
                        JPanel panel = (JPanel) comp;
                        panel.setVisible(true);
                        panel.setBackground(Color.GREEN);
                        panels[movieCounter] = panel;

                        for (Component panelComp : panel.getComponents()) {
                            if (panelComp.getName().equals("label_movie")) {
                                ((JLabel) panelComp).setText(movies[movieCounter].getJudul());
                            }

                            if (panelComp.getName().contains("button_fotoMovie_")) {
                                try {
                                    Image image = ImageIO.read(movies[movieCounter].getFotoMovie());
                                    ((JButton) panelComp).setIcon(new ImageIcon(
                                        new ImageIcon(image)
                                        .getImage()
                                        .getScaledInstance(100, 160, java.awt.Image.SCALE_SMOOTH)
                                    ));
                                } catch (Exception ex) {
                                    new ExceptionLogger(ex.getMessage());
                                }
                                panelComp.setName("button_fotoMovie_" + movies[movieCounter].getIdMovie());
                            }
                        }
                        
                        movieCounter++;
                    }
                }
            }

            for (int i = movieCounter; i < movies.length; i++) {
                JPanel panel = new JPanel();
                panel.setName("panel_" + i);
                panel.setLayout(null);
                panel.setSize(100, 180);

                if (i == 0) {
                    panel.setLocation(5, 70);
                } else {
                    panel.setLocation(
                        panels[i - 1].getX() + panels[i - 1].getWidth() + 5,
                        panels[i - 1].getY()
                    );

                    if (panel.getX() + panel.getWidth() + 5 > this.getWidth()) {
                        panel.setLocation(
                            5,
                            panels[i - 1].getY() + panels[i - 1].getHeight() + 5
                        );
                    }
                }

                panel.setBackground(Color.GREEN);
                panels[i] = panel;

                JLabel movieLabel = new JLabel(movies[i].getJudul());
                movieLabel.setName("label_movie");
                movieLabel.setSize(panel.getWidth(), 20);
                movieLabel.setLocation(0, 160);

                movieLabel.setText(movies[i].getJudul());

                panel.add(movieLabel);

                JButton buttonDisplayFoto = new JButton();
                buttonDisplayFoto.setName("button_fotoMovie_" + movies[i].getIdMovie());
                buttonDisplayFoto.setSize(100, 160);
                buttonDisplayFoto.setLocation(0, 0);

                try {
                    Image image = ImageIO.read(movies[i].getFotoMovie());
                    buttonDisplayFoto.setIcon(new ImageIcon(
                        new ImageIcon(image)
                        .getImage()
                        .getScaledInstance(100, 160,java.awt.Image.SCALE_SMOOTH)
                    ));
                } catch (Exception ex) {
                    new ExceptionLogger(ex.getMessage());
                }

                buttonDisplayFoto.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        output = ((JButton) e.getSource()).getName().replace("button_fotoMovie_", "");
                        getInput();
                    }
                    
                });

                panel.add(buttonDisplayFoto);

                this.add(panel);
            }
        }
    
        private String getInput() {
            this.dispose();
            return output;
        }
    }

    public static String getMovie(Window owner) {
        GetMovieDialog dialog = new GetMovieDialog(owner);
        dialog.setVisible(true);

        return dialog.getInput();
    }

    public static String getStudio(Window owner) {
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

        int result = JOptionPane.showConfirmDialog(owner, inputs, "Pilih Studio", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
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
