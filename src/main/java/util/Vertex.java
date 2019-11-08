package util;

import javafx.geometry.Point3D;

public class Vertex extends Point3D {
    public Vertex(double x, double y, double z) {
        super(x, y, z);
    }

    public Vertex(Point3D point3D){
        super(point3D.getX(), point3D.getY(), point3D.getZ());
    }

    @Override
    public Vertex add(Point3D point3D){
        return new Vertex(super.add(point3D));
    }
    @Override
    public Vertex subtract(Point3D point3D){
        return new Vertex(super.subtract(point3D));
    }
}
