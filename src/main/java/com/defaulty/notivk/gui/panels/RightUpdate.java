package com.defaulty.notivk.gui.panels;

import com.defaulty.notivk.gui.service.PanelConstructor;

import javax.swing.*;
import java.awt.*;

public class RightUpdate {

    private JPanel mainPanel = new JPanel();

    public JPanel getPanel() {
        mainPanel.setLayout(new BorderLayout());
        JLabel jLabel = new JLabel();
        try {
            jLabel.setIcon(PanelConstructor.makeThumbnail(new ImageIcon("./res/update.png"), 100, 100));
        } catch (Exception e) {
            e.printStackTrace();
        }
        jLabel.setHorizontalAlignment(JLabel.CENTER);
        jLabel.setVerticalAlignment(JLabel.CENTER);
        mainPanel.add(jLabel, BorderLayout.CENTER);

        return mainPanel;
    }

    public void setVisible(boolean visible) {
        mainPanel.setVisible(visible);
    }

    public void setBounds(int x, int y, int width, int height) {
        mainPanel.setBounds(x, y, width, height);
    }
}
