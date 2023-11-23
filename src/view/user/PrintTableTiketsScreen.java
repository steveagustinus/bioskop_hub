package src.view.user;

import java.awt.BorderLayout;
import java.awt.Dialog.ModalityType;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import src.controller.Controller;
import src.controller.UserDataSingleton;

public class PrintTableTiketsScreen {
    JTable table;
    DefaultTableModel model = new DefaultTableModel();
    Controller controller = new Controller();
    UserDataSingleton userData = UserDataSingleton.getInstance();
    public PrintTableTiketsScreen() {
        JDialog frame = new JDialog();
        frame.setTitle("Transaction History");
        frame.setModalityType(ModalityType.DOCUMENT_MODAL);
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        controller.printTableTickets(userData.getId(), table, model);
        JButton backButton = new JButton("Back");
        backButton.setBounds(40, 170, 200, 30);
        backButton.addActionListener(e -> {
            frame.dispose();
        });
        frame.getContentPane().add(backButton, BorderLayout.SOUTH);

        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}
