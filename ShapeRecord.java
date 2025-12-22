package com.outprogram.ui;
import java.awt.*;
import java.io.Serializable;
import java.util.List;

public class ShapeRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public String type;
    public int x1, y1, x2, y2;
    public List<Point> points;
    public boolean filled;

    public ShapeRecord(String type, int x1, int y1, int x2, int y2, boolean filled) {
        this.type = type;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.filled = filled;
    }

    public ShapeRecord(String type, List<Point> points, boolean filled) {
        this.type = type;
        this.points = points;
        this.filled = filled;
    }
}