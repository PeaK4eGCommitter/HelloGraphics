package models;

import javafx.geometry.Point3D;
import util.Triangle;

import java.util.List;

public abstract class Vessel {
    protected static final double SCALE = 20;
    private Point3D coordinates;
    private Point3D direction;
    private double acceleration;

    public abstract List<Triangle> getModel();

    public Point3D getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Point3D coordinates) {
        this.coordinates = coordinates;
    }
}
