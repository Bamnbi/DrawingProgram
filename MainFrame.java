package com.outprogram.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
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
    
    // 状态栏标签
    private JLabel statusLabel;

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
        
        // 创建状态栏
        statusLabel = new JLabel(" 当前工具: " + currentTool);
        statusLabel.setBorder(BorderFactory.createLoweredBevelBorder());
        add(statusLabel, BorderLayout.SOUTH);
        
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
        aboutItem.addActionListener(e -> 
            JOptionPane.showMessageDialog(MainFrame.this,
                "绘图程序 v1.0\n作者：YourName\n使用 Swing 开发",
                "关于", JOptionPane.INFORMATION_MESSAGE)
        );
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

        // 为按钮添加事件监听器（带弹窗测试）
        btnLine.addActionListener(e -> {
            System.out.println("点击了画直线");
            currentTool = "line";
            updateStatus();
            // 🔔 事件测试：弹出提示
            JOptionPane.showMessageDialog(this,
                "已切换到【画直线】工具",
                "工具切换提示",
                JOptionPane.INFORMATION_MESSAGE);
        });

        btnRect.addActionListener(e -> {
            System.out.println("点击了画矩形");
            currentTool = "rect";
            updateStatus();
            JOptionPane.showMessageDialog(this,
                "已切换到【画矩形】工具",
                "工具切换提示",
                JOptionPane.INFORMATION_MESSAGE);
        });

        btnOval.addActionListener(e -> {
            System.out.println("点击了画椭圆");
            currentTool = "oval";
            updateStatus();
            JOptionPane.showMessageDialog(this,
                "已切换到【画椭圆】工具",
                "工具切换提示",
                JOptionPane.INFORMATION_MESSAGE);
        });

        btnPolygon.addActionListener(e -> {
            System.out.println("点击了画多边形");
            currentTool = "polygon";
            updateStatus();
            JOptionPane.showMessageDialog(this,
                "已切换到【画多边形】工具",
                "工具切换提示",
                JOptionPane.INFORMATION_MESSAGE);
        });

        btnClear.addActionListener(e -> {
            drawingPanel.clear();
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
    }
    
    private void updateStatus() {
        statusLabel.setText(" 当前工具: " + currentTool);
        System.out.println("当前工具: " + currentTool);
    }
    
    // 主方法
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getLookAndFeel());
            } catch (Exception ignored) { }
            new MainFrame();
        });
    }
    
    // ============ 内部类：绘图面板 ============
    class DrawingPanel extends JPanel {
        private static final long serialVersionUID = 1L;
        
        // 存储绘图的点
        private List<Point> points = new ArrayList<>();
        
        public DrawingPanel() {
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createLineBorder(Color.GRAY));
            
            // 鼠标点击监听器
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    System.out.println("绘图区被点击，坐标：" + e.getX() + ", " + e.getY());
                    
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
                            points.add(new Point(e.getX(), e.getY()));
                            repaint();
                            break;
                    }
                }
            });
            
            // 鼠标拖动监听器
            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    System.out.println("鼠标拖动中...");
                }
            });
        }
        
        // 清除画布
        public void clear() {
            points.clear();
            repaint();
            System.out.println("绘图区已清除");
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            // 提示文字
            if (points.isEmpty()) {
                g.setColor(Color.LIGHT_GRAY);
                g.setFont(new Font("微软雅黑", Font.PLAIN, 14));
                g.drawString("在绘图区点击进行绘制", getWidth() / 2 - 60, getHeight() / 2);
                g.drawString("当前工具: " + currentTool, getWidth() / 2 - 60, getHeight() / 2 + 20);
            }

            // 绘制点
            g.setColor(Color.RED);
            for (Point p : points) {
                g.fillOval(p.x - 3, p.y - 3, 6, 6);
            }

            // 绘制线段（仅直线模式演示连接）
            if (points.size() >= 2 && currentTool.equals("line")) {
                g.setColor(Color.BLUE);
                for (int i = 0; i < points.size() - 1; i++) {
                    Point p1 = points.get(i);
                    Point p2 = points.get(i + 1);
                    g.drawLine(p1.x, p1.y, p2.x, p2.y);
                }
            }
        }
    }
}
