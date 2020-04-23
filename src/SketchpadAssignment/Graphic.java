package SketchpadAssignment;

public interface Graphic {
    public void Draw();
    public void setStart(double x, double y);
    public void setColour();
    public void removeGraphic(Graphic g);
    public void addGraphic(Graphic g);
    public Object clone() throws CloneNotSupportedException;
    public boolean isWithinPoint(Point p);
}
