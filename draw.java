 package com.draw;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

public class DrawingPanel extends JPanel implements MouseListener, MouseMotionListener {
    // 绘图类型：line（直线）、rect（矩形）、oval（椭圆）、polygon（多边形）
    private String drawType;
    // 是否实心
    private boolean isFilled;
    // 鼠标起点、终点
    private int startX, startY, endX, endY;
    // 多边形顶点集合
    private List<Point> polygonPoints = new ArrayList<>();

    public DrawingPanel() {
        // 给画布添加鼠标监听器
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }

    // ========== 外部调用的方法（给成员A的按钮用） ==========
    // 设置当前绘图类型
    public void setDrawType(String type) {
        this.drawType = type;
        // 画多边形前清空之前的顶点
        if (type.equals("polygon")) {
            polygonPoints.clear();
        }
    }

    // 切换实心/空心
    public void setFilled(boolean filled) {
        this.isFilled = filled;
    }

    // ========== 重写paintComponent（核心绘图方法） ==========
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        if (drawType == null) return;

        switch (drawType) {
            case "line":
                g2d.drawLine(startX, startY, endX, endY);
                break;
            case "rect":
                int rectX = Math.min(startX, endX);
                int rectY = Math.min(startY, endY);
                int rectW = Math.abs(endX - startX);
                int rectH = Math.abs(endY - startY);
                if (isFilled) {
                    g2d.fillRect(rectX, rectY, rectW, rectH);
                } else {
                    g2d.drawRect(rectX, rectY, rectW, rectH);
                }
                break;
            case "oval":
                int ovalX = Math.min(startX, endX);
                int ovalY = Math.min(startY, endY);
                int ovalW = Math.abs(endX - startX);
                int ovalH = Math.abs(endY - startY);
                if (isFilled) {
                    g2d.fillOval(ovalX, ovalY, ovalW, ovalH);
                } else {
                    g2d.drawOval(ovalX, ovalY, ovalW, ovalH);
                }
                break;
            case "polygon":
                if (polygonPoints.size() >= 2) {
                    int[] xArr = polygonPoints.stream().mapToInt(Point::x).toArray();
                    int[] yArr = polygonPoints.stream().mapToInt(Point::y).toArray();
                    if (isFilled) {
                        g2d.fillPolygon(xArr, yArr, polygonPoints.size());
                    } else {
                        g2d.drawPolygon(xArr, yArr, polygonPoints.size());
                    }
                }
                break;
        }
    }

    // ========== 鼠标监听方法（处理交互） ==========
    @Override
    public void mousePressed(MouseEvent e) {
        startX = e.getX();
        startY = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        endX = e.getX();
        endY = e.getY();
        if (drawType != null && drawType.equals("polygon")) {
            polygonPoints.add(new Point(endX, endY));
        }
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        endX = e.getX();
        endY = e.getY();
        repaint();
    }

    // 其他鼠标监听方法（空实现）
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void mouseMoved(MouseEvent e) {}
}