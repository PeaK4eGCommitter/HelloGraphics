import javax.swing.*;
import java.awt.*;

public class DemoViewer {

    public static void main(String[] args) {

        JFrame frame = new JFrame();
        Container pane = frame.getContentPane();
        pane.setLayout(new BorderLayout());

        JSlider headingSlider = new JSlider(0, 360, 180);
        pane.add(headingSlider, BorderLayout.SOUTH);

        JSlider pitchSlider = new JSlider(SwingConstants.VERTICAL, -90, 90, 0);
        pane.add(pitchSlider, BorderLayout.EAST);

        HelloJPanel renderPanel = new HelloJPanel(headingSlider.getValue(), pitchSlider.getValue());

        headingSlider.addChangeListener(e -> renderPanel.repaintHeading(headingSlider.getValue()));
        pitchSlider.addChangeListener(e -> renderPanel.repaintPitch(pitchSlider.getValue()));

        pane.add(renderPanel, BorderLayout.CENTER);
        frame.setSize(400, 400);
        frame.setVisible(true);
    }
}
