package src.view.admin.manage_cinema;

import java.awt.Font;
import java.awt.Window;
import java.awt.event.*;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import src.controller.Controller;

public abstract class CinemaFormDialog extends JDialog {
    protected Controller controller = new Controller();

    protected JTextField fieldID;
    protected JTextField fieldNama;
    protected JTextField fieldKota;
    protected JTextArea fieldAlamat;
    protected JFileChooser fChooser;
    protected JLabel labelDisplayFoto;
    protected File fotoCinema;
    protected JButton buttonSubmit;

    public CinemaFormDialog(Window owner) {
        super(owner, ModalityType.DOCUMENT_MODAL);
        initializeComponent();
        this.setLocationRelativeTo(owner);
    }

    private void initializeComponent() {
        this.setLayout(null);
        this.setSize(460, 525);

        JLabel labelID = new JLabel("ID: ");
        labelID.setBounds(5, 5, 100, 25);

        JLabel labelNama = new JLabel("Nama Cinema: ");
        labelNama.setBounds(5, 35, 100, 20);
        
        JLabel labelKota = new JLabel("Kota: ");
        labelKota.setBounds(5, 60, 100, 20);

        JLabel labelAlamat = new JLabel("Alamat: ");
        labelAlamat.setBounds(5, 85, 100, 20);

        JLabel labelFoto = new JLabel("Foto: ");
        labelFoto.setBounds(5, 170, 100, 20);

        labelDisplayFoto = new JLabel();
        labelDisplayFoto.setBounds(5, 195, 450, 250);

        fieldID = new JTextField("");
        fieldID.setBounds(110, 5, 345, 25);
        fieldID.setFont(new Font("Dialog", Font.PLAIN, 20));

        fieldNama = new JTextField("");
        fieldNama.setBounds(110, 35, 345, 20);

        fieldKota = new JTextField("");
        fieldKota.setBounds(110, 60, 345, 20);

        fieldAlamat = new JTextArea();
        fieldAlamat.setBounds(110, 85, 345, 80);
        fieldAlamat.setLineWrap(true);
        fieldAlamat.setWrapStyleWord(true);

        JButton buttonSelectFoto = new JButton("Select file");
        buttonSelectFoto.setBounds(110, 170, 150, 20);

        buttonSubmit = new JButton();
        buttonSubmit.setBounds(5, 455, 450, 40);
        
        if (fotoCinema != null) {
            labelDisplayFoto.setIcon(new ImageIcon(
                new ImageIcon(fotoCinema.getAbsolutePath())
                .getImage()
                .getScaledInstance(450, 250,java.awt.Image.SCALE_SMOOTH)
            ));
        }

        fChooser = new JFileChooser();

        buttonSelectFoto.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fChooser.setCurrentDirectory(new File(controller.getLastOpenedDirectory()));
                if (fChooser.showOpenDialog(getParent()) == JFileChooser.APPROVE_OPTION) {
                    fotoCinema = fChooser.getSelectedFile();

                    labelDisplayFoto.setIcon(new ImageIcon(
                        new ImageIcon(fotoCinema.getAbsolutePath())
                        .getImage()
                        .getScaledInstance(450, 250,java.awt.Image.SCALE_SMOOTH)
                    ));

                    controller.saveLastOpenedDirectory(fotoCinema.getParent());
                }
            }
        });

        this.add(labelID);
        this.add(labelNama);
        this.add(labelKota);
        this.add(labelAlamat);
        this.add(labelFoto);
        this.add(labelDisplayFoto);

        this.add(fieldID);
        this.add(fieldNama);
        this.add(fieldKota);
        this.add(fieldAlamat);
        this.add(buttonSelectFoto);
        this.add(buttonSubmit);
    }
}
