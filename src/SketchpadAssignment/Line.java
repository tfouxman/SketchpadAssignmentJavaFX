package SketchpadAssignment;

import javafx.scene.canvas.GraphicsContext;

public class Line implements Graphic {

    private double startX, startY, endX, endY;
    private GraphicsContext gc;

    public Line(GraphicsContext g) {
        gc = g;
    }

    public void Draw() {
        gc.strokeLine(startX, startY, endX, endY);
    }

    public void setStart(double x, double y) {
        startX = x;
        startY = y;
    }

    public void setEnd(double x, double y) {
        endX = x;
        endY = y;
    }

    public Point getStart() {
        return new Point(startX, startY);
    }

    public void removeGraphic(Graphic g) {

    }
    public void addGraphic(Graphic g) {

    }

}
