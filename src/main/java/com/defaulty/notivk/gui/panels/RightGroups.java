package com.defaulty.notivk.gui.panels;

import com.defaulty.notivk.gui.service.ButtonConstructor;
import com.defaulty.notivk.gui.service.Design;
import com.defaulty.notivk.gui.components.GroupAdd;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class RightGroups {

    private static Design design = Design.getInstance();

    private JPanel mainPanel = new JPanel();
    private JPanel headerPanel = new JPanel();
    private JPanel addPanel = new JPanel();

    public JPanel getPanel() {
        JButton addButton = new ButtonConstructor().getSimpleButton(
                "Добавить группу", e -> {
                    GroupAdd dialog = new GroupAdd("Добавить группу", "Введите название:", "Добавить");
                    dialog.pack();
                    dialog.setLocationRelativeTo(null); //Центр экрана
                    dialog.setVisible(true);
                });

        addPanel.setLayout(new BoxLayout(addPanel, BoxLayout.PAGE_AXIS));
        JPanel tmpPanel = new JPanel();
        tmpPanel.setLayout(new BorderLayout());
        tmpPanel.add(addPanel, BorderLayout.NORTH);
        JScrollPane centerPanel = new JScrollPane(tmpPanel);
        centerPanel.getVerticalScrollBar().setUnitIncrement(design.getScrollUnitIncrement());
        Border border = BorderFactory.createLineBorder(design.getBorderColor(), 0, true);
        centerPanel.setBorder(border);

        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(design.getBorderTopBottom());
        headerPanel.add(addButton, BorderLayout.EAST);

        mainPanel.setBorder(design.getBorderSmall());
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel);

        return mainPanel;
    }

    public void setVisible(boolean visible) {
        mainPanel.setVisible(visible);
        addPanel.setVisible(visible);
        headerPanel.setVisible(visible);
        mainPanel.setEnabled(visible);
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

    public void setBounds(int x, int y, int width, int height) {
        mainPanel.setBounds(x, y, width, height);
    }
}
