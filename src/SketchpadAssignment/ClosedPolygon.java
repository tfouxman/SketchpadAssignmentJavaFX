package SketchpadAssignment;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;

import java.util.ArrayList;

public class ClosedPolygon implements Graphic, Cloneable {

    private double startX, startY, endX, endY;
    private int clicks = 0;
    private GraphicsContext gc;
    private Paint stroke;
    private Paint fill;
    private double lineWidth;
    public ArrayList<Line> lines = new ArrayList<>();
    private Polygon poly = new Polygon();

    public ClosedPolygon(GraphicsContext g) {
        gc = g;
        stroke = g.getStroke();
        fill = g.getFill();
        lineWidth = g.getLineWidth();
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
        } else if (clicks == -2) {
            Line line = new Line(gc);
            line.setStart(endX, endY);
            line.setEnd(lines.get(0).getStart().x, lines.get(0).getStart().y);
            lines.add(line);
            line.Draw();
            double[] x = new double[lines.size()];
            double[] y = new double[lines.size()];
            int count = 0;
            for ( Line l : lines
                 ) {
                poly.getPoints().addAll(new Double[]{l.getStart().x, l.getStart().y});
                x[count] = l.getStart().x;
                y[count] = l.getStart().y;
                count++;
            }
            gc.fillPolygon(x, y, count);
        }
        else {
            poly.getPoints().clear();
            double[] x = new double[lines.size()];
            double[] y = new double[lines.size()];
            int count = 0;
            for ( Line l : lines
            ) {
                poly.getPoints().addAll(new Double[]{l.getStart().x, l.getStart().y});
                x[count] = l.getStart().x;
                y[count] = l.getStart().y;
                count++;
                l.Draw();
            }
            gc.fillPolygon(x, y, count);

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
        lines.clear();
    }

    public void removeGraphic(Graphic g) {

    }
    public void addGraphic(Graphic g) {

    }

    public boolean isWithinPoint(Point p) {
        return poly.contains(p.x, p.y);
    }

    public void setClicks(int i) {
        clicks = i;
    }

    public Object clone() throws CloneNotSupportedException {
        ClosedPolygon o = (ClosedPolygon) super.clone();
        o.poly = new Polygon();
        o.lines = new ArrayList<Line>();
        int count = 0;
        for ( Line l : lines
        ) {
            o.lines.add((Line)l.clone());
            o.poly.getPoints().addAll(new Double[]{o.lines.get(count).getStart().x, o.lines.get(count).getStart().y});
            count++;
        }
        return o;
    }
}
