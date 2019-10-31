package models;

import javafx.geometry.Point3D;
import util.Triangle;

import java.util.List;

public abstract class Vessel {
    protected static final double SCALE = 20;
    private double maxSpeed = 1.0;
    private Point3D coordinates;
    private Point3D direction = new Point3D(0, 0, 0);

    public abstract List<Triangle> getModel();

    public void move(){
        coordinates = coordinates.add(direction);
    }

    public Point3D getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Point3D coordinates) {
        this.coordinates = coordinates;
    }

    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public void setDirection(Point3D direction){
        if (direction.magnitude() > maxSpeed){
            this.direction = direction.normalize().multiply(maxSpeed) ;
        } else {
            this.direction = direction;
        }
    }
}
