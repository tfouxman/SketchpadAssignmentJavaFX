package SketchpadAssignment;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class Ellipse implements Graphic, Cloneable {

    private double startX, startY, endX, endY, radiusX, radiusY;
    private GraphicsContext gc;
    private Paint stroke;
    private Paint fill;
    private double lineWidth;
    private javafx.scene.shape.Ellipse ellipse = new javafx.scene.shape.Ellipse();

    public Ellipse(GraphicsContext g) {
        gc = g;
        stroke = g.getStroke();
        fill = g.getFill();
        lineWidth = g.getLineWidth();
    }

    public void setStart(double x, double y) {
        startX = x;
        startY = y;
        ellipse.setCenterX(startX);
        ellipse.setCenterY(startY);
    }

    public void setColour() {
        stroke = gc.getStroke();
        fill = gc.getFill();
        lineWidth = gc.getLineWidth();
    }
    public void setEnd(double x, double y) {
        endX = x;
        endY = y;
        radiusX = Math.abs(endX - startX);
        radiusY = Math.abs(endY - startY);
        if (startX > endX) {
            startX = endX;
        }
        if (startY > endY) {
            startY = endY;
        }
        ellipse.setCenterX(startX);
        ellipse.setCenterY(startY);
        ellipse.setRadiusX(radiusX);
        ellipse.setRadiusY(radiusY);
    }

    public void Draw() {
        gc.setStroke(stroke);
        gc.setFill(fill);
        gc.setLineWidth(lineWidth);
        gc.fillOval(startX, startY, radiusX, radiusY);
        gc.strokeOval(startX, startY, radiusX, radiusY);
    }

    public void removeGraphic(Graphic g) {

    }
    public void addGraphic(Graphic g) {

    }

    public boolean isWithinPoint(Point p) {
        return ellipse.contains(new Point2D(p.x, p.y));
    }

    public Object clone() throws CloneNotSupportedException {
        Ellipse o = (Ellipse) super.clone();
        o.ellipse = new javafx.scene.shape.Ellipse(startX, startY, radiusX, radiusY);
        return o;
    }
}
