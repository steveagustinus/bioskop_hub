package src.view.user;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import src.controller.Controller;
import src.controller.ExceptionLogger;
import src.model.Jadwal;
import src.model.movie.Movie;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class PesanTiket extends JDialog {
    private String fontFamily = "Dialog";

    private Controller controller = new Controller();

    public static void main(String[] args) {
        new PesanTiket(null);
    }
    public PesanTiket(Window owner) {
        super(owner, ModalityType.DOCUMENT_MODAL);
        this.setTitle("Pesan Tiket");
        this.setSize(1000, 1000);
        this.setLayout(null);
        this.setLocationRelativeTo(owner);

        initializeComponent();

        showPesanTiket();
    }

    public void showPesanTiket() {
        this.setVisible(true);
    }

    public void initializeComponent() {
        controller.fetchData(); // PLEASE DELETE THIS LINE
        JLabel labelHeader = new JLabel("Pesan Tiket");
        labelHeader.setSize(this.getWidth(), 50);
        labelHeader.setLocation(0, 0);
        labelHeader.setFont(new Font(fontFamily, Font.BOLD, 30));
        labelHeader.setHorizontalAlignment(SwingConstants.CENTER);
        labelHeader.setVerticalAlignment(SwingConstants.CENTER);

        JPanel panelCinema = new JPanel();
        panelCinema.setSize(this.getWidth() - 40, 150);
        panelCinema.setLocation(20, 50);
        panelCinema.setLayout(null);
        panelCinema.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JLabel panelCinemaTitle = new JLabel("Pilih cinema di kota pilihan anda");
        panelCinemaTitle.setLocation(1, 1);
        panelCinemaTitle.setSize(panelCinema.getWidth(), 30);
        panelCinemaTitle.setFont(new Font(fontFamily, Font.BOLD, 25));
        panelCinemaTitle.setOpaque(true);
        panelCinemaTitle.setBackground(Color.LIGHT_GRAY);

        int fieldWidth = (panelCinema.getWidth() - 60) / 2;
        int fieldHeight = (panelCinema.getHeight() - 75) / 2;

        JLabel labelKota = new JLabel("Pilih Kota: ");
        labelKota.setSize(fieldWidth, fieldHeight);
        labelKota.setLocation(20, 55);
        labelKota.setFont(new Font(fontFamily, Font.PLAIN, 20));

        JComboBox<String> fieldKota = new JComboBox<String>(controller.listKota());
        fieldKota.setSize(fieldWidth, fieldHeight);
        fieldKota.setLocation(
            labelKota.getX(),
            labelKota.getY() + labelKota.getHeight() + 5
        );
        fieldKota.setFont(new Font(fontFamily, Font.PLAIN, 20));
        
        JLabel labelCinema = new JLabel("Pilih Cinema: ");
        labelCinema.setSize(fieldWidth, fieldHeight);
        labelCinema.setLocation(
            labelKota.getX() + labelKota.getWidth() + 20,
            labelKota.getY()
        );
        labelCinema.setFont(new Font(fontFamily, Font.PLAIN, 20));
        
        JComboBox<String> fieldCinema = new JComboBox<String>(controller.listCinema((String) fieldKota.getSelectedItem()));
        fieldCinema.setSize(fieldWidth, fieldHeight);
        fieldCinema.setLocation(
            labelCinema.getX(),
            labelCinema.getY() + labelCinema.getHeight() + 5
        );
        fieldCinema.setFont(new Font(fontFamily, Font.PLAIN, 20));

        JPanel panelFilm = new JPanel();
        panelFilm.setName("mainpanel_movie");
        panelFilm.setSize(this.getWidth() - 40, 400);
        panelFilm.setLocation(
            panelCinema.getX(),
            panelCinema.getY() + panelCinema.getHeight() + 20
        );
        panelFilm.setLayout(null);
        panelFilm.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        fieldKota.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                fieldCinema.removeAllItems();
                
                String[] listCinema = controller.listCinema((String) fieldKota.getSelectedItem());
                for (String cinema : listCinema) {
                    fieldCinema.addItem(cinema);
                }
            }
                
        });

        fieldCinema.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                showFilm((String) fieldCinema.getSelectedItem());
            }
            
        });

        panelCinema.add(panelCinemaTitle);
        panelCinema.add(labelKota);
        panelCinema.add(fieldKota);
        panelCinema.add(labelCinema);
        panelCinema.add(fieldCinema);

        this.add(labelHeader);
        this.add(panelCinema);
        this.add(panelFilm);
    }

    private void showFilm(String idCinema) {
        Jadwal[] listJadwal = controller.getJadwalByTimeRange(
            idCinema,
            LocalDate.of(2023, 11, 14), //LocalDate.now(),
            LocalDate.of(2023, 11, 17) //LocalDate.now().plusWeeks(1)
        );
        
        Movie[] movies = controller.extractMoviesFromListJadwal(listJadwal);
        
        // Search for Movie Panel
        JPanel panelMovie = null;
        for (Component comp : this.getContentPane().getComponents()) {
            if (comp instanceof JPanel) {
                if (comp.getName() == null) { continue; }
                if (comp.getName().equals("mainpanel_movie")) {
                    panelMovie = (JPanel) comp;
                    break;
                }
            }
        }

        // Hide all movie panels
        for (Component comp : panelMovie.getComponents()) {
            if (comp instanceof JPanel) {
                if (comp.getName() == null) { continue; }
                if (comp.getName().contains("panel_movie_")) {
                    JPanel panel = (JPanel) comp;
                    panel.setBackground(new Color(238, 238, 238));
                    panel.setVisible(false);
                }
            }
        }

        if (movies == null) { return; }

        JPanel[] panels = new JPanel[movies.length];

        int movieCounter = 0;

        for (Component comp : panelMovie.getComponents()) {
            if (movieCounter == movies.length) { break; }

            if (comp instanceof JPanel) {
                if (comp.getName() == null) { continue; }
                if (comp.getName().equals("panel_movie_" + movieCounter)) {
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
                                    .getScaledInstance(200, 320, java.awt.Image.SCALE_SMOOTH)
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
        
        for (int i = 0; i < movies.length; i++) {
            JPanel moviePanel = new JPanel();
            moviePanel.setName("panel_movie_" + i);
            moviePanel.setLayout(null);
            moviePanel.setSize(200, 360);

            if (i == 0) {
                moviePanel.setLocation(5, 20);
            } else {
                moviePanel.setLocation(
                    panels[i - 1].getX() + panels[i - 1].getWidth() + 5,
                    panels[i - 1].getY()
                );

                if (moviePanel.getX() + moviePanel.getWidth() + 5 > this.getWidth()) {
                    moviePanel.setLocation(
                        5,
                        panels[i - 1].getY() + panels[i - 1].getHeight() + 5
                    );
                }
            }

            moviePanel.setBackground(Color.magenta);
            panels[i] = moviePanel;

            JLabel movieLabel = new JLabel(movies[i].getJudul());
            movieLabel.setName("label_movie");
            movieLabel.setSize(moviePanel.getWidth(), 40);
            movieLabel.setLocation(0, 320);
            movieLabel.setFont(new Font(fontFamily, Font.BOLD, 20));
            movieLabel.setVerticalAlignment(SwingConstants.CENTER);
            movieLabel.setHorizontalAlignment(SwingConstants.CENTER);

            movieLabel.setText(movies[i].getJudul());

            moviePanel.add(movieLabel);

            JButton buttonDisplayFoto = new JButton();
            buttonDisplayFoto.setName("button_fotoMovie_" + movies[i].getIdMovie());
            buttonDisplayFoto.setSize(200, 320);
            buttonDisplayFoto.setLocation(0, 0);

            try {
                Image image = ImageIO.read(movies[i].getFotoMovie());
                buttonDisplayFoto.setIcon(new ImageIcon(
                    new ImageIcon(image)
                    .getImage()
                    .getScaledInstance(200, 320, java.awt.Image.SCALE_SMOOTH)
                ));
            } catch (Exception ex) {
                new ExceptionLogger(ex.getMessage());
            }

            buttonDisplayFoto.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    // output = ((JButton) e.getSource()).getName().replace("button_fotoMovie_", "");
                    // getInput();
                }
                
            });

            moviePanel.add(buttonDisplayFoto);

            panelMovie.add(moviePanel);
        }
    }

    public void close() {
        this.setVisible(false);
        this.dispose();
    }
}

