package SketchpadAssignment;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class Circle implements Graphic, Cloneable {

    private double startX, startY, endX, endY, radius;
    private GraphicsContext gc;
    private Paint stroke;
    private Paint fill;
    private double lineWidth;
    private javafx.scene.shape.Circle circle = new javafx.scene.shape.Circle();

    public Circle(GraphicsContext g) {
        gc = g;
        stroke = g.getStroke();
        fill = g.getFill();
        lineWidth = g.getLineWidth();
    }

    public void setStart(double x, double y) {
        startX = x;
        startY = y;
        circle.setCenterX(startX);
        circle.setCenterY(startY);
    }

    public void setColour() {
        stroke = gc.getStroke();
        fill = gc.getFill();
        lineWidth = gc.getLineWidth();
    }
    public void setEnd(double x, double y) {
        endX = x;
        endY = y;
        radius = (Math.abs(endX - startX) + Math.abs(endY - startY)) / 2;
        if (startX > endX) {
            startX = endX;
        }
        if (startY > endY) {
            startY = endY;
        }
        circle.setCenterX(startX);
        circle.setCenterY(startY);
        circle.setRadius(radius);
    }

    public void Draw() {
        gc.setStroke(stroke);
        gc.setFill(fill);
        gc.setLineWidth(lineWidth);
        gc.fillOval(startX, startY, radius, radius);
        gc.strokeOval(startX, startY, radius, radius);
    }

    public void removeGraphic(Graphic g) {

    }
    public void addGraphic(Graphic g) {

    }

    public boolean isWithinPoint(Point p) {
        return circle.contains(new Point2D(p.x, p.y));
    }

    public Object clone() throws CloneNotSupportedException {
        Circle o = (Circle) super.clone();
        o.circle = new javafx.scene.shape.Circle(startX, startY, radius);
        return o;
    }
}
