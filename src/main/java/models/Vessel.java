package models;

import javafx.geometry.Point3D;
import util.Triangle;

import java.util.List;

public abstract class Vessel implements VesselInterface {
    protected static final double SCALE = 10;
    private Point3D coordinates;
    private Point3D direction;
    private double acceleration;

    public abstract List<Triangle> getModel();
}
