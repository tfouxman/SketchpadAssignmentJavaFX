package SketchpadAssignment;

import javafx.scene.canvas.GraphicsContext;

public class Circle implements Graphic {

    private double startX, startY, endX, endY, radius;
    private GraphicsContext gc;

    public Circle(GraphicsContext g) {
        gc = g;
    }

    public void setStart(double x, double y) {
        startX = x;
        startY = y;
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
    }

    public void Draw() {
        gc.fillOval(startX, startY, radius, radius);
        gc.strokeOval(startX, startY, radius, radius);
    }

    public void removeGraphic(Graphic g) {

    }
    public void addGraphic(Graphic g) {

    }
}
