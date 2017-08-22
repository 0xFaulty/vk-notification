package com.defaulty.notivk.gui.service;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * The class {@code Design} содержит все настройки оформления приложения.
 */
public class Design {

    private static Design ourInstance;

    public static Design getInstance() {
        if (ourInstance == null) ourInstance = new Design();
        return ourInstance;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }
    public Color getBorderColor() {
        return borderColor;
    }
    public Color getSecondForeColor() {
        return secondForeColor;
    }
    public Font getFirstFont() {
        return firstFont;
    }
    public Color getFirstForeColor() {
        return firstForeColor;
    }
    public Color getButtonForeColor() {
        return buttonForeColor;
    }
    public Font getFirstBoldFont() {
        return firstBoldFont;
    }
    public Font getSecondFont() {
        return secondFont;
    }
    public Font getSecondBoldFont() {
        return secondBoldFont;
    }
    public Color getThirdForeColor() {
        return thirdForeColor;
    }
    public Color getMaxthonColor() {
        return maxthonColor;
    }
    public Border getBorderSmall() {
        return borderSmall;
    }
    public Border getBorderTopBottom() {
        return borderTopBottom;
    }
    public Dimension getMenuButtSize() {
        return menuButtSize;
    }
    public int getScrollUnitIncrement() {
        return scrollUnitIncrement;
    }
    public Color getButtonEntered() {
        return buttonEntered;
    }
    public Border getBorderLarge() {
        return borderLarge;
    }

    private Color maxthonColor = new Color(51, 146, 240);
    private Color backgroundColor = new Color(237, 238, 240);
    private Color buttonEntered = new Color(225, 229, 235);
    private Color borderColor = new Color(0.9372549f, 0.9372549f, 0.9372549f);
    private Color firstForeColor = new Color(1, 1, 1);
    private Color secondForeColor = new Color(0.3372549f, 0.3372549f, 0.3372549f);
    private Color thirdForeColor = new Color(147, 147, 147);
    private Color buttonForeColor = new Color(76, 107, 143);
    private Font firstFont = new Font("Segoe UI", Font.PLAIN, 14);
    private Font firstBoldFont = new Font("Segoe UI", Font.BOLD, 14);
    private Font secondFont = new Font("Segoe UI", Font.PLAIN, 12);
    private Font secondBoldFont = new Font("Segoe UI", Font.BOLD, 12);

    private int borderLeastValue = 1;
    private int borderSmallValue = 5;
    private int borderLargeValue = 15;
    private Border borderSmall = BorderFactory.createEmptyBorder(borderSmallValue, borderSmallValue + 5, borderSmallValue, borderSmallValue);
    private Border borderTopBottom = BorderFactory.createEmptyBorder(borderSmallValue, borderLeastValue, borderSmallValue, borderLeastValue);
    private Border borderLarge = BorderFactory.createEmptyBorder(borderSmallValue, borderLargeValue, borderSmallValue, borderLargeValue);

    private int scrollUnitIncrement = 25;

    private Dimension menuButtSize = new Dimension(200, 50);

}
