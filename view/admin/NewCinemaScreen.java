package view.admin;

import java.awt.event.*;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import controller.Controller;

public class NewCinemaScreen {
    Controller controller = new Controller();
    private JFrame mainFrame;

    private File fotoCinema;

    private String lastDirectory;

    public NewCinemaScreen() {
        showNewCinemaScreen();
        this.lastDirectory = controller.getLastOpenedDirectory();
    }

    public void showNewCinemaScreen() {
        initializeComponent();
    }

    public void initializeComponent() {
        mainFrame = new JFrame("Admin: New Cinema");
        mainFrame.setLayout(null);
        mainFrame.setSize(1280, 720);

        JLabel labelID = new JLabel("ID: ");
        labelID.setBounds(5, 5, 100, 20);

        JLabel labelNama = new JLabel("Nama Cinema: ");
        labelNama.setBounds(5, 30, 100, 20);
        
        JLabel labelKota = new JLabel("Kota: ");
        labelKota.setBounds(5, 55, 100, 20);

        JLabel labelAlamat = new JLabel("Alamat: ");
        labelAlamat.setBounds(5, 140, 100, 20);

        JLabel labelFoto = new JLabel("Foto: ");
        labelFoto.setBounds(5, 165, 100, 20);

        JTextField fieldID = new JTextField("");
        fieldID.setBounds(110, 5, 200, 20);

        JTextField fieldNama = new JTextField("");
        fieldNama.setBounds(110, 30, 200, 20);

        JTextField fieldKota = new JTextField("");
        fieldKota.setBounds(110, 55, 200, 20);

        JTextArea fieldAlamat = new JTextArea();
        fieldAlamat.setBounds(110, 80, 200, 80);
        fieldAlamat.setLineWrap(true);
        fieldAlamat.setWrapStyleWord(true);
        
        JLabel labelDisplayFoto = new JLabel();
        labelDisplayFoto.setBounds(5, 195, 450, 250);

        if (fotoCinema != null) {
            labelDisplayFoto.setIcon(new ImageIcon(
                new ImageIcon(fotoCinema.getAbsolutePath())
                .getImage()
                .getScaledInstance(450, 250,java.awt.Image.SCALE_SMOOTH)
            ));
        }

        JButton buttonSelectFoto = new JButton("Select file");
        buttonSelectFoto.setBounds(110, 165, 150, 20);
        buttonSelectFoto.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fChooser = new JFileChooser();
                fChooser.setCurrentDirectory(new File(lastDirectory));
                if (fChooser.showOpenDialog(mainFrame) == JFileChooser.APPROVE_OPTION) {
                    fotoCinema = fChooser.getSelectedFile();

                    labelDisplayFoto.setIcon(new ImageIcon(
                        new ImageIcon(fotoCinema.getAbsolutePath())
                        .getImage()
                        .getScaledInstance(450, 250,java.awt.Image.SCALE_SMOOTH)
                    ));

                    lastDirectory = fotoCinema.getParent();
                    controller.saveLastOpenedDirectory(lastDirectory);
                }
            }
        });

        JButton buttonSubmit = new JButton("Submit");
        buttonSubmit.setBounds(5, 450, 100, 20);
        //buttonSubmit.setEnabled(false);
        buttonSubmit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (controller.addNewCinema(
                    fieldID.getText(),
                    fieldNama.getText(),
                    fieldAlamat.getText(),
                    fieldKota.getText(),
                    fotoCinema
                ) == 0) {
                    JOptionPane.showMessageDialog(
                        mainFrame,
                        "Operation success",
                        "Admin: Add new cinema",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                }
            }
        });

        mainFrame.add(labelID);
        mainFrame.add(labelNama);
        mainFrame.add(labelKota);
        mainFrame.add(labelAlamat);
        mainFrame.add(labelFoto);
        mainFrame.add(labelDisplayFoto);

        mainFrame.add(fieldID);
        mainFrame.add(fieldNama);
        mainFrame.add(fieldKota);
        mainFrame.add(fieldAlamat);
        mainFrame.add(buttonSelectFoto);
        mainFrame.add(buttonSubmit);

        mainFrame.setVisible(true);

        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}
