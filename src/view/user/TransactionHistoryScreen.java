package src.view.user;


import java.awt.Font;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


import src.controller.Controller;
import src.controller.UserDataSingleton;


public class TransactionHistoryScreen {
    Controller controller = new Controller();
    UserDataSingleton userData = UserDataSingleton.getInstance();

    public TransactionHistoryScreen() {
        JFrame frame = new JFrame("Transaction History");
        frame.setSize(300, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
            frame.dispose();
            new PrintTableFnBScreen();
        });

        JButton tiketsButton = new JButton("Tickets Transaction History");
        tiketsButton.setBounds(40, 120, 200, 30);
        tiketsButton.addActionListener(e -> {
            frame.dispose();
            new PrintTableTiketsScreen();
        });
        
        
        JButton backButton = new JButton("Back");
        backButton.setBounds(40, 170, 200, 30);
        backButton.addActionListener(e -> {
            frame.dispose();
            new MainMenuUserScreen();
        });

        panel.add(backButton);
        panel.add(fnbButton);
        panel.add(tiketsButton);
        frame.add(panel);

        frame.setVisible(true);
    }
    public static void main(String[] args) {
        new TransactionHistoryScreen();
    }
}
