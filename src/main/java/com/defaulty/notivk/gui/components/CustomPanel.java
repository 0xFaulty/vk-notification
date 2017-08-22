package com.defaulty.notivk.gui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;

/**
 * The class {@code CustomPanel} предназначен для создания панели с расширенными насройками.
 */
public class CustomPanel extends JPanel {
    private int strokeThickness = 0;
    private int radius = 10;
    private int padding = 0;

    private Color bubbleColor = new Color(1.0f, 1.0f, 1.0f);

    public void setBubbleColor(Color bubbleColor) {
        this.bubbleColor = bubbleColor;
    }

    public void setStrokeThickness(int strokeThickness) {
        this.strokeThickness = strokeThickness;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }

    @Override
    protected void paintComponent(final Graphics g) {
        final Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(bubbleColor);
        int bottomLineY = getHeight();
        int width = getWidth() - (padding * 2);
        g2d.fillRect(padding, 0, width, bottomLineY);
        RoundRectangle2D.Double rect = new RoundRectangle2D.Double(padding, padding, width, bottomLineY, radius, radius);
        Area area = new Area(rect);
        g2d.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
        g2d.setStroke(new BasicStroke(strokeThickness));
        g2d.draw(area);
    }
}