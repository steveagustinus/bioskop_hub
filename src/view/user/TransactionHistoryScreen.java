package src.view.user;


import java.awt.Font;
import java.awt.Dialog.ModalityType;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;


import src.controller.Controller;
import src.controller.UserDataSingleton;


public class TransactionHistoryScreen {
    Controller controller = new Controller();
    UserDataSingleton userData = UserDataSingleton.getInstance();

    public TransactionHistoryScreen() {
        JDialog frame = new JDialog();
        frame.setTitle("Transaction History");
        frame.setModalityType(ModalityType.DOCUMENT_MODAL);
        frame.setSize(300, 250);
        frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        
        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel mainlabel = new JLabel("Transaction History");
        mainlabel.setBounds(30, 10, 500, 50);
        panel.add(mainlabel);
        mainlabel.setFont(new Font("Arial", Font.BOLD, 24));

        JButton fnbButton = new JButton("FnB Transaction History");
        fnbButton.setBounds(40, 70, 200, 30);
        fnbButton.addActionListener(e -> {
            frame.setVisible(false);
            new PrintTableFnBScreen();
            frame.setVisible(true);
        });

        JButton tiketsButton = new JButton("Tickets Transaction History");
        tiketsButton.setBounds(40, 120, 200, 30);
        tiketsButton.addActionListener(e -> {
            frame.setVisible(false);
            new PrintTableTiketsScreen();
            frame.setVisible(true);
        });
        
        
        JButton backButton = new JButton("Back");
        backButton.setBounds(40, 170, 200, 30);
        backButton.addActionListener(e -> {
            frame.dispose();
        });

        panel.add(backButton);
        panel.add(fnbButton);
        panel.add(tiketsButton);
        frame.add(panel);

        frame.setVisible(true);
    }
}
