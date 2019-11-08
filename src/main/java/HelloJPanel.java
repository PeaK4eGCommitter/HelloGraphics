import javafx.geometry.Point3D;
import models.Cruiser;
import models.Vessel;
import models.Wing;
import util.Matrix3;
import util.Triangle;
import util.Vertex;

import javax.swing.JPanel;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HelloJPanel extends JPanel {
    private int sliderHeading;
    private int sliderPitch;
    private int clockPitch;

//    private Vessel cruiser = new Cruiser();
//    private Vessel wing = new Wing();
    private List<Vessel> vessels = new ArrayList<>();

    public HelloJPanel(int sliderHeading, int sliderPitch){
        this.sliderPitch = sliderPitch;
        this.sliderHeading = sliderHeading;
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, getWidth(), getHeight());
        paint(g2);
    }

    public void repaintHeading(int heading){
        this.sliderHeading = heading;
        repaint();
    }

    public void repaintPitch(int pitch){
        this.sliderPitch = pitch;
        repaint();
    }

    private Matrix3 getTransform(){
        double heading = Math.toRadians(sliderHeading);
        Matrix3 headingTransform = Matrix3.getMatrix3XZ(heading);
        double pitch = Math.toRadians(sliderPitch);
        Matrix3 pitchTransform = Matrix3.getMatrix3YZ(pitch);
//        double clock = Math.toRadians(clockPitch);
//        double clock = Math.atan2();
//        Matrix3 clockTransform = Matrix3.getMatrix3XY(clock);
//        return headingTransform.multiply(pitchTransform).multiply(clockTransform);
        return headingTransform.multiply(pitchTransform).multiply(pitchTransform);
    }

    private void paint(Graphics2D g2){
        Matrix3 transformBase = getTransform();
//        g2.translate(getWidth() / 2, getHeight() / 2);


//        ArrayList<Triangle> tris = createTris();
//        List<Triangle> tris = wing.getModel();
//        tris.addAll(cruiser.getModel());
        //        List<Triangle> tris = cruiser.getModel();
//        tris = inflate(tris);
//        tris = inflate(tris);
//        tris = inflate(tris);
//        tris = inflate(tris);
//        tris = inflate(tris);
//        tris = inflate(tris);
        g2.setColor(Color.WHITE);

        BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);

        double[] zBuffer = createZBuffer(img);

        for (Vessel vessel : vessels) {

            for (Triangle t : vessel.getModel()) {
                double clock = - Math.atan2(vessel.getDirection().getY(), vessel.getDirection().getX());
                Matrix3 transform = transformBase.multiply(Matrix3.getMatrix3XY(clock));
                Vertex vA = transform.transform(t.vA).add(vessel.getCoordinates());
                Vertex vB = transform.transform(t.vB).add(vessel.getCoordinates());
                Vertex vC = transform.transform(t.vC).add(vessel.getCoordinates());

                Vertex ab = vB.subtract(vA);
                Vertex ac = vC.subtract(vA);
                Point3D norm = new Vertex(
                        ab.getY() * ac.getZ() - ab.getY() * ac.getY(),
                        ab.getZ() * ac.getX() - ab.getX() * ac.getZ(),
                        ab.getX() * ac.getY() - ab.getY() * ac.getX()
                ).normalize();

                double angleCos = Math.abs(norm.getZ());
                // compute rectangular bounds for triangle
                int minX = (int) Math.max(0, Math.ceil(Math.min(vA.getX(), Math.min(vB.getX(), vC.getX()))));
                int maxX = (int) Math.min(img.getWidth() - 1.0, Math.floor(Math.max(vA.getX(), Math.max(vB.getX(), vC.getX()))));
                int minY = (int) Math.max(0, Math.ceil(Math.min(vA.getY(), Math.min(vB.getY(), vC.getY()))));
                int maxY = (int) Math.min(img.getHeight() - 1.0, Math.floor(Math.max(vA.getY(), Math.max(vB.getY(), vC.getY()))));

                double triangleArea = (vA.getY() - vC.getY()) * (vB.getX() - vC.getX()) + (vB.getY() - vC.getY()) * (vC.getX() - vA.getX());
                for (int y = minY; y <= maxY; y++) {
                    for (int x = minX; x <= maxX; x++) {
                        double b1 = ((y - vC.getY()) * (vB.getX() - vC.getX()) + (vB.getY() - vC.getY()) * (vC.getX() - x)) / triangleArea;
                        double b2 = ((y - vA.getY()) * (vC.getX() - vA.getX()) + (vC.getY() - vA.getY()) * (vA.getX() - x)) / triangleArea;
                        double b3 = ((y - vB.getY()) * (vA.getX() - vB.getX()) + (vA.getY() - vB.getY()) * (vB.getX() - x)) / triangleArea;
                        if (b1 >= 0 && b1 <= 1 && b2 >= 0 && b2 <= 1 && b3 >= 0 && b3 <= 1) {
                            double depth = b1 * vA.getZ() + b2 * vB.getZ() + b3 * vC.getZ();
                            int zIndex = y * img.getWidth() + x;
                            if (zBuffer[zIndex] < depth) {
                                img.setRGB(x, y, getShade(t.color, angleCos).getRGB());
                                zBuffer[zIndex] = depth;
                            }
                        }
                    }
                }
            }
        }
        g2.drawImage(img, 0, 0, null);
    }

    private double[] createZBuffer(BufferedImage img) {
        double[] zBuffer = new double[img.getWidth() * img.getHeight()];
        Arrays.fill(zBuffer, Double.NEGATIVE_INFINITY);
        return zBuffer;
    }

    private static Color getShade(Color color, double shade) {
        double redLinear = Math.pow(color.getRed(), 2.4) * shade;
        double greenLinear = Math.pow(color.getGreen(), 2.4) * shade;
        double blueLinear = Math.pow(color.getBlue(), 2.4) * shade;

        int red = (int) Math.pow(redLinear, 1/2.4);
        int green = (int) Math.pow(greenLinear, 1/2.4);
        int blue = (int) Math.pow(blueLinear, 1/2.4);

        return new Color(red, green, blue);
    }

    private ArrayList<Triangle> createTris(){
        ArrayList<Triangle> tris = new ArrayList<>();
        tris.add(new Triangle(
                new Vertex(100, 100, 100),
                new Vertex(-100, -100, 100),
                new Vertex(-100, 100, -100),
                Color.WHITE)
        );

        tris.add(new Triangle(
                new Vertex(100, 100, 100),
                new Vertex(-100, -100, 100),
                new Vertex(100, -100, -100),
                Color.RED)
        );

        tris.add(new Triangle(
                new Vertex(-100, 100, -100),
                new Vertex(100, -100, -100),
                new Vertex(100, 100, 100),
                Color.GREEN)
        );

        tris.add(new Triangle(
                new Vertex(-100, 100, -100),
                new Vertex(100, -100, -100),
                new Vertex(-100, -100, 100),
                Color.BLUE)
        );
        return tris;
    }

    public static List<Triangle> inflate(List<Triangle> tris) {
        ArrayList<Triangle> result = new ArrayList<>();
        for (Triangle t : tris) {
            Vertex m1 = new Vertex((t.vA.getX() + t.vB.getX())/2, (t.vA.getY() + t.vB.getY())/2, (t.vA.getZ() + t.vB.getZ())/2);
            Vertex m2 = new Vertex((t.vB.getX() + t.vC.getX())/2, (t.vB.getY() + t.vC.getY())/2, (t.vB.getZ() + t.vC.getZ())/2);
            Vertex m3 = new Vertex((t.vA.getX() + t.vC.getX())/2, (t.vA.getY() + t.vC.getY())/2, (t.vA.getZ() + t.vC.getZ())/2);
            result.add(new Triangle(t.vA, m1, m3, t.color));
            result.add(new Triangle(t.vB, m1, m2, t.color));
            result.add(new Triangle(t.vC, m2, m3, t.color));
            result.add(new Triangle(m1, m2, m3, t.color));
        }
        for (Triangle t : result) {
            for (Vertex v : new Vertex[] { t.vA, t.vB, t.vC }) {
                double l = Math.sqrt(v.getX() * v.getX() + v.getY() * v.getY() + v.getZ() * v.getZ()) / Math.sqrt(30000);
                v = (Vertex) v.multiply(1/l);
            }
        }
        return result;
    }

    @Override
    public void repaint(){
        moveVessels();
        super.repaint();
    }

    public void repaintClock(int clock) {
        this.clockPitch = clock;
        moveVessels();
        repaint();
    }

    public void addVessel(Vessel vessel) {
        vessels.add(vessel);
    }

    private void moveVessels(){
        if(vessels != null) {
            for (Vessel vessel : vessels) {
                vessel.move();
            }
        }
    }
}
