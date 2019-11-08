package util;

import java.awt.Color;

public class Triangle {
    public Vertex vA;
    public Vertex vB;
    public Vertex vC;
    public Color color;
    public Triangle(Vertex vA, Vertex vB, Vertex vC, Color color) {
        this.vA = vA;
        this.vB = vB;
        this.vC = vC;
        this.color = color;
    }
}
