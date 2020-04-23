package SketchpadAssignment;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

import java.util.ArrayList;

public class OpenPolygon implements Graphic, Cloneable {

    private double startX, startY, endX, endY;
    private int clicks = 0;
    private GraphicsContext gc;
    private Paint stroke;
    private Paint fill;
    private double lineWidth;
    public ArrayList<Line> lines = new ArrayList<>();

    public OpenPolygon(GraphicsContext g) {
        gc = g;
        stroke = g.getStroke();
        fill = g.getFill();
        lineWidth = g.getLineWidth();
    }

    public void Draw() {
        if (clicks >= 0) {
            gc.setStroke(stroke);
            gc.setFill(fill);
            gc.setLineWidth(lineWidth);
            if (clicks > 1) {
                Line line = new Line(gc);
                line.setStart(startX, startY);
                line.setEnd(endX, endY);
                lines.add(line);
                line.Draw();
                startX = endX;
                startY = endY;
            }
        } else {

            for ( Line l : lines
            ) {
                l.Draw();
            }

        }
    }

    public void setStart(double x, double y) {
        startX = x;
        startY = y;
    }

    public void setColour() {
        stroke = gc.getStroke();
        fill = gc.getFill();
        lineWidth = gc.getLineWidth();
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
        lines.clear();
    }

    public void removeGraphic(Graphic g) {

    }
    public void addGraphic(Graphic g) {

    }

    public boolean isWithinPoint(Point p) {
        for (Line l : lines
             ) {
            if (l.isWithinPoint(p)) return true;
        }
        return false;
    }

    public void setClicks(int i) {
        clicks = i;
    }

    public Object clone() throws CloneNotSupportedException {
        OpenPolygon o = (OpenPolygon) super.clone();
        o.lines = new ArrayList<Line>();
        for ( Line l : lines
             ) {
            o.lines.add((Line)l.clone());
        }
        return o;
    }
}
