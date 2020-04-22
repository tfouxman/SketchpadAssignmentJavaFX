package SketchpadAssignment;

import javafx.scene.canvas.GraphicsContext;

public class Scribble implements Graphic {

    private GraphicsContext gc;
    private double currX;
    private double currY;

    public Scribble(GraphicsContext g) {
        gc = g;
    }

    public void setPoint(double x, double y) {
        currX = x;
        currY = y;
        gc.lineTo(currX, currY);
    }

    public void Draw() {
        gc.stroke();
    }

    public void startPath() {
        gc.setLineWidth(gc.getLineWidth() / 2);
        gc.beginPath();
    }

    public void endPath() {
        gc.closePath();
        gc.setLineWidth(gc.getLineWidth() * 2);
    }

    public void removeGraphic(Graphic g) {

    }
    public void addGraphic(Graphic g) {

    }
}
