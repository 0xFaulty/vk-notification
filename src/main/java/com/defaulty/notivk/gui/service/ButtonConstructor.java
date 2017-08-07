package com.defaulty.notivk.gui.service;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ButtonConstructor {

    private static Design design = Design.getInstance();

    public JPanel getFullPanelButton(String text, ActionListener actionListener, Dimension dimension) {
        JButton newButton = getSimpleButton(text, actionListener);
        newButton.setPreferredSize(dimension);

        newButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                newButton.setBackground(design.getButtonEntered());
                super.mouseEntered(e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                newButton.setBackground(design.getBackgroundColor());
                super.mouseExited(e);
            }
        });

        JPanel newPanel = new JPanel();
        newButton.setBorder(design.getBorderSmall());
        newPanel.add(newButton);
        return newPanel;
    }

    public JButton getSimpleButton(String text, ActionListener actionListener) {
        JButton newButton = new JButton(text);
        newButton.addActionListener(actionListener);
        newButton.setFocusPainted(false);
        newButton.setBackground(design.getBackgroundColor());
        newButton.setFont(design.getFirstBoldFont());
        newButton.setForeground(design.getButtonForeColor());
        return newButton;
    }

}
