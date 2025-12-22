package com.outprogram.ui;  


import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class DrawingPanel extends JPanel implements MouseListener, MouseMotionListener {
    private String drawType = "line";
    private boolean isFilled = false;
    private int startX, startY, endX, endY;
    private List<Point> tempPolygonPoints = new ArrayList<>();
    private List<ShapeRecord> history = new ArrayList<>();
    private boolean dragging = false;

    public DrawingPanel() {
        setBackground(Color.WHITE);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public void setDrawType(String type) {
        this.drawType = type;
        if ("polygon".equals(type)) {
            tempPolygonPoints.clear();
        }
        repaint();
    }

    public void setFilled(boolean filled) {
        this.isFilled = filled;
        repaint();
    }

    public void clearAll() {
        history.clear();
        tempPolygonPoints.clear();
        repaint();
    }

    public void undo() {
        if (!history.isEmpty()) {
            history.remove(history.size() - 1);
            repaint();
        }
    }

    public boolean canUndo() {
        return !history.isEmpty();
    }

    public boolean saveImage(File file) {
        int w = getWidth();
        int h = getHeight();
        if (w <= 0 || h <= 0) return false;

        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(getBackground());
        g2d.fillRect(0, 0, w, h);
        paintHistory(g2d);
        g2d.dispose();

        try {
            return ImageIO.write(image, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void paintHistory(Graphics2D g2d) {
        for (ShapeRecord record : history) {
            switch (record.type) {
                case "line":
                    g2d.setColor(Color.BLUE);
                    g2d.drawLine(record.x1, record.y1, record.x2, record.y2);
                    break;
                case "rect":
                    g2d.setColor(Color.GREEN);
                    int rx = Math.min(record.x1, record.x2);
                    int ry = Math.min(record.y1, record.y2);
                    int rw = Math.abs(record.x2 - record.x1);
                    int rh = Math.abs(record.y2 - record.y1);
                    if (record.filled) g2d.fillRect(rx, ry, rw, rh);
                    else g2d.drawRect(rx, ry, rw, rh);
                    break;
                case "oval":
                    g2d.setColor(Color.RED);
                    int ox = Math.min(record.x1, record.x2);
                    int oy = Math.min(record.y1, record.y2);
                    int ow = Math.abs(record.x2 - record.x1);
                    int oh = Math.abs(record.y2 - record.y1);
                    if (record.filled) g2d.fillOval(ox, oy, ow, oh);
                    else g2d.drawOval(ox, oy, ow, oh);
                    break;
                case "polygon":
                    if (record.points != null && record.points.size() >= 3) { // ✅ 至少3点
                        g2d.setColor(Color.MAGENTA);
                        int n = record.points.size();
                        int[] xArr = new int[n];
                        int[] yArr = new int[n];
                        for (int i = 0; i < n; i++) {
                            Point p = record.points.get(i);
                            xArr[i] = p.x;
                            yArr[i] = p.y;
                        }
                        if (record.filled) g2d.fillPolygon(xArr, yArr, n);
                        else g2d.drawPolygon(xArr, yArr, n);
                    }
                    break;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        paintHistory(g2d);

        // 绘制正在拖拽的图形（非多边形）
        if (dragging && !"polygon".equals(drawType)) {
            g2d.setColor(Color.GRAY);
            switch (drawType) {
                case "line":
                    g2d.drawLine(startX, startY, endX, endY);
                    break;
                case "rect":
                    int rx = Math.min(startX, endX);
                    int ry = Math.min(startY, endY);
                    int rw = Math.abs(endX - startX);
                    int rh = Math.abs(endY - startY);
                    if (isFilled) g2d.fillRect(rx, ry, rw, rh);
                    else g2d.drawRect(rx, ry, rw, rh);
                    break;
                case "oval":
                    int ox = Math.min(startX, endX);
                    int oy = Math.min(startY, endY);
                    int ow = Math.abs(endX - startX);
                    int oh = Math.abs(endY - startY);
                    if (isFilled) g2d.fillOval(ox, oy, ow, oh);
                    else g2d.drawOval(ox, oy, ow, oh);
                    break;
            }
        }

        // 实时预览多边形（临时线）
        if ("polygon".equals(drawType)) {
            g2d.setColor(Color.MAGENTA);
            // 已点击的点之间连线
            if (tempPolygonPoints.size() >= 2) {
                for (int i = 0; i < tempPolygonPoints.size() - 1; i++) {
                    Point p1 = tempPolygonPoints.get(i);
                    Point p2 = tempPolygonPoints.get(i + 1);
                    g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
                }
            }
            // 最后一个点连到鼠标当前位置
            if (!tempPolygonPoints.isEmpty()) {
                Point last = tempPolygonPoints.get(tempPolygonPoints.size() - 1);
                g2d.drawLine(last.x, last.y, endX, endY);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        startX = e.getX();
        startY = e.getY();
        dragging = true;
        if ("polygon".equals(drawType)) {
            // ✅ 修复：添加当前鼠标位置作为顶点
            tempPolygonPoints.add(new Point());
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        endX = e.getX();
        endY = e.getY();
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        endX = e.getX();
        endY = e.getY();
        dragging = false;
        // 非多边形图形直接保存
        if (!"polygon".equals(drawType)) {
            history.add(new ShapeRecord(drawType, startX, startY, endX, endY, isFilled));
            repaint();
        }
        // 多边形不在此处保存（由 mouseClicked 双击完成）
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // ✅ 双击完成多边形
        if ("polygon".equals(drawType) && e.getClickCount() == 2) {
            if (tempPolygonPoints.size() >= 3) {
                history.add(new ShapeRecord("polygon", new ArrayList<>(tempPolygonPoints), isFilled));
            }
            tempPolygonPoints.clear();
            repaint();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {
        endX = e.getX();
        endY = e.getY();
        if ("polygon".equals(drawType)) {
            repaint(); // 实时更新预览线
        }
    }
}