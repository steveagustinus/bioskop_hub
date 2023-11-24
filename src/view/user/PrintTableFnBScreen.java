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
import src.view.MainInterface;

public class PrintTableFnBScreen {
    JTable table;
    DefaultTableModel model = new DefaultTableModel();
    Controller controller = new Controller();
    UserDataSingleton userData = UserDataSingleton.getInstance();
    public PrintTableFnBScreen() {
        JDialog frame = new JDialog();
        frame.setTitle("Transaction History");
        frame.setModalityType(ModalityType.DOCUMENT_MODAL);
        frame.setSize(1000, 700);
        frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        controller.printTableFnB(userData.getId(), table, model);
        JButton backButton = new JButton("Back");
        backButton.setBounds(40, 170, 200, 30);
        backButton.setBackground(MainInterface.BUTTON_BACKGROUND);
        backButton.setForeground(MainInterface.BUTTON_FOREGROUND);
        backButton.addActionListener(e -> {
            frame.dispose();
        });

        frame.getContentPane().add(backButton, BorderLayout.SOUTH);

        table = new JTable(model);
        table.setBackground(MainInterface.FRAME_BACKGROUND);
        table.setForeground(MainInterface.TEXT_BACKGROUND);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(MainInterface.FRAME_BACKGROUND);

        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.getContentPane().setBackground(MainInterface.FRAME_BACKGROUND);
        frame.setVisible(true);
    }
}
