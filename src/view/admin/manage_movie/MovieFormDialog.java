package src.view.admin.manage_movie;

import java.awt.Window;
import java.awt.event.*;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
    protected JTextField fieldDirector;
    protected JComboBox<String> fieldBahasa;
    protected JTextField fieldDurasi;
    protected JTextArea fieldSinopsis;
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

        String[] labelText = { "ID", "Judul", "Tanggal Rilis", "Director", "Bahasa", "Durasi", "Sinopsis" };
        JLabel[] labels = new JLabel[labelText.length];

        for (int i = 0; i < labelText.length; i++) {
            JLabel label = new JLabel(labelText[i]);
            label.setSize(100, 20);

            if (i == 0) {
                label.setLocation(5, 5);
            } else {
                label.setLocation(
                    labels[i - 1].getX(),
                    labels[i - 1].getY() + labels[i - 1].getHeight() + 5
                );
            }

            labels[i] = label;
            this.add(label);
        }
        fieldID = new JTextField("");
        fieldID.setBounds(110, 5, 200, 20);

        fieldJudul = new JTextField("");
        fieldJudul.setBounds(110, 30, 200, 20);

        UtilDateModel model = new UtilDateModel();
        JDatePanelImpl datePanel = new JDatePanelImpl(model);
        datePickerReleaseDate = new JDatePickerImpl(datePanel);
        datePickerReleaseDate.setBounds(110, 55, 200, 30);

        fieldDirector = new JTextField("");
        fieldDirector.setBounds(110, 90, 200, 20);

        fieldBahasa = new JComboBox<String>();
        fieldBahasa.setBounds(110, 115, 200, 20);

        fieldDurasi = new JTextField("");
        fieldDurasi.setBounds(110, 140, 200, 20);

        fieldSinopsis = new JTextArea("");
        fieldSinopsis.setBounds(110, 165, 200, 100);
        
        if (fotoMovie != null) {
            labelDisplayFoto.setIcon(new ImageIcon(
                new ImageIcon(fotoMovie.getAbsolutePath())
                .getImage()
                .getScaledInstance(225, 400, java.awt.Image.SCALE_SMOOTH)
            ));
        }


        labelDisplayFoto = new JLabel();
        labelDisplayFoto.setBounds(565, 30, 225, 400);

        fChooser = new JFileChooser();

        JButton buttonSelectFoto = new JButton("Select file");
        buttonSelectFoto.setBounds(565, 5, 225, 20);

        buttonSelectFoto.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fChooser.setCurrentDirectory(new File(controller.getLastOpenedDirectory()));
                if (fChooser.showOpenDialog(getParent()) == JFileChooser.APPROVE_OPTION) {
                    fotoMovie = fChooser.getSelectedFile();

                    labelDisplayFoto.setIcon(new ImageIcon(
                        new ImageIcon(fotoMovie.getAbsolutePath())
                        .getImage()
                        .getScaledInstance(225, 400, java.awt.Image.SCALE_SMOOTH)
                    ));

                    controller.saveLastOpenedDirectory(fotoMovie.getParent());
                }
            }
        });

        buttonSubmit = new JButton();
        buttonSubmit.setBounds(5, 450, 250, 20);
        //buttonSubmit.setEnabled(false);

        this.add(fieldID);
        this.add(fieldJudul);
        this.add(datePickerReleaseDate);
        this.add(fieldDirector);
        this.add(fieldBahasa);
        this.add(fieldDurasi);
        this.add(fieldSinopsis);
        this.add(labelDisplayFoto);
        this.add(buttonSelectFoto);
        this.add(buttonSubmit);
    }
}
