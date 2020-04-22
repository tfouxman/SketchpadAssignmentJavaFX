package SketchpadAssignment;

import javafx.scene.canvas.GraphicsContext;

public class Square implements Graphic {

    private double startX, startY, endX, endY, width, height;
    private GraphicsContext gc;

    public Square(GraphicsContext g) {
        gc = g;
    }

    public void setStart(double x, double y) {
        startX = x;
        startY = y;
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
    }

    public void Draw() {
        if (width > height) {
            gc.fillRect(startX, startY, width, width);
            gc.strokeRect(startX, startY, width, width);
        }
        else {
            gc.fillRect(startX, startY, height, height);
            gc.strokeRect(startX, startY, height, height);
        }

    }

    public void removeGraphic(Graphic g) {

    }
    public void addGraphic(Graphic g) {

    }
}
