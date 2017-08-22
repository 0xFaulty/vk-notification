package com.defaulty.notivk.gui.panels;

import com.defaulty.notivk.gui.GUI;
import com.defaulty.notivk.gui.service.ButtonConstructor;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * The class {@code RightPosts} представляет собой главную правую панель
 * приложения и отвечает за отображение полученных из групп постов.
 */
public class RightPosts extends Panel {

    private JScrollPane rightPostsScroll;

    public JPanel getPanel() {
        JButton menuButton = new ButtonConstructor().getSimpleButton("|||", e -> GUI.getInstance().toggleMenuVisible());
        JButton resetButton = new ButtonConstructor().getSimpleButton("Обновить", e -> GUI.getInstance().refreshList());

        JPanel eastPanel = new JPanel();
        eastPanel.setLayout(new BorderLayout(10, 0));
        eastPanel.add(menuButton, BorderLayout.WEST);

        headerPanel.setLayout(new BorderLayout());
        headerPanel.add(eastPanel, BorderLayout.CENTER);
        headerPanel.add(resetButton, BorderLayout.EAST);
        headerPanel.setBorder(design.getBorderTopBottom());

        addPanel.setLayout(new BoxLayout(addPanel, BoxLayout.PAGE_AXIS));
        rightPostsScroll = new JScrollPane(addPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        rightPostsScroll.getVerticalScrollBar().setUnitIncrement(design.getScrollUnitIncrement());
        Border border = BorderFactory.createLineBorder(design.getBorderColor(), 0, true);
        rightPostsScroll.setBorder(border);

        mainPanel.setBorder(design.getBorderSmall());
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(rightPostsScroll);

        return mainPanel;
    }

    public void updateScrollsPos() {
        //TODO: Возможны однажды я узнаю как обойтись без этой ф-ии
        int max = rightPostsScroll.getHorizontalScrollBar().getMaximum();
        int width = GUI.getInstance().getWidth();
        int lwidth = mainPanel.getWidth();
        int scrollPos = max / 2 - (width - lwidth) / 2 - 170;
        rightPostsScroll.getHorizontalScrollBar().setValue(scrollPos);
    }

}
