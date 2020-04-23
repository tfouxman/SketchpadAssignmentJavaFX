package SketchpadAssignment;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

import java.util.ArrayList;

public class Scribble implements Graphic, Cloneable {

    private GraphicsContext gc;
    private Paint stroke;
    private Paint fill;
    private double lineWidth;
    private double startX, startY;
    private double currX, currY;
    private ArrayList<Point> points = new ArrayList<>();

    public Scribble(GraphicsContext g) {
        gc = g;
        stroke = g.getStroke();
        fill = g.getFill();
        lineWidth = g.getLineWidth();
    }

    public void setStart(double x, double y) {
        startX = x;
        startY = y;
    }

    public void setColour() {
        stroke = gc.getStroke();
        fill = gc.getFill();
        lineWidth = gc.getLineWidth();
    }

    public void setPoint(double x, double y) {
        currX = x;
        currY = y;
        Point point = new Point(x, y);
        points.add(point);
        gc.lineTo(currX, currY);
        gc.stroke();
    }

    public void Draw() {
        gc.setStroke(stroke);
        gc.setFill(fill);
        gc.setLineWidth(lineWidth / 2);
        gc.beginPath();
        for ( Point p : points
             ) {
            gc.lineTo(p.x, p.y);
            gc.stroke();
        }
        this.endPath();

    }

    public void startPath() {
        points.clear();
        gc.setLineWidth(lineWidth / 2);
        gc.beginPath();
    }

    public void endPath() {
        gc.closePath();
        gc.setLineWidth(lineWidth);
    }

    public void removeGraphic(Graphic g) {

    }
    public void addGraphic(Graphic g) {

    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    public boolean isWithinPoint(Point p) {
        for (Point pt : points
             ) {
            if (pt.x == p.x && pt.y == p.y) return true;
        }
        return false;
    }

    public Object clone() throws CloneNotSupportedException {
        Scribble o = (Scribble) super.clone();
        o.points = new ArrayList<Point>();
        for ( Point p : points
             ) {
            o.points.add((Point)p.clone());
        }
        return o;
    }

}
