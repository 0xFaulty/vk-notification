package com.defaulty.notivk.gui.panels;

import com.defaulty.notivk.gui.service.Design;

import javax.swing.*;

/**
 * The abstract class {@code Panel} включает в себя общие методы панелей меню.
 */
public abstract class Panel {

    static Design design = Design.getInstance();
    JPanel mainPanel = new JPanel();
    JPanel headerPanel = new JPanel();
    JPanel addPanel = new JPanel();

    public abstract JPanel getPanel();

    public void toggleVisible() {
        mainPanel.setVisible(!mainPanel.isVisible());
    }

    public boolean isVisible() {
        return mainPanel.isVisible();
    }

    public void setVisible(boolean visible) {
        mainPanel.setVisible(visible);
        addPanel.setVisible(visible);
        headerPanel.setVisible(visible);
    }

    public void add(JPanel panel) {
        addPanel.add(panel);
    }

    public void add(JPanel panel, int index) {
        addPanel.add(panel,index);
    }

    public void removeAll() {
        addPanel.removeAll();
    }

    public void repaintComponent() {
        addPanel.revalidate();
        addPanel.repaint();
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
