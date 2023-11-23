package src.view.user;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import src.controller.Controller;
import src.controller.ExceptionLogger;
import src.model.Jadwal;
import src.model.movie.Movie;
import src.model.seat.Seat;
import src.model.seat.SeatStatusInterface;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

public class PesanTiket extends JDialog {
    private Jadwal[] listJadwal = null;
    private Movie[] listMovie = null;
    private ArrayList<String> selectedSeatIds = new ArrayList<String>();

    private String fontFamily = "Dialog";

    private JButton buttonOrder;

    private Controller controller = new Controller();

    public static void main(String[] args) {
        new PesanTiket(null);
    }

    public PesanTiket(Window owner) {
        super(owner, ModalityType.DOCUMENT_MODAL);
        this.setTitle("Pesan Tiket");
        this.setSize(1280, 950);
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
        panelCinema.setSize(this.getWidth() - 40, 160);
        panelCinema.setLocation(20, 50);
        panelCinema.setLayout(null);
        panelCinema.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JLabel panelCinemaTitle = new JLabel("Pilih cinema di kota pilihan anda");
        panelCinemaTitle.setLocation(1, 1);
        panelCinemaTitle.setSize(panelCinema.getWidth() - 2, 30);
        panelCinemaTitle.setFont(new Font(fontFamily, Font.BOLD, 20));
        panelCinemaTitle.setOpaque(true);
        panelCinemaTitle.setBackground(Color.LIGHT_GRAY);
        panelCinemaTitle.setHorizontalAlignment(SwingConstants.CENTER);
        panelCinemaTitle.setVerticalAlignment(SwingConstants.CENTER);

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
        fieldKota.setSelectedIndex(-1);
        
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
        panelFilm.setSize(this.getWidth() - 40, 310);
        panelFilm.setLocation(
            panelCinema.getX(),
            panelCinema.getY() + panelCinema.getHeight() + 20
        );
        panelFilm.setLayout(null);
        panelFilm.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panelFilm.setVisible(false);

        JLabel panelFilmTitle = new JLabel("Pilih film");
        panelFilmTitle.setLocation(1, 1);
        panelFilmTitle.setSize(panelFilm.getWidth() - 2, 30);
        panelFilmTitle.setFont(new Font(fontFamily, Font.BOLD, 20));
        panelFilmTitle.setOpaque(true);
        panelFilmTitle.setBackground(Color.LIGHT_GRAY);
        panelFilmTitle.setHorizontalAlignment(SwingConstants.CENTER);
        panelFilmTitle.setVerticalAlignment(SwingConstants.CENTER);

        JPanel panelJadwal = new JPanel();
        panelJadwal.setName("mainpanel_jadwal");
        panelJadwal.setSize(
            (this.getWidth() - 55) / 2 - 200,
            300
        );
        panelJadwal.setLocation(
            panelFilm.getX(),
            panelFilm.getY() + panelFilm.getHeight() + 20
        );
        panelJadwal.setLayout(null);
        panelJadwal.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panelJadwal.setVisible(false);
        
        JLabel panelJadwalTitle = new JLabel("Pilih showtime");
        panelJadwalTitle.setLocation(1, 1);
        panelJadwalTitle.setSize(panelJadwal.getWidth() - 2, 30);
        panelJadwalTitle.setFont(new Font(fontFamily, Font.BOLD, 20));
        panelJadwalTitle.setOpaque(true);
        panelJadwalTitle.setBackground(Color.LIGHT_GRAY);
        panelJadwalTitle.setHorizontalAlignment(SwingConstants.CENTER);
        panelJadwalTitle.setVerticalAlignment(SwingConstants.CENTER);

        JPanel panelSeat = new JPanel();
        panelSeat.setName("mainpanel_seat");
        panelSeat.setSize(
            (this.getWidth() - 55) / 2 + 200,
            300
        );
        panelSeat.setLocation(
            panelJadwal.getX() + panelJadwal.getWidth() + 15,
            panelJadwal.getY()
        );
        panelSeat.setLayout(null);
        panelSeat.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panelSeat.setVisible(false);
        
        JLabel panelSeatTitle = new JLabel("Pilih kursi");
        panelSeatTitle.setLocation(1, 1);
        panelSeatTitle.setSize(panelSeat.getWidth() - 2, 30);
        panelSeatTitle.setFont(new Font(fontFamily, Font.BOLD, 20));
        panelSeatTitle.setOpaque(true);
        panelSeatTitle.setBackground(Color.LIGHT_GRAY);
        panelSeatTitle.setHorizontalAlignment(SwingConstants.CENTER);
        panelSeatTitle.setVerticalAlignment(SwingConstants.CENTER);

        JLabel labelSelectedSeatsInfo = new JLabel();
        labelSelectedSeatsInfo.setName("label_selectedseatsinfo");
        labelSelectedSeatsInfo.setSize(panelSeat.getWidth() - 2, 30);
        labelSelectedSeatsInfo.setLocation(1, panelSeat.getHeight() - labelSelectedSeatsInfo.getHeight() - 1);
        labelSelectedSeatsInfo.setOpaque(true);
        labelSelectedSeatsInfo.setBackground(new Color(255, 240, 200));

        buttonOrder = new JButton("Go to Payment");
        buttonOrder.setSize(300, 50);
        buttonOrder.setLocation(
            panelSeat.getX() + panelSeat.getWidth() - buttonOrder.getWidth(),
            panelSeat.getY() + panelSeat.getHeight() + 5
        );
        buttonOrder.setOpaque(true);
        buttonOrder.setBackground(Color.WHITE);
        buttonOrder.setFont(new Font(fontFamily, Font.BOLD, 20));
        buttonOrder.setFocusPainted(false);
        buttonOrder.setVisible(false);

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

        panelFilm.add(panelFilmTitle);

        panelJadwal.add(panelJadwalTitle);

        panelSeat.add(panelSeatTitle);
        panelSeat.add(labelSelectedSeatsInfo);

        this.add(labelHeader);
        this.add(panelCinema);
        this.add(panelFilm);
        this.add(panelJadwal);
        this.add(panelSeat);
        this.add(buttonOrder);
    }

    private void showFilm(String idCinema) {
        buttonOrder.setVisible(false);

        listJadwal = controller.getJadwalByTimeRange(
            idCinema,
            LocalDate.of(2023, 11, 14), //LocalDate.now(),
            LocalDate.of(2023, 11, 17) //LocalDate.now().plusWeeks(1)
        );
        
        listMovie = controller.extractMoviesFromListJadwal(listJadwal);
        
        // Search for Movie Panel, and hide Seat Panel
        JPanel panelMovie = null;
        for (Component comp : this.getContentPane().getComponents()) {
            if (comp instanceof JPanel) {
                if (comp.getName() == null) { continue; }
                if (comp.getName().equals("mainpanel_movie")) {
                    panelMovie = (JPanel) comp;
                    panelMovie.setVisible(true);
                    continue;
                }

                if (comp.getName().equals("mainpanel_jadwal")) {
                    comp.setVisible(false);
                    continue;
                }

                if (comp.getName().equals("mainpanel_seat")) {
                    comp.setVisible(false);
                    continue;
                }
            }
        }

        for (Component comp : panelMovie.getComponents()) {
            if (comp instanceof JPanel) {
                if (comp.getName() == null) { continue; }
                if (comp.getName().contains("panel_movie_")) {
                    panelMovie.remove(comp);
                }
            }
        }

        panelMovie.revalidate();
        panelMovie.repaint();

        if (listMovie == null) { return; }

        JPanel[] panels = new JPanel[listMovie.length];
        
        for (int i = 0; i < listMovie.length; i++) {
            JPanel moviePanel = new JPanel();
            moviePanel.setName("panel_movie_" + i);
            moviePanel.setLayout(null);
            moviePanel.setSize(150, 270);

            if (i == 0) {
                moviePanel.setLocation(5, 35);
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

            JLabel movieLabel = new JLabel(listMovie[i].getJudul());
            movieLabel.setName("label_movie");
            movieLabel.setSize(moviePanel.getWidth(), 30);
            movieLabel.setLocation(0, 240);
            movieLabel.setFont(new Font(fontFamily, Font.BOLD, 15));
            movieLabel.setVerticalAlignment(SwingConstants.CENTER);
            movieLabel.setHorizontalAlignment(SwingConstants.CENTER);

            movieLabel.setText(listMovie[i].getJudul());

            moviePanel.add(movieLabel);

            JButton buttonDisplayFoto = new JButton();
            buttonDisplayFoto.setName("button_fotoMovie_" + listMovie[i].getIdMovie());
            buttonDisplayFoto.setSize(150, 240);
            buttonDisplayFoto.setLocation(0, 0);

            try {
                Image image = ImageIO.read(listMovie[i].getFotoMovie());
                buttonDisplayFoto.setIcon(new ImageIcon(
                    new ImageIcon(image)
                    .getImage()
                    .getScaledInstance(150, 240, java.awt.Image.SCALE_SMOOTH)
                ));
            } catch (Exception ex) {
                new ExceptionLogger(ex.getMessage());
            }

            buttonDisplayFoto.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    String idMovie = ((JButton) buttonDisplayFoto).getName().replace("button_fotoMovie_", "");
                    showJadwal(idMovie);
                }
                
            });

            moviePanel.add(buttonDisplayFoto);

            panelMovie.add(moviePanel);
            panelMovie.revalidate();
            panelMovie.repaint();
        }
    }

    private void showJadwal(String idMovie) {
        buttonOrder.setVisible(false);
        // Search for Jadwal Panel
        JPanel panelJadwal = null;
        for (Component comp : this.getContentPane().getComponents()) {
            if (comp instanceof JPanel) {
                if (comp.getName() == null) { continue; }
                if (comp.getName().equals("mainpanel_jadwal")) {
                    panelJadwal = (JPanel) comp;
                    panelJadwal.setVisible(true);
                    continue;
                }

                if (comp.getName().equals("mainpanel_seat")) {
                    comp.setVisible(false);
                    continue;
                }
            }
        }

        // Delete JTable
        for (Component comp : panelJadwal.getComponents()) {
            if (comp instanceof JTable) {
                if (comp.getName() == null) { continue; }
                if (comp.getName().equals("table_jadwal")) {
                    panelJadwal.remove(comp);
                    panelJadwal.revalidate();
                    panelJadwal.repaint();
                    break;
                }
            }
        }

        if (listJadwal == null) { return; }

        String[] columnHeader = new String[] { "Showtime" };
        // String[] columnHeader = new String[] { "Waktu", "Film", "Kelas Studio", "Tipe Studio", "Harga" };
        Jadwal[] filteredJadwal = controller.filterJadwal(listJadwal, idMovie);
        String[][] rowData = controller.getJadwalData(filteredJadwal);

        JTable tableJadwal = new JTable(rowData, columnHeader) {
            public boolean isCellEditable(int row,int column) {
                return false;
            }  
        };

        tableJadwal.setName("table_jadwal");
        tableJadwal.setSize(panelJadwal.getWidth() - 2, panelJadwal.getHeight() - 33);
        tableJadwal.setLocation(1, 32);
        tableJadwal.setFont(new Font(fontFamily, Font.BOLD, 12));
        tableJadwal.setRowHeight(40);
        tableJadwal.setShowGrid(false);

        tableJadwal.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                String idJadwal = filteredJadwal[tableJadwal.getSelectedRow()].getIdJadwal();
                showSeat(idJadwal);
            }
            
        });

        panelJadwal.add(tableJadwal);
        panelJadwal.revalidate();
        panelJadwal.repaint();
    }

    private void showSeat(String idJadwal) {
        buttonOrder.setVisible(false);
        selectedSeatIds.clear();
        setLabelSelectedSeatsInfoLabel("Selected (0): ");

        // Search for Seat Panel
        JPanel panelSeat = null;
        for (Component comp : this.getContentPane().getComponents()) {
            if (comp instanceof JPanel) {
                if (comp.getName() == null) { continue; }
                if (comp.getName().equals("mainpanel_seat")) {
                    panelSeat = (JPanel) comp;
                    panelSeat.setVisible(true);
                    break;
                }
            }
        }

        // Delete Seats
        for (Component comp : panelSeat.getComponents()) {
            if (comp instanceof JButton) {
                if (comp.getName() == null) { continue; }
                if (comp.getName().contains("button_seat_")) {
                    panelSeat.remove(comp);
                }
            }
        }

        panelSeat.revalidate();
        panelSeat.repaint();

        Seat[][] seats = controller.getSeatFromJadwal(listJadwal, idJadwal);
        JButton[][] buttonSeat = new JButton[seats.length][seats[0].length];

        for (int i = 0; i < buttonSeat.length; i++) {
            for (int j = 0; j < buttonSeat[i].length; j++) {
                JButton button = new JButton(seats[i][j].getSeatCode());
                button.setName("button_seat_" + seats[i][j].getSeatCode());
                button.setSize(60, 25);
                if (seats[i][j].getSeatStatus() == SeatStatusInterface.TAKEN) {
                    button.setBackground(Color.RED);
                    button.setEnabled(false);
                } else {
                    button.setBackground(Color.WHITE);
                }

                button.setFont(new Font(fontFamily, Font.BOLD, 12));
                
                if (i == 0 && j == 0) {
                    button.setLocation(5, 35);
                } else if (j == 0) {
                    button.setLocation(
                        buttonSeat[i - 1][j].getX(),
                        buttonSeat[i - 1][j].getY() + buttonSeat[i - 1][j].getHeight()
                    );
                } else {
                     button.setLocation(
                        buttonSeat[i][j - 1].getX() + buttonSeat[i][j - 1].getWidth(),
                        buttonSeat[i][j - 1].getY()
                    );
                }

                button.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String currentSeatId = ((JButton) e.getSource()).getName().replace("button_seat_", "");
                        if (!selectedSeatIds.contains(currentSeatId)) {
                            selectedSeatIds.add(currentSeatId);
                            button.setBackground(Color.CYAN);
                        } else {
                            selectedSeatIds.remove(currentSeatId);
                            button.setBackground(Color.WHITE);
                        }

                        button.setFocusPainted(false);

                        Collections.sort(selectedSeatIds);

                        String info = "Selected (" + selectedSeatIds.size() +"): ";
                        for (int i = 0; i < selectedSeatIds.size(); i++) {
                            info += " " + selectedSeatIds.get(i) + " |";
                        }

                        setLabelSelectedSeatsInfoLabel(info.substring(0, info.length() - 1));

                        buttonOrder.setVisible(selectedSeatIds.size() != 0);
                    }
                    
                });

                buttonSeat[i][j] = button;
                panelSeat.add(button);
            }
        }

        panelSeat.revalidate();
        panelSeat.repaint();
    }

    private void setLabelSelectedSeatsInfoLabel(String str) {
        JPanel panelSeat = null;
        for (Component comp : this.getContentPane().getComponents()) {
            if (comp instanceof JPanel) {
                if (comp.getName() == null) { continue; }
                if (comp.getName().equals("mainpanel_seat")) {
                    panelSeat = (JPanel) comp;
                    break;
                }
            }
        }

        // Search for SelectedSeatInfo Label
        for (Component comp : panelSeat.getComponents()) {
            if (comp instanceof JLabel) {
                if (comp.getName() == null) { continue; }
                if (comp.getName().equals("label_selectedseatsinfo")) {
                    ((JLabel) comp).setText(str);
                    break;
                }
            }
        }
    }

    public void close() {
        this.setVisible(false);
        this.dispose();
    }
}

