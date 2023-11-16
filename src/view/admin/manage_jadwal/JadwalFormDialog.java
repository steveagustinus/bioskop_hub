package src.view.admin.manage_jadwal;

import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;
import src.controller.Controller;
import src.view.admin.GetObjectDialog;

public abstract class JadwalFormDialog extends JDialog {
    protected Controller controller = new Controller();

    protected JTextField fieldID;
    protected JTextField fieldIDStudio;
    protected JDatePickerImpl datePickerTanggal;
    protected JTextField fieldWaktu;
    protected JTextField fieldIDMovie;
    protected JTextField fieldHarga;
    protected JButton buttonSubmit;

    public JadwalFormDialog(Window owner) {
        super(owner, ModalityType.DOCUMENT_MODAL);
        initializeComponent();
        this.setLocationRelativeTo(owner);
    }

    private void initializeComponent() {
        this.setLayout(null);
        this.setSize(555, 270);

        String[] labelText = { "ID", "ID Studio", "Tanggal Tayang", "Waktu Tayang", "ID Movie", "Harga" };
        JLabel[] labels = new JLabel[labelText.length];

        for (int i = 0; i < labelText.length; i++) {
            JLabel label = new JLabel(labelText[i] + " : ");
            label.setSize(100, 25);
            
            if (i == 0) {
                label.setSize(100, 30);
                label.setLocation(5, 5);
                label.setFont(new Font("Dialog", Font.PLAIN, 20));
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

        fieldIDStudio = new JTextField();
        fieldIDStudio.setBounds(115, 35, 300, 25);
        fieldIDStudio.setEditable(false);

        JButton buttonSelectStudio = new JButton("Pilh studio");
        buttonSelectStudio.setBounds(420, 35, 130, 25);

        UtilDateModel model = new UtilDateModel();
        JDatePanelImpl datePanel = new JDatePanelImpl(model);
        datePickerTanggal = new JDatePickerImpl(datePanel);
        datePickerTanggal.setBounds(115, 65, 435, 30);

        fieldWaktu = new JTextField();
        fieldWaktu.setBounds(115, 100, 435, 25);

        fieldIDMovie = new JTextField();
        fieldIDMovie.setBounds(115, 130, 300, 25);
        fieldIDMovie.setEditable(false);

        JButton buttonSelectMovie = new JButton("Pilih film");
        buttonSelectMovie.setBounds(420, 130, 130, 25);
        
        fieldHarga = new JTextField();
        fieldHarga.setBounds(115, 160, 435, 25);

        buttonSubmit = new JButton();
        buttonSubmit.setSize(545, 40);
        buttonSubmit.setLocation(5, 200);

        buttonSelectStudio.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                fieldIDStudio.setText(GetObjectDialog.getStudio());
            }
            
        });

        buttonSelectMovie.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                fieldIDMovie.setText(GetObjectDialog.getMovie());
            }
            
        });

        this.add(fieldID);
        this.add(fieldIDStudio);
        this.add(buttonSelectStudio);
        this.add(datePickerTanggal);
        this.add(fieldWaktu);
        this.add(fieldIDMovie);
        this.add(buttonSelectMovie);
        this.add(fieldHarga);
        this.add(buttonSubmit);
    }
}
