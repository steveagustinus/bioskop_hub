package src.view.user;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class TransactionHistoryScreen {
    private JTable table = new JTable();
    private DefaultTableModel model = new DefaultTableModel();
    public TransactionHistoryScreen() {
        JFrame frame = new JFrame("Transaction History");
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        model.addColumn("Transaction Date");
        model.addColumn("Transaction Type");
        

        frame.add(panel);

        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
}
