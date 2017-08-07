package com.defaulty.notivk.gui.panels;

import com.defaulty.notivk.gui.GUI;
import com.defaulty.notivk.gui.service.ButtonConstructor;
import com.defaulty.notivk.gui.service.Design;

import javax.swing.*;
import java.awt.*;

public class LeftMenu extends Panel{

    public JPanel getPanel() {
        mainPanel.setPreferredSize(new Dimension(200, GUI.getInstance().getHeight()));

        Dimension buttSize = design.getMenuButtSize();

        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));
        buttons.setBorder(design.getBorderSmall());

        buttons.add(new ButtonConstructor().getFullPanelButton(
                "Новые", e -> GUI.getInstance().setTopPanel(EnumPanels.Posts), buttSize));
        buttons.add(new ButtonConstructor().getFullPanelButton(
                "Группы", e -> GUI.getInstance().setTopPanel(EnumPanels.Groups), buttSize));
        buttons.add(new ButtonConstructor().getFullPanelButton(
                "Настройки", e -> GUI.getInstance().setTopPanel(EnumPanels.Settings), buttSize));

        JPanel leftButtonsWrapper = new JPanel();
        leftButtonsWrapper.setLayout(new BorderLayout());
        leftButtonsWrapper.add(buttons, BorderLayout.NORTH);

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(addPanel, BorderLayout.NORTH);
        mainPanel.add(leftButtonsWrapper, BorderLayout.CENTER);

        return mainPanel;
    }

}
