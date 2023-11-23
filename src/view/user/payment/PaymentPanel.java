package src.view.user.payment;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

import src.controller.Controller;

public class PaymentPanel extends JPanel {
    private Controller controller = new Controller();

    private int totalBayar;
    private String[] paymentMethods;
    private String paymentMethod;

    public PaymentPanel(int width, int height, int totalBayar) {
        this.setLayout(null);
        this.setSize(width, height);
        this.totalBayar = totalBayar;
        initializeComponent();
    }

    private void initializeComponent() {
        JLabel labelHeader = new JLabel("Pembayaran");
        labelHeader.setSize(this.getWidth(), 40);
        labelHeader.setLocation(0, 0);
        labelHeader.setHorizontalAlignment(SwingConstants.CENTER);
        labelHeader.setVerticalAlignment(SwingConstants.CENTER);
        labelHeader.setFont(new Font("Dialog", Font.BOLD, 25));

        JLabel labelInfo = new JLabel("Pilih metode pembayaran di bawah ini");
        labelInfo.setSize(this.getWidth() - 20, 20);
        labelInfo.setLocation(20, labelHeader.getY() + labelHeader.getHeight() + 10);

        ButtonGroup groupPaymentMethod = new ButtonGroup();
        paymentMethods = controller.getPaymentMethods();

        for (int i = 0; i < paymentMethods.length; i++) {
            JRadioButton radioButton = new JRadioButton(paymentMethods[i]);
            radioButton.setName("radiobutton_" + i);
            radioButton.setSize(200, 20);
            
            if (i == 0) {
                radioButton.setLocation(20, labelInfo.getY() + labelInfo.getHeight() + 10);
            } else {
                radioButton.setLocation(20, labelInfo.getY() + labelInfo.getHeight() + 10 + i * (10 + radioButton.getHeight()));
            }

            groupPaymentMethod.add(radioButton);
            this.add(radioButton);
        }

        JButton buttonPesan = new JButton("Pesan");
        buttonPesan.setSize(this.getWidth() / 3, 30);
        buttonPesan.setLocation(
            buttonPesan.getWidth(),
            labelInfo.getY() + labelInfo.getHeight() + 10 + paymentMethods.length * (20 + 10) + 10
        );

        buttonPesan.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                executeOrder();
            }
            
        });

        this.add(labelHeader);
        this.add(labelInfo);
        this.add(buttonPesan);
    }

    private void executeOrder() {
        
    }
}