package src.view.admin.manage_studio;

import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import src.controller.Controller;
import src.view.admin.GetObjectDialog;

public abstract class StudioFormDialog extends JDialog {
    protected Controller controller = new Controller();

    protected JTextField fieldIdStudio;
    protected JTextField fieldIdCinema;
    protected JButton buttonSelectCinema;
    protected JComboBox<String> fieldStudioClass;
    protected JComboBox<String> fieldStudioType;
    protected JButton buttonSubmit;

    public StudioFormDialog(Window owner) {
        super(owner, ModalityType.DOCUMENT_MODAL);
        initializeComponent();
        this.setLocationRelativeTo(owner);
    }

    private void initializeComponent() {
        this.setLayout(null);
        this.setSize(420, 195);

        JLabel labelIdStudio = new JLabel("ID Studio: ");
        labelIdStudio.setBounds(5, 5, 100, 25);

        JLabel labelIdCinema = new JLabel("ID Cinema: ");
        labelIdCinema.setBounds(5, 35, 100, 20);

        JLabel labelStudioClass = new JLabel("Kelas Studio: ");
        labelStudioClass.setBounds(5, 60, 100, 20);

        JLabel labelStudioType = new JLabel("Tipe Studio: ");
        labelStudioType.setBounds(5, 85, 100, 20);

        fieldIdStudio = new JTextField("");
        fieldIdStudio.setBounds(110, 5, 305, 25);
        fieldIdStudio.setFont(new Font("Dialog", Font.PLAIN, 20));

        fieldIdCinema = new JTextField("");
        fieldIdCinema.setBounds(110, 35, 200, 20);
        fieldIdCinema.setEditable(false);

        buttonSelectCinema = new JButton("Pilih cinema");
        buttonSelectCinema.setBounds(315, 35, 100, 20);

        fieldStudioClass = new JComboBox<String>(controller.getListStudioClass());
        fieldStudioClass.setBounds(110, 60, 305, 20);

        fieldStudioType = new JComboBox<String>(controller.getListStudioType());
        fieldStudioType.setBounds(110, 85, 305, 20);

        buttonSubmit = new JButton("");
        buttonSubmit.setBounds(5, 125, 410, 40);

        buttonSelectCinema.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                fieldIdCinema.setText(GetObjectDialog.getCinema());
            }
            
        });

        this.add(labelIdStudio);
        this.add(labelIdCinema);
        this.add(labelStudioClass);
        this.add(labelStudioType);

        this.add(fieldIdStudio);
        this.add(fieldIdCinema);
        this.add(buttonSelectCinema);
        this.add(fieldStudioClass);
        this.add(fieldStudioType);

        this.add(buttonSubmit);
    }
}
