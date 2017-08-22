package com.defaulty.notivk.gui.panels;

import com.defaulty.notivk.backend.SettingsWrapper;
import com.defaulty.notivk.gui.service.ButtonConstructor;

import javax.swing.*;
import java.awt.*;

/**
 * The class {@code RightSettings} представляет собой правую панель содержащую
 * настройки приложения.
 */
public class RightSettings extends Panel {

    private static SettingsWrapper settings = SettingsWrapper.getInstance();
    private JCheckBox notifyCheckBox = new JCheckBox();

    public JPanel getPanel() {
        JButton resetButton = new ButtonConstructor().getSimpleButton(
                "Сбросить", e -> {
                    notifyCheckBox.setSelected(true);
                    settings.setNotifyState(true);
                    settings.save();
                });

        notifyCheckBox.setFont(design.getFirstFont());
        notifyCheckBox.setForeground(design.getSecondForeColor());
        notifyCheckBox.setText("Разрешить всплывающее окно уведомлений");
        notifyCheckBox.setFocusPainted(false);
        notifyCheckBox.setSelected(settings.getNotifyState());
        notifyCheckBox.addActionListener(e -> {
            settings.setNotifyState(notifyCheckBox.isSelected());
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

}
