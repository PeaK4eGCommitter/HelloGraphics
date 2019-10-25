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

        JSlider clockSlider = new JSlider(SwingConstants.VERTICAL, -90, 90, 0);
        pane.add(clockSlider, BorderLayout.WEST);

        HelloJPanel renderPanel = new HelloJPanel(headingSlider.getValue(), pitchSlider.getValue());

        headingSlider.addChangeListener(e -> renderPanel.repaintHeading(headingSlider.getValue()));
        pitchSlider.addChangeListener(e -> renderPanel.repaintPitch(pitchSlider.getValue()));
//        clockSlider.addChangeListener(e -> renderPanel.repaintClock(clockSlider.getValue()));

        Thread myThready = new Thread(new Runnable() {
            private int angle = 0;
            public void run(){
                while (renderPanel != null) {
                    angle++;
                    if (angle > 180) {
                        angle = -180;
                    }
                    renderPanel.repaintClock(angle);
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        myThready.start();

        pane.add(renderPanel, BorderLayout.CENTER);
        frame.setSize(400, 400);
        frame.setVisible(true);
    }
}
