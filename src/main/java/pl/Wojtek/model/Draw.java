package pl.Wojtek.model;

import com.vaadin.shared.ui.colorpicker.Color;
import pl.Wojtek.util.Point;

/**
 *
 */
public class Draw {

    private User user;
    private Point prevPoint;
    private Point newPoint;
    private String color;
    private double strokeSize;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public Point getPrevPoint() {
        return prevPoint;
    }

    public void setPrevPoint(Point prevPoint) {
        this.prevPoint = prevPoint;
    }

    public Point getNewPoint() {
        return newPoint;
    }

    public void setNewPoint(Point newPoint) {
        this.newPoint = newPoint;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public double getStrokeSize() {
        return strokeSize;
    }

    public void setStrokeSize(double strokeSize) {
        this.strokeSize = strokeSize;
    }

    @Override
    public String toString() {
        return "Draw(" + this.user + "/ " + this.getPrevPoint() + " -> " + this.getNewPoint() + ")";
    }

}
