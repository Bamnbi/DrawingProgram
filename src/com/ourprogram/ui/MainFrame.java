package com.ourprogram.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainFrame extends JFrame {
    // 组件声明
    private DrawingPanel drawingPanel;
    private JMenuBar menuBar;
    private JToolBar toolBar;
    
    // 按钮
    private JButton btnLine, btnRect, btnOval, btnPolygon, btnClear;
    
    // 菜单项
    private JMenuItem newItem, saveItem, exitItem, undoItem, clearItem;
    
    // 当前工具状态
    private String currentTool = "line";
    
    public MainFrame() {
        // 初始化界面
        initUI();
    }
    
    private void initUI() {
        // 基本窗口设置
        setTitle("绘图程序 v1.0");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        // 初始化菜单栏
        initMenuBar();
        
        // 初始化工具栏
        initToolBar();
        
        // 初始化绘图面板
        initDrawingPanel();
        
        // 设置窗口可见
        setVisible(true);
    }
    
    private void initMenuBar() {
        menuBar = new JMenuBar();
        
        // 文件菜单
        JMenu fileMenu = new JMenu("文件");
        fileMenu.setMnemonic('F');
        
        newItem = new JMenuItem("新建");
        saveItem = new JMenuItem("保存");
        exitItem = new JMenuItem("退出");
        
        // 添加退出功能
        exitItem.addActionListener(e -> System.exit(0));
        
        fileMenu.add(newItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        
        // 编辑菜单
        JMenu editMenu = new JMenu("编辑");
        editMenu.setMnemonic('E');
        
        undoItem = new JMenuItem("撤销");
        clearItem = new JMenuItem("清除画布");
        
        // 添加清除功能
        clearItem.addActionListener(e -> drawingPanel.clear());
        
        editMenu.add(undoItem);
        editMenu.add(clearItem);
        
        // 帮助菜单
        JMenu helpMenu = new JMenu("帮助");
        helpMenu.setMnemonic('H');
        JMenuItem aboutItem = new JMenuItem("关于");
        helpMenu.add(aboutItem);
        
        // 添加到菜单栏
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(Box.createHorizontalGlue()); // 右对齐
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
    }
    
    private void initToolBar() {
        toolBar = new JToolBar();
        toolBar.setFloatable(false); // 固定工具栏
        
        // 创建工具按钮
        btnLine = new JButton("画直线");
        btnRect = new JButton("画矩形");
        btnOval = new JButton("画椭圆");
        btnPolygon = new JButton("画多边形");
        btnClear = new JButton("清除");
        
        // 为按钮添加事件监听器
        btnLine.addActionListener(e -> {
            System.out.println("点击了画直线");
            currentTool = "line";
            updateStatus();
        });
        
        btnRect.addActionListener(e -> {
            System.out.println("点击了画矩形");
            currentTool = "rect";
            updateStatus();
        });
        
        btnOval.addActionListener(e -> {
            System.out.println("点击了画椭圆");
            currentTool = "oval";
            updateStatus();
        });
        
        btnPolygon.addActionListener(e -> {
            System.out.println("点击了画多边形");
            currentTool = "polygon";
            updateStatus();
        });
        
        btnClear.addActionListener(e -> {
            drawingPanel.clear(); // 清除绘图区内容
            System.out.println("已清除绘图区");
        });
        
        // 添加按钮到工具栏
        toolBar.add(btnLine);
        toolBar.add(btnRect);
        toolBar.add(btnOval);
        toolBar.add(btnPolygon);
        toolBar.addSeparator();
        toolBar.add(btnClear);
        
        add(toolBar, BorderLayout.NORTH);
    }
    
    private void initDrawingPanel() {
        drawingPanel = new DrawingPanel();
        drawingPanel.setPreferredSize(new Dimension(800, 500));
        add(drawingPanel, BorderLayout.CENTER);
        
        // 状态栏
        JLabel statusLabel = new JLabel("就绪 - 当前工具: 直线");
        statusLabel.setBorder(BorderFactory.createLoweredBevelBorder());
        add(statusLabel, BorderLayout.SOUTH);
    }
    
    private void updateStatus() {
        System.out.println("当前工具: " + currentTool);
    }
    
    // 主方法
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            new MainFrame();
        });
    }
    
    // ============ 内部类：绘图面板 ============
    class DrawingPanel extends JPanel {
        private static final long serialVersionUID = 1L; // 修复序列化警告
        
        // 存储绘图的点
        private java.util.List<Point> points = new java.util.ArrayList<>();
        
        public DrawingPanel() {
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createLineBorder(Color.GRAY));
            
            // 设置鼠标监听器
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    System.out.println("绘图区被点击，坐标：" + e.getX() + ", " + e.getY());
                    
                    // 根据当前工具处理点击
                    switch (currentTool) {
                        case "line":
                            points.add(new Point(e.getX(), e.getY()));
                            if (points.size() >= 2) {
                                repaint();
                            }
                            break;
                        case "rect":
                        case "oval":
                        case "polygon":
                            // 其他工具暂时简单处理
                            points.add(new Point(e.getX(), e.getY()));
                            repaint();
                            break;
                    }
                }
            });
            
            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    System.out.println("鼠标拖动中...");
                }
            });
        }
        
        // 清除方法 - 修复未定义错误
        public void clear() {
            points.clear();
            repaint();
            System.out.println("绘图区已清除");
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            // 设置绘图颜色
            g.setColor(Color.BLACK);
                
            // 绘制提示文字
            g.setColor(Color.LIGHT_GRAY);
            g.drawString("在绘图区点击进行绘制", 350, 250);
            g.drawString("当前工具: " + currentTool, 350, 270);
            
            // 绘制已有点
            g.setColor(Color.RED);
            for (Point p : points) {
                g.fillOval(p.x - 3, p.y - 3, 6, 6);
            }
            
            // 根据当前工具绘制
            if (points.size() >= 2 && currentTool.equals("line")) {
                g.setColor(Color.BLUE);
                for (int i = 0; i < points.size() - 1; i++) {
                    Point p1 = points.get(i);
                    Point p2 = points.get(i+1);
                    g.drawLine(p1.x, p1.y, p2.x, p2.y);
                    }
                }
            }
        }
    }
   