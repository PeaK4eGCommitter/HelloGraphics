import util.Triangle;
import util.Vertex;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.util.ArrayList;

public class HelloJPanel extends JPanel {
    private int heading = getWidth() / 2;
    private int pitch = getHeight() / 2;

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, getWidth(), getHeight());
        paint(g2);
    }

    public void repaintHeading(int heading){
        this.heading = heading;
        repaint();
    }

    public void repaintPitch(int pitch){
        this.pitch = pitch;
        repaint();
    }

    private void paint(Graphics2D g2){
        g2.translate(heading, - pitch);
        g2.setColor(Color.WHITE);
        ArrayList<Triangle> tris = createTris();
        for (Triangle t : tris) {
            Path2D path = new Path2D.Double();
            path.moveTo(t.v1.x, t.v1.y);
            path.lineTo(t.v2.x, t.v2.y);
            path.lineTo(t.v3.x, t.v3.y);
            path.closePath();
            g2.draw(path);
        }
    }

    private ArrayList<Triangle> createTris(){
        ArrayList<Triangle> tris = new ArrayList<>();
        tris.add(new Triangle(new Vertex(100, 100, 100),
                new Vertex(-100, -100, 100),
                new Vertex(-100, 100, -100),
                Color.WHITE));

        tris.add(new Triangle(new Vertex(100, 100, 100),
                new Vertex(-100, -100, 100),
                new Vertex(100, -100, -100),
                Color.RED));

        tris.add(new Triangle(new Vertex(-100, 100, -100),
                new Vertex(100, -100, -100),
                new Vertex(100, 100, 100),
                Color.GREEN));

        tris.add(new Triangle(new Vertex(-100, 100, -100),
                new Vertex(100, -100, -100),
                new Vertex(-100, -100, 100),
                Color.BLUE));
        return tris;
    }
}
