package src.view.admin.manage_movie;

import java.awt.Font;
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
    protected JComboBox<String> fieldLanguage;
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
        this.setSize(555, 555);

        String[] labelText = { "ID", "Judul", "Tanggal Rilis", "Sutradara", "Bahasa", "Durasi", "Sinopsis" };
        JLabel[] labels = new JLabel[labelText.length];

        for (int i = 0; i < labelText.length; i++) {
            JLabel label = new JLabel(labelText[i] + " : ");
            label.setSize(100, 25);
            
            if (i == 0) {
                label.setLocation(5, 5);
            } else {
                label.setLocation(
                    labels[i - 1].getX(),
                    labels[i - 1].getY() + labels[i - 1].getHeight() + 5
                );
            }

            this.add(label);
            labels[i] = label;
        }

        fieldID = new JTextField();
        fieldID.setBounds(115, 5, 435, 25);
        fieldID.setFont(new Font("Dialog", Font.PLAIN, 20));

        fieldJudul = new JTextField();
        fieldJudul.setBounds(115, 35, 200, 25);

        UtilDateModel model = new UtilDateModel();
        JDatePanelImpl datePanel = new JDatePanelImpl(model);
        datePickerReleaseDate = new JDatePickerImpl(datePanel);
        datePickerReleaseDate.setBounds(115, 65, 200, 30);

        fieldDirector = new JTextField();
        fieldDirector.setBounds(115, 100, 200, 25);

        fieldLanguage = new JComboBox<String>(controller.getMovieLanguageList());
        fieldLanguage.setBounds(115, 130, 200, 25);

        fieldDurasi = new JTextField();
        fieldDurasi.setBounds(115, 160, 200, 25);

        fieldSinopsis = new JTextArea();
        fieldSinopsis.setBounds(115, 190, 200, 270);
        fieldSinopsis.setLineWrap(true);
        fieldSinopsis.setWrapStyleWord(true);

        JButton buttonSelectFoto = new JButton("Select photo");
        buttonSelectFoto.setBounds(325, 35, 225, 20);

        labelDisplayFoto = new JLabel();
        labelDisplayFoto.setBounds(325, 60, 225, 400);

        buttonSubmit = new JButton();
        buttonSubmit.setSize(545, 40);
        buttonSubmit.setLocation(5, 480);

        if (fotoMovie != null) {
            labelDisplayFoto.setIcon(new ImageIcon(
                new ImageIcon(fotoMovie.getAbsolutePath())
                .getImage()
                .getScaledInstance(225, 400, java.awt.Image.SCALE_SMOOTH)
            ));
        }

        fChooser = new JFileChooser();
        buttonSelectFoto.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fChooser.setCurrentDirectory(new File(controller.getLastOpenedDirectory()));
                if (fChooser.showOpenDialog(getParent()) == JFileChooser.APPROVE_OPTION) {
                    fotoMovie = fChooser.getSelectedFile();

                    labelDisplayFoto.setIcon(new ImageIcon(
                        new ImageIcon(fotoMovie.getAbsolutePath())
                        .getImage()
                        .getScaledInstance(225, 400,java.awt.Image.SCALE_SMOOTH)
                    ));

                    controller.saveLastOpenedDirectory(fotoMovie.getParent());
                }
            }
        });

        this.add(fieldID);
        this.add(fieldJudul);
        this.add(datePickerReleaseDate);
        this.add(fieldDirector);
        this.add(fieldLanguage);
        this.add(fieldDurasi);
        this.add(fieldSinopsis);
        this.add(labelDisplayFoto);
        this.add(buttonSelectFoto);
        this.add(buttonSubmit);
    }
}
