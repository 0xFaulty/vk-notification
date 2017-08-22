package com.defaulty.notivk.gui.panels;

import com.defaulty.notivk.gui.components.GroupAdd;
import com.defaulty.notivk.gui.service.ButtonConstructor;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * The class {@code RightGroups} представляет собой правую панель отвечающую за
 * отображения добавленных групп, а также за задание настроек для этих групп.
 */
public class RightGroups extends Panel {

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

}
