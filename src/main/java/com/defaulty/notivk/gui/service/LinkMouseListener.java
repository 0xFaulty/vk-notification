package com.defaulty.notivk.gui.service;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LinkMouseListener extends MouseAdapter {

    private static Design design = Design.getInstance();
    private ExecutePoint executePoint;

    public LinkMouseListener(ExecutePoint executePoint) {
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
        label.setForeground(design.getThirdForeColor());
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        executePoint.execute();
    }
}
