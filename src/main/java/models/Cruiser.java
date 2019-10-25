package models;

import util.Triangle;
import util.Vertex;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Cruiser extends Vessel{
    private static final Vertex VERTEX_FORWARD = new Vertex(0, SCALE * 6, 0);
    private static final Vertex VERTEX_LEFT = new Vertex(- SCALE * 2, 0, 0);
    private static final Vertex VERTEX_RIGHT = new Vertex(SCALE * 2, 0, 0);
    private static final Vertex VERTEX_BACK = new Vertex(0, - SCALE * 2, 0);
    private static final Vertex VERTEX_TOP = new Vertex(0, 0, - SCALE);

    @Override
    public List<Triangle> getModel() {
        List<Triangle> model = new ArrayList<>();
        model.add(new Triangle(VERTEX_FORWARD, VERTEX_LEFT, VERTEX_TOP, Color.WHITE));
        model.add(new Triangle(VERTEX_FORWARD, VERTEX_RIGHT, VERTEX_TOP, Color.BLUE));
        model.add(new Triangle(VERTEX_BACK, VERTEX_LEFT, VERTEX_TOP, Color.CYAN));
        model.add(new Triangle(VERTEX_BACK, VERTEX_RIGHT, VERTEX_TOP, Color.GREEN));
        model.add(new Triangle(VERTEX_BACK, VERTEX_LEFT, VERTEX_FORWARD, Color.ORANGE));
        model.add(new Triangle(VERTEX_BACK, VERTEX_RIGHT, VERTEX_FORWARD, Color.PINK));
        return model;
    }
}
