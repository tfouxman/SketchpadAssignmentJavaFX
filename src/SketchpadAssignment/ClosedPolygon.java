package SketchpadAssignment;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

public class ClosedPolygon implements Graphic {

    private double startX, startY, endX, endY;
    private int clicks = 0;
    private GraphicsContext gc;
    private ArrayList<Line> lines = new ArrayList<>();

    public ClosedPolygon(GraphicsContext g) {
        gc = g;
    }

    public void Draw() {
        if (clicks > 1) {
            Line line = new Line(gc);
            line.setStart(startX, startY);
            line.setEnd(endX, endY);
            lines.add(line);
            line.Draw();
            startX = endX;
            startY = endY;
        }
    }

    public void setPoint(double x, double y) {
        if (clicks == 0) {
            startX = x;
            startY = y;
        }
        else {
            endX = x;
            endY = y;
        }
        clicks++;
    }

    public void reset() {
        clicks = 0;
        Line line = new Line(gc);
        line.setStart(endX, endY);
        line.setEnd(lines.get(0).getStart().x, lines.get(0).getStart().y);
        lines.add(line);
        line.Draw();
        lines.clear();
    }

    public void removeGraphic(Graphic g) {

    }
    public void addGraphic(Graphic g) {

    }
}
