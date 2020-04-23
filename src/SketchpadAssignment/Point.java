package SketchpadAssignment;

public class Point implements Cloneable{
    public double x, y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
