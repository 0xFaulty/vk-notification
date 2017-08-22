package com.defaulty.notivk.gui.service;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * The class {@code LinkMouseListener} представляет собой listener
 * для подсветки ссылок.
 */
public class LinkMouseListener extends MouseAdapter {

    private static Design design = Design.getInstance();
    private Runnable executePoint;

    public LinkMouseListener(Runnable executePoint) {
        this.executePoint = executePoint;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (!(e.getSource() instanceof JLabel)) {
            return;
        }
        JLabel label = (JLabel) e.getSource();
        label.setForeground(design.getMaxthonColor());
        Cursor cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
        label.setCursor(cursor);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (!(e.getSource() instanceof JLabel)) {
            return;
        }
        JLabel label = (JLabel) e.getSource();
        label.setForeground(design.getFirstForeColor());
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        executePoint.run();
    }
}
