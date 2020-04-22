package SketchpadAssignment;

import javafx.scene.canvas.GraphicsContext;

public class Ellipse implements Graphic {

    private double startX, startY, endX, endY, radiusX, radiusY;
    private GraphicsContext gc;

    public Ellipse(GraphicsContext g) {
        gc = g;
    }

    public void setStart(double x, double y) {
        startX = x;
        startY = y;
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
    }

    public void Draw() {
        gc.fillOval(startX, startY, radiusX, radiusY);
        gc.strokeOval(startX, startY, radiusX, radiusY);
    }

    public void removeGraphic(Graphic g) {

    }
    public void addGraphic(Graphic g) {

    }
}
