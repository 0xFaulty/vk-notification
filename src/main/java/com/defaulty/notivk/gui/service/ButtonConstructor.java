package com.defaulty.notivk.gui.service;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * The class {@code ButtonConstructor} используется для создания кнопок с
 * фиксированным оформлением.
 */
public class ButtonConstructor {

    private static Design design = Design.getInstance();

    public JPanel getFullPanelButton(String text, ActionListener listener, Dimension dim) {
        JButton newButton = getSimpleButton(text, listener);
        newButton.setPreferredSize(dim);

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

    public JButton getSimpleButton(String text, ActionListener listener) {
        JButton newButton = new JButton(text);
        newButton.addActionListener(listener);
        newButton.setFocusPainted(false);
        newButton.setBackground(design.getBackgroundColor());
        newButton.setFont(design.getFirstBoldFont());
        newButton.setForeground(design.getButtonForeColor());
        return newButton;
    }

}
