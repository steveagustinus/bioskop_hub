package src.view.user;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import src.controller.Controller;
import src.controller.ExceptionLogger;
import src.controller.OperationCode;
import src.controller.UserDataSingleton;
import src.model.Cinema;
import src.model.Jadwal;
import src.model.movie.Movie;
import src.model.seat.Seat;
import src.model.seat.SeatStatusInterface;
import src.model.studio.Studio;
import src.view.MainInterface;
import src.view.user.payment.PaymentPanel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;

public class PesanTiket extends JDialog implements MainInterface{
    public class OrderConfirmation extends JDialog implements MainInterface {

        public OrderConfirmation(Window owner) {
            super(owner, ModalityType.DOCUMENT_MODAL);
            this.setLocation(owner.getX(), owner.getY());
            this.setSize(500, 750);
            this.setLayout(null);
            this.getContentPane().setBackground(FRAME_BACKGROUND);

            initializeComponent();
        }

        public void showDialog() {
            this.setVisible(true);
        }

        public void close() {
            this.setVisible(false);
            this.dispose();
        }

        public void initializeComponent() {
            JLabel labelHeader = new JLabel("Order Confirmation");
            labelHeader.setSize(this.getWidth(), 50);
            labelHeader.setLocation(0, 0);
            labelHeader.setFont(new Font(FONTFAMILY, Font.BOLD, 30));
            labelHeader.setHorizontalAlignment(SwingConstants.CENTER);
            labelHeader.setVerticalAlignment(SwingConstants.CENTER);
            labelHeader.setForeground(TEXT_BACKGROUND);

            Jadwal jadwal = controller.getJadwalById(selectedJadwalId);
            Studio studio = controller.getStudioById(jadwal.getIdStudio());
            Cinema cinema = controller.getCinemaById(studio.getIdCinema());
            Movie movie = controller.getMovieById(jadwal.getIdMovie());
            Seat[] seats = controller.getSeatFromListSeatString(jadwal.getSeat(), selectedSeatIds);

            JLabel labelCinema = new JLabel(cinema.getNama() + " - " + cinema.getKota());
            labelCinema.setSize(this.getWidth() - 20, 30);
            labelCinema.setLocation(10, labelHeader.getY() + labelHeader.getHeight() + 20);
            labelCinema.setFont(new Font(FONTFAMILY, Font.BOLD, 20));
            labelCinema.setForeground(TEXT_BACKGROUND);
            
            JLabel labelCinemaInfo = new JLabel(cinema.getAlamat());
            labelCinemaInfo.setSize(labelCinema.getWidth(), 20);
            labelCinemaInfo.setLocation(
                labelCinema.getX(),
                labelCinema.getY() + labelCinema.getHeight() + 5
            );
            labelCinemaInfo.setFont(new Font(FONTFAMILY, Font.PLAIN, 15));
            labelCinemaInfo.setForeground(TEXT_BACKGROUND);

            JLabel labelStudio = new JLabel(studio.getIdStudio());
            labelStudio.setSize(this.getWidth() - 20, 25);
            labelStudio.setLocation(
                labelCinemaInfo.getX(),
                labelCinemaInfo.getY() + labelCinemaInfo.getHeight() + 15
            );
            labelStudio.setFont(new Font(FONTFAMILY, Font.BOLD, 18));
            labelStudio.setForeground(TEXT_BACKGROUND);

            JLabel labelStudioInfo = new JLabel(studio.getStudioClass() + " | " + controller.getStudioTypeString(studio.getStudioType()));
            labelStudioInfo.setSize(labelStudio.getWidth(), 20);
            labelStudioInfo.setLocation(
                labelStudio.getX(),
                labelStudio.getY() + labelStudio.getHeight() + 5
            );
            labelStudioInfo.setFont(new Font(FONTFAMILY, Font.PLAIN, 15));
            labelStudioInfo.setForeground(TEXT_BACKGROUND);

            JSeparator separator1 = new JSeparator();
            separator1.setOrientation(SwingConstants.HORIZONTAL);
            separator1.setSize(this.getWidth(), 5);
            separator1.setLocation(0, labelStudioInfo.getY() + labelStudioInfo.getHeight() + 5);
            separator1.setFont(new Font(FONTFAMILY, Font.BOLD, 20));
            separator1.setForeground(Color.WHITE);

            JLabel labelMovie = new JLabel(movie.getJudul());
            labelMovie.setSize(this.getWidth() - 20, 25);
            labelMovie.setLocation(labelStudioInfo.getX(), separator1.getY() + separator1.getHeight() + 5);
            labelMovie.setFont(new Font(FONTFAMILY, Font.BOLD, 20));
            labelMovie.setForeground(TEXT_BACKGROUND);

            JLabel labelMovieInfo = new JLabel(String.valueOf(movie.getDurasi()) + " min");
            labelMovieInfo.setSize(labelMovie.getWidth(), 15);
            labelMovieInfo.setLocation(labelMovie.getX(), labelMovie.getY() + labelMovie.getHeight() + 5);
            labelMovieInfo.setFont(new Font(FONTFAMILY, Font.PLAIN, 15));
            labelMovieInfo.setForeground(TEXT_BACKGROUND);

            JLabel labelShowtime = new JLabel(
                jadwal.getWaktu().format(DateTimeFormatter.ofPattern("hh:mm dd MMMM yyyy"))
            );
            labelShowtime.setSize(this.getWidth() - 20, 25);
            labelShowtime.setLocation(labelMovieInfo.getX(), labelMovieInfo.getY() + labelMovieInfo.getHeight() + 5);
            labelShowtime.setFont(new Font(FONTFAMILY, Font.BOLD, 18));
            labelShowtime.setForeground(TEXT_BACKGROUND);

            JLabel labelSeat = new JLabel("Kursi yang dipilih: ");
            labelSeat.setSize(this.getWidth() - 20, 20);
            labelSeat.setLocation(labelShowtime.getX(), labelShowtime.getY() + labelShowtime.getHeight() + 10);
            labelSeat.setFont(new Font(FONTFAMILY, Font.PLAIN, 18));
            labelSeat.setForeground(TEXT_BACKGROUND);

            String seatMessage = "";
            for (Seat seat : seats) {
                seatMessage += " " + seat.getSeatCode() + " |";
            }
            seatMessage = seatMessage.substring(0, seatMessage.length() - 1);

            JLabel labelSeat2 = new JLabel(seatMessage);
            labelSeat2.setSize(this.getWidth() - 20, 20);
            labelSeat2.setLocation(labelSeat.getX(), labelSeat.getY() + labelSeat.getHeight() + 5);
            labelSeat2.setFont(new Font(FONTFAMILY, Font.BOLD, 14));
            labelSeat2.setForeground(TEXT_BACKGROUND);

            JSeparator separator2 = new JSeparator();
            separator2.setOrientation(SwingConstants.HORIZONTAL);
            separator2.setSize(this.getWidth(), 5);
            separator2.setLocation(0, labelSeat2.getY() + labelSeat2.getHeight() + 5);
            separator2.setFont(new Font(FONTFAMILY, Font.BOLD, 20));
            separator2.setForeground(Color.BLACK);

            JLabel labelTotalBayar = new JLabel("Total bayar: ");
            labelTotalBayar.setSize(this.getWidth(), 26);
            labelTotalBayar.setLocation(labelSeat.getX(), separator2.getY() + separator2.getHeight() + 5);
            labelTotalBayar.setFont(new Font(FONTFAMILY, Font.BOLD, 25));
            labelTotalBayar.setForeground(TEXT_BACKGROUND);

            JLabel labelTotalBayar2 = new JLabel(
                seats.length + " x Rp. " +
                jadwal.getHarga() + " = Rp. " + 
                new DecimalFormat("###,###").format(controller.getTotalBayar(jadwal, seats))
            );
            labelTotalBayar2.setSize(this.getWidth(), 26);
            labelTotalBayar2.setLocation(labelTotalBayar.getX(), labelTotalBayar.getY() + labelTotalBayar.getHeight() + 5);
            labelTotalBayar2.setFont(new Font(FONTFAMILY, Font.BOLD, 25));
            labelTotalBayar2.setForeground(TEXT_BACKGROUND);

            JSeparator separator3 = new JSeparator();
            separator3.setOrientation(SwingConstants.HORIZONTAL);
            separator3.setSize(this.getWidth(), 5);
            separator3.setLocation(0, labelTotalBayar2.getY() + labelTotalBayar2.getHeight() + 5);
            separator3.setFont(new Font(FONTFAMILY, Font.BOLD, 20));
            separator3.setForeground(Color.BLACK);

            PaymentPanel panelPayment = new PaymentPanel(this.getWidth() - 20, 250);
            panelPayment.setLocation(labelTotalBayar.getX(), separator3.getY() + separator3.getHeight() + 10);

            JButton buttonPesan = new JButton("Pesan");
            buttonPesan.setSize(this.getWidth() / 3, 30);
            buttonPesan.setLocation(
                buttonPesan.getWidth(),
                panelPayment.getY() + panelPayment.getHeight() + 10
            );
            buttonPesan.setBackground(BUTTON_BACKGROUND);
            buttonPesan.setForeground(BUTTON_FOREGROUND);

            buttonPesan.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    int status = controller.pesanTiket(
                        String.valueOf(UserDataSingleton.getInstance().getId()),
                        jadwal,
                        seats,
                        panelPayment.getPaymentMethod()
                    );

                    if (status == OperationCode.PesanTiket.SUCCESS) {
                        JOptionPane.showMessageDialog(
                            null,
                            "Tiket berhasil dipesan!",
                            "Pesan tiket",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                        close();
                    } else {
                        if (status == OperationCode.PesanTiket.IDCUSTOMERNOTVALID) { 
                            JOptionPane.showMessageDialog(
                                null, "Harap melakukan login ulang!", "Pesan Tiket", JOptionPane.ERROR_MESSAGE
                            );
                        }
                        else if (status == OperationCode.PesanTiket.NOJADWALSELECTED) {
                            JOptionPane.showMessageDialog(
                                null, "Harap pilih jadwal tayang", "Pesan Tiket", JOptionPane.ERROR_MESSAGE
                            );
                        }
                        else if (status == OperationCode.PesanTiket.NOSEATSELECTED) {
                            JOptionPane.showMessageDialog(
                                null, "Harap pilih kursi yang ingin dipesan", "Pesan Tiket", JOptionPane.ERROR_MESSAGE
                            );
                        }
                        else if (status == OperationCode.PesanTiket.NOPAYMENTMETHOD) {
                            JOptionPane.showMessageDialog(
                                null, "Harap pilih metode pembayaran", "Pesan Tiket", JOptionPane.ERROR_MESSAGE
                            );
                        }
                        else if (status == OperationCode.PesanTiket.ANYEXCEPTION) {
                            JOptionPane.showMessageDialog(
                                null, "Terjadi kesalahan!", "Pesan Tiket", JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }
                }
                
            });
            this.add(labelHeader);
            this.add(labelCinema);
            this.add(labelCinemaInfo);
            this.add(labelStudio);
            this.add(labelStudioInfo);
            this.add(separator1);
            this.add(labelMovie);
            this.add(labelMovieInfo);
            this.add(labelShowtime);
            this.add(labelSeat);
            this.add(labelSeat2);
            this.add(separator2);
            this.add(labelTotalBayar);
            this.add(labelTotalBayar2);
            this.add(separator3);
            this.add(panelPayment);
            this.add(buttonPesan);
        }
    }

    private Jadwal[] listJadwal = null;
    private Movie[] listMovie = null;

    private String selectedJadwalId = null;
    private ArrayList<String> selectedSeatIds = new ArrayList<String>();

    private String FONTFAMILY = "Dialog";

    private JButton buttonOrder;

    private Controller controller = new Controller();

    public PesanTiket(Window owner) {
        super(owner, ModalityType.DOCUMENT_MODAL);
        this.setTitle("Pesan Tiket");
        this.setSize(1280, 950);
        this.setLayout(null);
        this.setLocationRelativeTo(owner);
        this.getContentPane().setBackground(FRAME_BACKGROUND);

        initializeComponent();

        showPesanTiket();
    }

    public void showPesanTiket() {
        this.setVisible(true);
    }

    private void initializeComponent() {
        JLabel labelHeader = new JLabel("Pesan Tiket");
        labelHeader.setSize(this.getWidth(), 50);
        labelHeader.setLocation(0, 0);
        labelHeader.setFont(new Font(FONTFAMILY, Font.BOLD, 30));
        labelHeader.setHorizontalAlignment(SwingConstants.CENTER);
        labelHeader.setVerticalAlignment(SwingConstants.CENTER);
        labelHeader.setForeground(TEXT_BACKGROUND);

        JPanel panelCinema = new JPanel();
        panelCinema.setSize(this.getWidth() - 40, 160);
        panelCinema.setLocation(20, 50);
        panelCinema.setLayout(null);
        panelCinema.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panelCinema.setBackground(FRAME_BACKGROUND);

        JLabel panelCinemaTitle = new JLabel("Pilih cinema di kota pilihan anda");
        panelCinemaTitle.setLocation(1, 1);
        panelCinemaTitle.setSize(panelCinema.getWidth() - 2, 30);
        panelCinemaTitle.setFont(new Font(FONTFAMILY, Font.BOLD, 20));
        panelCinemaTitle.setOpaque(true);
        panelCinemaTitle.setBackground(BUTTON_BACKGROUND);
        panelCinemaTitle.setHorizontalAlignment(SwingConstants.CENTER);
        panelCinemaTitle.setVerticalAlignment(SwingConstants.CENTER);
        panelCinemaTitle.setForeground(FRAME_BACKGROUND);

        int fieldWidth = (panelCinema.getWidth() - 60) / 2;
        int fieldHeight = (panelCinema.getHeight() - 75) / 2;

        JLabel labelKota = new JLabel("Pilih Kota: ");
        labelKota.setSize(fieldWidth, fieldHeight);
        labelKota.setLocation(20, 55);
        labelKota.setFont(new Font(FONTFAMILY, Font.PLAIN, 20));
        labelKota.setForeground(TEXT_BACKGROUND);

        JComboBox<String> fieldKota = new JComboBox<String>(controller.listKota());
        fieldKota.setSize(fieldWidth, fieldHeight);
        fieldKota.setLocation(
            labelKota.getX(),
            labelKota.getY() + labelKota.getHeight() + 5
        );
        fieldKota.setFont(new Font(FONTFAMILY, Font.PLAIN, 20));
        fieldKota.setSelectedIndex(-1);
        fieldKota.setBackground(FRAME_BACKGROUND);
        fieldKota.setForeground(TEXT_BACKGROUND);
        
        JLabel labelCinema = new JLabel("Pilih Cinema: ");
        labelCinema.setSize(fieldWidth, fieldHeight);
        labelCinema.setLocation(
            labelKota.getX() + labelKota.getWidth() + 20,
            labelKota.getY()
        );
        labelCinema.setFont(new Font(FONTFAMILY, Font.PLAIN, 20));
        labelCinema.setForeground(TEXT_BACKGROUND);
        
        JComboBox<String> fieldCinema = new JComboBox<String>(controller.listCinema((String) fieldKota.getSelectedItem()));
        fieldCinema.setSize(fieldWidth, fieldHeight);
        fieldCinema.setLocation(
            labelCinema.getX(),
            labelCinema.getY() + labelCinema.getHeight() + 5
        );
        fieldCinema.setFont(new Font(FONTFAMILY, Font.PLAIN, 20));
        fieldCinema.setBackground(FRAME_BACKGROUND);
        fieldCinema.setForeground(TEXT_BACKGROUND);

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
        panelFilm.setBackground(FRAME_BACKGROUND);

        JLabel panelFilmTitle = new JLabel("Pilih film");
        panelFilmTitle.setLocation(1, 1);
        panelFilmTitle.setSize(panelFilm.getWidth() - 2, 30);
        panelFilmTitle.setFont(new Font(FONTFAMILY, Font.BOLD, 20));
        panelFilmTitle.setOpaque(true);
        panelFilmTitle.setBackground(BUTTON_BACKGROUND);
        panelFilmTitle.setHorizontalAlignment(SwingConstants.CENTER);
        panelFilmTitle.setVerticalAlignment(SwingConstants.CENTER);
        panelFilmTitle.setForeground(FRAME_BACKGROUND);

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
        panelJadwal.setBackground(FRAME_BACKGROUND);
        
        JLabel panelJadwalTitle = new JLabel("Pilih showtime");
        panelJadwalTitle.setLocation(1, 1);
        panelJadwalTitle.setSize(panelJadwal.getWidth() - 2, 30);
        panelJadwalTitle.setFont(new Font(FONTFAMILY, Font.BOLD, 20));
        panelJadwalTitle.setOpaque(true);
        panelJadwalTitle.setBackground(BUTTON_BACKGROUND);
        panelJadwalTitle.setHorizontalAlignment(SwingConstants.CENTER);
        panelJadwalTitle.setVerticalAlignment(SwingConstants.CENTER);
        panelJadwalTitle.setForeground(FRAME_BACKGROUND);

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
        panelSeat.setBackground(FRAME_BACKGROUND);
        
        JLabel panelSeatTitle = new JLabel("Pilih kursi");
        panelSeatTitle.setLocation(1, 1);
        panelSeatTitle.setSize(panelSeat.getWidth() - 2, 30);
        panelSeatTitle.setFont(new Font(FONTFAMILY, Font.BOLD, 20));
        panelSeatTitle.setOpaque(true);
        panelSeatTitle.setBackground(BUTTON_BACKGROUND);
        panelSeatTitle.setHorizontalAlignment(SwingConstants.CENTER);
        panelSeatTitle.setVerticalAlignment(SwingConstants.CENTER);
        panelSeatTitle.setForeground(FRAME_BACKGROUND);

        JLabel labelSelectedSeatsInfo = new JLabel();
        labelSelectedSeatsInfo.setName("label_selectedseatsinfo");
        labelSelectedSeatsInfo.setSize(panelSeat.getWidth() - 2, 30);
        labelSelectedSeatsInfo.setLocation(1, panelSeat.getHeight() - labelSelectedSeatsInfo.getHeight() - 1);
        labelSelectedSeatsInfo.setOpaque(true);
        labelSelectedSeatsInfo.setBackground(BUTTON_BACKGROUND);

        buttonOrder = new JButton("Go to Payment");
        buttonOrder.setSize(300, 50);
        buttonOrder.setLocation(
            panelSeat.getX() + panelSeat.getWidth() - buttonOrder.getWidth(),
            panelSeat.getY() + panelSeat.getHeight() + 5
        );
        buttonOrder.setOpaque(true);
        buttonOrder.setBackground(BUTTON_BACKGROUND);
        buttonOrder.setForeground(BUTTON_FOREGROUND);
        buttonOrder.setFont(new Font(FONTFAMILY, Font.BOLD, 20));
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

        buttonOrder.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                orderConfirmationClick();
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
            LocalDate.of(2023, 12, 1), //LocalDate.now(),
            LocalDate.of(2023, 12, 7) //LocalDate.now().plusWeeks(1)
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

            moviePanel.setBackground(BUTTON_BACKGROUND);
            panels[i] = moviePanel;

            JLabel movieLabel = new JLabel(listMovie[i].getJudul());
            movieLabel.setName("label_movie");
            movieLabel.setSize(moviePanel.getWidth(), 30);
            movieLabel.setLocation(0, 240);
            movieLabel.setFont(new Font(FONTFAMILY, Font.BOLD, 15));
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
        tableJadwal.setBackground(FRAME_BACKGROUND);
        tableJadwal.setForeground(TEXT_BACKGROUND);
        tableJadwal.setFont(new Font(FONTFAMILY, Font.BOLD, 12));
        tableJadwal.setRowHeight(40);
        tableJadwal.setShowGrid(false);

        tableJadwal.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                selectedJadwalId = filteredJadwal[tableJadwal.getSelectedRow()].getIdJadwal();
                showSeat(selectedJadwalId);
            }
            
        });

        panelJadwal.add(tableJadwal);
        panelJadwal.revalidate();
        panelJadwal.repaint();
    }

    private void showSeat(String idJadwal) {
        buttonOrder.setVisible(false);
        selectedSeatIds.clear();
        setLabelSelectedSeatsInfoText("Selected (0): ");

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

                button.setFont(new Font(FONTFAMILY, Font.BOLD, 12));
                
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
                            button.setBackground(Color.BLUE);
                            button.setForeground(Color.WHITE);
                        } else {
                            selectedSeatIds.remove(currentSeatId);
                            button.setBackground(Color.WHITE);
                            button.setForeground(Color.BLACK);
                        }

                        button.setFocusPainted(false);

                        Collections.sort(selectedSeatIds);

                        String info = "Selected (" + selectedSeatIds.size() +"): ";
                        for (int i = 0; i < selectedSeatIds.size(); i++) {
                            info += " " + selectedSeatIds.get(i) + " |";
                        }

                        setLabelSelectedSeatsInfoText(info.substring(0, info.length() - 1));

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

    private void setLabelSelectedSeatsInfoText(String str) {
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

    private void orderConfirmationClick() {
        OrderConfirmation orderConfirmationDialog = new OrderConfirmation(this);
        orderConfirmationDialog.showDialog();
        close();
    }

    public void close() {
        this.setVisible(false);
        this.dispose();
    }
}
