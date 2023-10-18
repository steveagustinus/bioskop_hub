package src.view.admin.manage_movie;

import java.awt.Window;
import java.awt.event.*;
import java.io.File;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;
import src.controller.Controller;

public abstract class MovieFormDialog extends JDialog {
    protected Controller controller = new Controller();

    protected JTextField fieldID;
    protected JTextField fieldJudul;
    protected JDatePickerImpl datePickerReleaseDate;
    protected JTextArea fieldAlamat;
    protected JFileChooser fChooser;
    protected JLabel labelDisplayFoto;
    protected File fotoMovie;
    protected JButton buttonSubmit;

    public MovieFormDialog(Window owner) {
        super(owner, ModalityType.DOCUMENT_MODAL);
        initializeComponent();
        this.setLocationRelativeTo(owner);
    }

    private void initializeComponent() {
        this.setLayout(null);
        this.setSize(1280, 720);

        JLabel labelID = new JLabel("ID: ");
        labelID.setBounds(5, 5, 100, 20);

        JLabel labelJudul = new JLabel("Judul: ");
        labelJudul.setBounds(5, 30, 100, 20);
        
        JLabel labelKota = new JLabel("Kota: ");
        labelKota.setBounds(5, 55, 100, 20);

        JLabel labelAlamat = new JLabel("Alamat: ");
        labelAlamat.setBounds(5, 140, 100, 20);

        JLabel labelFoto = new JLabel("Foto: ");
        labelFoto.setBounds(5, 165, 100, 20);

        labelDisplayFoto = new JLabel();
        labelDisplayFoto.setBounds(5, 195, 450, 250);

        fieldID = new JTextField("");
        fieldID.setBounds(110, 5, 200, 20);

        fieldJudul = new JTextField("");
        fieldJudul.setBounds(110, 30, 200, 20);

        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model);
        datePickerReleaseDate = new JDatePickerImpl(datePanel);
        datePickerReleaseDate.setBounds(110, 55, 200, 30);

        fieldAlamat = new JTextArea();
        fieldAlamat.setBounds(110, 90, 200, 80);
        fieldAlamat.setLineWrap(true);
        fieldAlamat.setWrapStyleWord(true);
        
        if (fotoMovie != null) {
            labelDisplayFoto.setIcon(new ImageIcon(
                new ImageIcon(fotoMovie.getAbsolutePath())
                .getImage()
                .getScaledInstance(450, 250,java.awt.Image.SCALE_SMOOTH)
            ));
        }

        fChooser = new JFileChooser();

        JButton buttonSelectFoto = new JButton("Select file");
        buttonSelectFoto.setBounds(110, 165, 150, 20);
        buttonSelectFoto.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fChooser.setCurrentDirectory(new File(controller.getLastOpenedDirectory()));
                if (fChooser.showOpenDialog(getParent()) == JFileChooser.APPROVE_OPTION) {
                    fotoMovie = fChooser.getSelectedFile();

                    labelDisplayFoto.setIcon(new ImageIcon(
                        new ImageIcon(fotoMovie.getAbsolutePath())
                        .getImage()
                        .getScaledInstance(450, 250,java.awt.Image.SCALE_SMOOTH)
                    ));

                    controller.saveLastOpenedDirectory(fotoMovie.getParent());
                }
            }
        });

        buttonSubmit = new JButton();
        buttonSubmit.setBounds(5, 450, 250, 20);
        //buttonSubmit.setEnabled(false);

        this.add(labelID);
        this.add(labelJudul);
        this.add(labelKota);
        this.add(labelAlamat);
        this.add(labelFoto);
        this.add(labelDisplayFoto);

        this.add(fieldID);
        this.add(fieldJudul);
        this.add(datePickerReleaseDate);
        this.add(fieldAlamat);
        this.add(buttonSelectFoto);
        this.add(buttonSubmit);
    }
}
