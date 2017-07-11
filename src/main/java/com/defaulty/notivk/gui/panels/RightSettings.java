package com.defaulty.notivk.gui.panels;

import com.defaulty.notivk.backend.SettingsWrapper;
import com.defaulty.notivk.gui.service.ButtonConstructor;
import com.defaulty.notivk.gui.service.Design;

import javax.swing.*;
import java.awt.*;

public class RightSettings {

    private static SettingsWrapper settings = SettingsWrapper.getInstance();
    private static Design design = Design.getInstance();

    private JPanel addPanel = new JPanel();
    private JPanel mainPanel = new JPanel();
    private JPanel headerPanel = new JPanel();

    private JCheckBox notifyCheckBox = new JCheckBox();

    public JPanel getPanel() {
        JButton resetButton = new ButtonConstructor().getSimpleButton(
                "Сбросить", e -> {
                    notifyCheckBox.setSelected(true);
                    settings.setNotifyType(true);
                    settings.save();
                });

        notifyCheckBox.setFont(design.getFirstFont());
        notifyCheckBox.setForeground(design.getSecondForeColor());
        notifyCheckBox.setText("Разрешить всплывающее окно уведомлений");
        notifyCheckBox.setFocusPainted(false);
        notifyCheckBox.setSelected(settings.getNotifyType());
        notifyCheckBox.addActionListener(e -> {
            settings.setNotifyType(notifyCheckBox.isSelected());
            settings.save();
        });

        addPanel.setLayout(new BoxLayout(addPanel, BoxLayout.PAGE_AXIS));
        addPanel.add(notifyCheckBox);
        JScrollPane centerPanel = new JScrollPane(addPanel);
        centerPanel.getVerticalScrollBar().setUnitIncrement(design.getScrollUnitIncrement());

        headerPanel.setLayout(new BorderLayout());
        headerPanel.add(resetButton, BorderLayout.EAST);
        headerPanel.setBorder(design.getBorderTopBottom());

        mainPanel.setBorder(design.getBorderSmall());
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(addPanel);

        return mainPanel;
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

    public void setBounds(int x, int y, int width, int height) {
        mainPanel.setBounds(x, y, width, height);
    }
}
