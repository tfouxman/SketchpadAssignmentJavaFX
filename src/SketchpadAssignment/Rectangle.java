package SketchpadAssignment;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class Rectangle implements Graphic, Cloneable {

    private double startX, startY, endX, endY, width, height;
    private GraphicsContext gc;
    private Paint stroke;
    private Paint fill;
    private double lineWidth;

    private javafx.scene.shape.Rectangle rect = new javafx.scene.shape.Rectangle();

    public Rectangle(GraphicsContext g) {
        gc = g;
        stroke = g.getStroke();
        fill = g.getFill();
        lineWidth = g.getLineWidth();
    }

    public void setStart(double x, double y) {
        startX = x;
        startY = y;
        rect.setX(startX);
        rect.setY(startY);
    }

    public void setEnd(double x, double y) {
        endX = x;
        endY = y;
        width = Math.abs((endX - startX));
        height = Math.abs((endY - startY));
        if (startX > endX) {
            startX = endX;
        }
        if (startY > endY) {
            startY = endY;
        }
        rect.setX(startX);
        rect.setY(startY);
        rect.setWidth(width);
        rect.setHeight(height);
    }
    
    public void Draw() {
        gc.setStroke(stroke);
        gc.setFill(fill);
        gc.setLineWidth(lineWidth);
        gc.fillRect(startX, startY, width, height);
        gc.strokeRect(startX, startY, width, height);
    }

    public void removeGraphic(Graphic g) {

    }
    public void addGraphic(Graphic g) {

    }

    public boolean isWithinPoint(Point p) {
        return rect.contains(new Point2D(p.x, p.y));
    }

    public void setColour() {
        stroke = gc.getStroke();
        fill = gc.getFill();
        lineWidth = gc.getLineWidth();
    }

    public Object clone() throws CloneNotSupportedException {
        Rectangle o = (Rectangle) super.clone();
        o.rect = new javafx.scene.shape.Rectangle(startX, startY, width, height);
        return o;
    }
}
