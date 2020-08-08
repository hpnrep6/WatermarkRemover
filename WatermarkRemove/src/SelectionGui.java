import javax.swing.*;
import java.awt.event.ActionEvent;

public class SelectionGui {
    private JPanel mainPanel;
    private JButton addWatermark;
    private JButton removeWatermark;

    public SelectionGui() {
        JFrame main = new JFrame("Watermark Adder");
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main.setSize(600,300);
        main.setVisible(true);
        main.add(mainPanel);

        addWatermark.setText("Add Watermark");
        removeWatermark.setText("Remove Watermark");

        addWatermark.addActionListener(this::actionPerformed);
        removeWatermark.addActionListener(this::actionPerformed);
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == addWatermark) {
            new SecondaryGui();
        }

        if(e.getSource() == removeWatermark) {
            new MainGui();
        }
    }
}
