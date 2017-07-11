package com.defaulty.notivk.gui.service;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LinkMouseListener extends MouseAdapter {
    @Override
    public void mouseEntered(MouseEvent e) {
        if (!(e.getSource() instanceof JLabel)) {
            return;
        }
        //JLabel label = (JLabel)e.getSource();
        JPanel panel = (JPanel) e.getSource();
        //label.setForeground(Color.RED);
        Cursor cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
        panel.setCursor(cursor);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (!(e.getSource() instanceof JLabel)) {
            return;
        }
        JLabel label = (JLabel) e.getSource();
        label.setForeground(Color.BLUE);
    }
}
