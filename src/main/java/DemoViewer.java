import javafx.geometry.Point3D;
import models.Cruiser;
import models.Vessel;
import models.Wing;

import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

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

        Vessel vessel = new Wing();
        vessel.setCoordinates(new Point3D(100, 100, 0));
        renderPanel.addVessel(vessel);
        headingSlider.addChangeListener(e -> renderPanel.repaintHeading(headingSlider.getValue()));
        pitchSlider.addChangeListener(e -> renderPanel.repaintPitch(pitchSlider.getValue()));
        renderPanel.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                // not used
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                vessel.setDirectionTo(new Point3D(e.getX(), e.getY(), 0));
            }
        });

        Thread myThready = new Thread(new Runnable() {
            private int angle = 0;
            public void run(){
                while (renderPanel != null) {
                    /*
                    angle++;
                    if (angle > 180) {
                        angle = -180;
                    }
                    renderPanel.repaintClock(angle);
                    */
                    renderPanel.repaint();
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
