package pl.Wojtek.util;

/**
 *
 */
public class Point {
    private double x;
    private double y;

    public Point(Double x, Double y){
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "<" + this.x + "," + this.y + ">";
    }
}
