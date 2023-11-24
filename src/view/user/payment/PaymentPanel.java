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
import src.view.MainInterface;

public class PaymentPanel extends JPanel implements MainInterface {
    private Controller controller = new Controller();

    private String[] paymentMethods;
    private String paymentMethod;

    public PaymentPanel(int width, int height) {
        this.setLayout(null);
        this.setSize(width, height);
        this.setBackground(FRAME_BACKGROUND);
        initializeComponent();
    }

    private void initializeComponent() {
        JLabel labelHeader = new JLabel("Pembayaran");
        labelHeader.setSize(this.getWidth(), 40);
        labelHeader.setLocation(0, 0);
        labelHeader.setHorizontalAlignment(SwingConstants.CENTER);
        labelHeader.setVerticalAlignment(SwingConstants.CENTER);
        labelHeader.setFont(new Font("Dialog", Font.BOLD, 25));
        labelHeader.setForeground(TEXT_BACKGROUND);

        JLabel labelInfo = new JLabel("Pilih metode pembayaran di bawah ini");
        labelInfo.setSize(this.getWidth() - 20, 20);
        labelInfo.setLocation(20, labelHeader.getY() + labelHeader.getHeight() + 10);
        labelInfo.setForeground(TEXT_BACKGROUND);

        ButtonGroup groupPaymentMethod = new ButtonGroup();
        paymentMethods = controller.getPaymentMethods();

        for (int i = 0; i < paymentMethods.length; i++) {
            JRadioButton radioButton = new JRadioButton(paymentMethods[i]);
            radioButton.setName("radiobutton_" + i);
            radioButton.setSize(200, 20);
            radioButton.setForeground(TEXT_BACKGROUND);
            radioButton.setBackground(FRAME_BACKGROUND);
            
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