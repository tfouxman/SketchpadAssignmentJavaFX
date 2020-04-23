package SketchpadAssignment;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class Line implements Graphic, Cloneable {

    private double startX, startY, endX, endY;
    private GraphicsContext gc;
    private Paint stroke;
    private Paint fill;
    private double lineWidth;
    private javafx.scene.shape.Line line = new javafx.scene.shape.Line();

    public Line(GraphicsContext g) {
        gc = g;
        stroke = g.getStroke();
        fill = g.getFill();
        lineWidth = g.getLineWidth();
    }

    public void Draw() {
        gc.setStroke(stroke);
        gc.setFill(fill);
        gc.setLineWidth(lineWidth);
        gc.strokeLine(startX, startY, endX, endY);
    }

    public void setStart(double x, double y) {
        startX = x;
        startY = y;
        line.setStartX(x);
        line.setStartY(y);
    }

    public void setColour() {
        stroke = gc.getStroke();
        fill = gc.getFill();
        lineWidth = gc.getLineWidth();
    }
    public void setEnd(double x, double y) {
        endX = x;
        endY = y;
        line.setEndX(x);
        line.setEndY(y);
    }

    public Point getStart() {
        return new Point(startX, startY);
    }
    public Point getEnd() {
        return new Point(endX, endY);
    }

    public void removeGraphic(Graphic g) {

    }
    public void addGraphic(Graphic g) {

    }

    public boolean isWithinPoint(Point p) {
        return line.contains(new Point2D(p.x, p.y));
    }

    public Object clone() throws CloneNotSupportedException {
        Line o = (Line) super.clone();
        o.line = new javafx.scene.shape.Line(startX, startY, endX, endY);
        return o;
    }
}
