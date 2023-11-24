package src.view.user.payment;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

import src.controller.Controller;

public class PaymentPanel extends JPanel {
    private Controller controller = new Controller();

    private String[] paymentMethods;
    private String paymentMethod;

    public PaymentPanel(int width, int height) {
        this.setLayout(null);
        this.setSize(width, height);
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

            radioButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (e.getSource() instanceof JRadioButton) {
                        int index = Integer.parseInt(((JRadioButton) e.getSource()).getName().replace("radiobutton_", ""));
                        paymentMethod = paymentMethods[index];
                    }
                }
                
            });

            

            groupPaymentMethod.add(radioButton);
            this.add(radioButton);
        }

        this.add(labelHeader);
        this.add(labelInfo);
    }

    public String getPaymentMethod() {
        return this.paymentMethod;
    }
}