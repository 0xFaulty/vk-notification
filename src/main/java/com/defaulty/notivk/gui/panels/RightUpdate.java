package com.defaulty.notivk.gui.panels;

import com.defaulty.Main;
import com.defaulty.notivk.gui.service.PanelConstructor;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.InputStream;

public class RightUpdate extends Panel {

    public JPanel getPanel() {
        mainPanel.setLayout(new BorderLayout());
        JLabel jLabel = new JLabel();
        try {
            InputStream is = Main.class.getClassLoader().getResourceAsStream("update.png");
            jLabel.setIcon(PanelConstructor.makeThumbnail(new ImageIcon(ImageIO.read(is)), 100, 100));
        } catch (Exception e) {
            e.printStackTrace();
        }
        jLabel.setHorizontalAlignment(JLabel.CENTER);
        jLabel.setVerticalAlignment(JLabel.CENTER);
        mainPanel.add(jLabel, BorderLayout.CENTER);

        return mainPanel;
    }

}
