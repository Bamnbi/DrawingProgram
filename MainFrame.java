package com.outprogram.ui;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;



public class MainFrame extends JFrame {
    private DrawingPanel drawingPanel;
    private JLabel statusLabel;
    private JCheckBoxMenuItem fillItem;

    public MainFrame() {
        initUI();
    }

    private void initUI() {
        setTitle("绘图程序 v1.0");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initMenuBar();
        initToolBar();
        initDrawingPanel();

        setVisible(true);
    }

    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("文件");
        fileMenu.setMnemonic('F');

        JMenuItem saveItem = new JMenuItem("保存");
        JMenuItem exitItem = new JMenuItem("退出");

        exitItem.addActionListener(e -> System.exit(0));

        saveItem.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("保存绘图");
            chooser.setSelectedFile(new File("drawing.png"));
            int result = chooser.showSaveDialog(MainFrame.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                if (!file.getName().toLowerCase().endsWith(".png")) {
                    file = new File(file.getAbsolutePath() + ".png");
                }
                if (drawingPanel.saveImage(file)) {
                    JOptionPane.showMessageDialog(this, "图像已保存！", "成功", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "保存失败！", "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        JMenu editMenu = new JMenu("编辑");
        editMenu.setMnemonic('E');

        JMenuItem undoItem = new JMenuItem("撤销");
        JMenuItem clearItem = new JMenuItem("清除画布");

        undoItem.addActionListener(e -> drawingPanel.undo());
        clearItem.addActionListener(e -> drawingPanel.clearAll());

        editMenu.add(undoItem);
        editMenu.add(clearItem);

        fillItem = new JCheckBoxMenuItem("实心填充");
        fillItem.setState(false);
        fillItem.addItemListener(e -> {
            boolean filled = e.getStateChange() == ItemEvent.SELECTED;
            drawingPanel.setFilled(filled);
        });
        editMenu.addSeparator();
        editMenu.add(fillItem);

        JMenu helpMenu = new JMenu("帮助");
        helpMenu.setMnemonic('H');
        JMenuItem aboutItem = new JMenuItem("关于");
        aboutItem.addActionListener(e ->
            JOptionPane.showMessageDialog(this, "绘图程序 v1.0\n使用 Java Swing 开发", "关于", JOptionPane.INFORMATION_MESSAGE)
        );
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    private void initToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        JButton btnLine = new JButton("直线");
        JButton btnRect = new JButton("矩形");
        JButton btnOval = new JButton("椭圆");
        JButton btnPolygon = new JButton("多边形");
        JButton btnClear = new JButton("清除");

        btnLine.addActionListener(e -> {
            drawingPanel.setDrawType("line");
            statusLabel.setText("就绪 - 当前工具: 直线");
        });
        btnRect.addActionListener(e -> {
            drawingPanel.setDrawType("rect");
            statusLabel.setText("就绪 - 当前工具: 矩形");
        });
        btnOval.addActionListener(e -> {
            drawingPanel.setDrawType("oval");
            statusLabel.setText("就绪 - 当前工具: 椭圆");
        });
        btnPolygon.addActionListener(e -> {
            drawingPanel.setDrawType("polygon");
            statusLabel.setText("就绪 - 当前工具: 多边形（点击添加顶点）");
        });
        btnClear.addActionListener(e -> {
            drawingPanel.clearAll();
            statusLabel.setText("画布已清除");
        });

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
        add(drawingPanel, BorderLayout.CENTER);

        statusLabel = new JLabel("就绪 - 当前工具: 直线");
        statusLabel.setBorder(BorderFactory.createLoweredBevelBorder());
        add(statusLabel, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getLookAndFeel());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new MainFrame();
        });
    }
}

