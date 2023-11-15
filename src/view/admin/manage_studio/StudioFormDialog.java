package src.view.admin.manage_studio;

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
    private JButton buttonSelectCinema;
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
        this.setSize(1280, 720);

        JLabel labelIdStudio = new JLabel("ID: ");
        labelIdStudio.setBounds(5, 5, 100, 20);

        JLabel labelIdCinema = new JLabel("Cinema: ");
        labelIdCinema.setBounds(5, 30, 100, 20);

        fieldIdStudio = new JTextField("");
        fieldIdStudio.setBounds(110, 5, 200, 20);

        fieldIdCinema = new JTextField("");
        fieldIdCinema.setBounds(110, 30, 200, 20);
        fieldIdCinema.setEditable(false);

        buttonSelectCinema = new JButton("Pilih cinema");
        buttonSelectCinema.setBounds(315, 30, 100, 20);

        fieldStudioClass = new JComboBox<String>(controller.getListStudioClass());
        fieldStudioClass.setBounds(110, 55, 200, 20);

        fieldStudioType = new JComboBox<String>(controller.getListStudioType());
        fieldStudioType.setSelectedItem(3);
        fieldStudioType.setBounds(110, 80, 200, 20);

        buttonSubmit = new JButton("");
        buttonSubmit.setBounds(5, 115, 310, 20);

        buttonSelectCinema.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                fieldIdCinema.setText(GetObjectDialog.getCinema());
            }
            
        });
        //buttonSubmit.setEnabled(false);

        this.add(labelIdStudio);
        this.add(labelIdCinema);

        this.add(fieldIdStudio);
        this.add(fieldIdCinema);
        this.add(buttonSelectCinema);
        this.add(fieldStudioClass);
        this.add(fieldStudioType);
        this.add(buttonSubmit);
    }
}
