package com.defaulty.notivk.gui.panels;

import com.defaulty.notivk.gui.GUI;
import com.defaulty.notivk.gui.service.ButtonConstructor;
import com.defaulty.notivk.gui.service.Design;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class RightPosts {

    private static Design design = Design.getInstance();

    private JPanel addPanel = new JPanel();
    private JPanel mainPanel = new JPanel();
    private JPanel headerPanel = new JPanel();
    private JScrollPane rightPostsScroll; //Фиксит то, что из-за TextLabel увеличивается размер формы

    public JPanel getPanel() {
        JButton menuButton = new ButtonConstructor().getSimpleButton("|||", e -> GUI.getInstance().toggleMenuVisible());
        JButton resetButton = new ButtonConstructor().getSimpleButton("Обновить", e -> GUI.getInstance().refreshList());

        JPanel eastPanel = new JPanel();
        eastPanel.setLayout(new BorderLayout(10, 0));
        eastPanel.add(menuButton, BorderLayout.WEST);

        headerPanel.setLayout(new BorderLayout());
        headerPanel.add(eastPanel, BorderLayout.CENTER);
        headerPanel.add(resetButton, BorderLayout.EAST);
        headerPanel.setBorder(design.getBorderTopBottom());

        addPanel.setLayout(new BoxLayout(addPanel, BoxLayout.PAGE_AXIS));
        //rightPostsScroll = new JScrollPane(addPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        rightPostsScroll = new JScrollPane(addPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        rightPostsScroll.getVerticalScrollBar().setUnitIncrement(design.getScrollUnitIncrement());
        Border border = BorderFactory.createLineBorder(design.getBorderColor(), 0, true);
        rightPostsScroll.setBorder(border);
        rightPostsScroll.addPropertyChangeListener(evt -> GUI.getInstance().resizePanels());

        mainPanel.setBorder(design.getBorderSmall());
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(rightPostsScroll);

        return mainPanel;
    }

    public void updateScrollsPos() {
        //TODO: Возможны однажды я узнаю как обойтись без этой ф-ии
        int max = rightPostsScroll.getHorizontalScrollBar().getMaximum();
        int width = GUI.getInstance().getWidth();
        int lwidth = mainPanel.getWidth();
        int scrollPos = max / 2 - (width - lwidth) / 2 - 170;
        rightPostsScroll.getHorizontalScrollBar().setValue(scrollPos);
    }

    public void setVisible(boolean visible) {
        mainPanel.setVisible(visible);
    }

    public void add(JPanel panel) {
        addPanel.add(panel);
    }

    public void removeAll() {
        addPanel.removeAll();
    }

    public void updateUI() {
        addPanel.updateUI();
    }

    public int getWidth() {
        return mainPanel.getWidth();
    }

    public int getHeight() {
        return mainPanel.getHeight();
    }

    public int getHeaderHeight() {
        return headerPanel.getHeight();
    }

    public void setBounds(int x, int y, int width, int height) {
        mainPanel.setBounds(x, y, width, height);
    }
}
