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

    private Vessel cruiser = new Cruiser();
    private Vessel wing = new Wing();
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
        double clock = Math.toRadians(clockPitch);
        Matrix3 clockTransform = Matrix3.getMatrix3XY(clock);
        return headingTransform.multiply(pitchTransform).multiply(clockTransform);
    }

    private void paint(Graphics2D g2){
        Matrix3 transform = getTransform();
//        g2.translate(getWidth() / 2, getHeight() / 2);


//        ArrayList<Triangle> tris = createTris();
        List<Triangle> tris = wing.getModel();
        tris.addAll(cruiser.getModel());
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
                Vertex v1 = transform.transform(t.v1);
                Vertex v2 = transform.transform(t.v2);
                Vertex v3 = transform.transform(t.v3);

                // since we are not using Graphics2D anymore, we have to do translation manually
                v1.x += getWidth() / 2 + vessel.getCoordinates().getX();
                v1.y += getHeight() / 2 + vessel.getCoordinates().getY();
                v2.x += getWidth() / 2 + vessel.getCoordinates().getX();
                v2.y += getHeight() / 2 + vessel.getCoordinates().getY();
                v3.x += getWidth() / 2 + vessel.getCoordinates().getX();
                v3.y += getHeight() / 2 + vessel.getCoordinates().getY();

                Vertex ab = new Vertex(v2.x - v1.x, v2.y - v1.y, v2.z - v1.z);
                Vertex ac = new Vertex(v3.x - v1.x, v3.y - v1.y, v3.z - v1.z);
                Vertex norm = new Vertex(
                        ab.y * ac.z - ab.z * ac.y,
                        ab.z * ac.x - ab.x * ac.z,
                        ab.x * ac.y - ab.y * ac.x
                );
                double normalLength = Math.sqrt(norm.x * norm.x + norm.y * norm.y + norm.z * norm.z);
                norm.x /= normalLength;
                norm.y /= normalLength;
                norm.z /= normalLength;

                double angleCos = Math.abs(norm.z);
                // compute rectangular bounds for triangle
                int minX = (int) Math.max(0, Math.ceil(Math.min(v1.x, Math.min(v2.x, v3.x))));
                int maxX = (int) Math.min(img.getWidth() - 1, Math.floor(Math.max(v1.x, Math.max(v2.x, v3.x))));
                int minY = (int) Math.max(0, Math.ceil(Math.min(v1.y, Math.min(v2.y, v3.y))));
                int maxY = (int) Math.min(img.getHeight() - 1, Math.floor(Math.max(v1.y, Math.max(v2.y, v3.y))));

                double triangleArea = (v1.y - v3.y) * (v2.x - v3.x) + (v2.y - v3.y) * (v3.x - v1.x);
                for (int y = minY; y <= maxY; y++) {
                    for (int x = minX; x <= maxX; x++) {
                        double b1 = ((y - v3.y) * (v2.x - v3.x) + (v2.y - v3.y) * (v3.x - x)) / triangleArea;
                        double b2 = ((y - v1.y) * (v3.x - v1.x) + (v3.y - v1.y) * (v1.x - x)) / triangleArea;
                        double b3 = ((y - v2.y) * (v1.x - v2.x) + (v1.y - v2.y) * (v2.x - x)) / triangleArea;
                        if (b1 >= 0 && b1 <= 1 && b2 >= 0 && b2 <= 1 && b3 >= 0 && b3 <= 1) {
                            double depth = b1 * v1.z + b2 * v2.z + b3 * v3.z;
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
            Vertex m1 = new Vertex((t.v1.x + t.v2.x)/2, (t.v1.y + t.v2.y)/2, (t.v1.z + t.v2.z)/2);
            Vertex m2 = new Vertex((t.v2.x + t.v3.x)/2, (t.v2.y + t.v3.y)/2, (t.v2.z + t.v3.z)/2);
            Vertex m3 = new Vertex((t.v1.x + t.v3.x)/2, (t.v1.y + t.v3.y)/2, (t.v1.z + t.v3.z)/2);
            result.add(new Triangle(t.v1, m1, m3, t.color));
            result.add(new Triangle(t.v2, m1, m2, t.color));
            result.add(new Triangle(t.v3, m2, m3, t.color));
            result.add(new Triangle(m1, m2, m3, t.color));
        }
        for (Triangle t : result) {
            for (Vertex v : new Vertex[] { t.v1, t.v2, t.v3 }) {
                double l = Math.sqrt(v.x * v.x + v.y * v.y + v.z * v.z) / Math.sqrt(30000);
                v.x /= l;
                v.y /= l;
                v.z /= l;
            }
        }
        return result;
    }

    public void repaintClock(int clock) {
        this.clockPitch = clock;
        repaint();
    }

    public void addVessel(Vessel vessel) {
        vessels.add(vessel);
    }
}
