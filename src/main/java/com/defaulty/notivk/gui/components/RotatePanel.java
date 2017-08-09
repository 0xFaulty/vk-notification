package com.defaulty.notivk.gui.components;

import com.defaulty.Main;
import com.defaulty.notivk.gui.service.PanelConstructor;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

public class RotatePanel extends JPanel {

    private BufferedImage bi;
    private double angle = 0;

    public RotatePanel() {
        try {
            InputStream is = Main.class.getClassLoader().getResourceAsStream("update.png");
            ImageIcon icon = PanelConstructor.makeThumbnail(new ImageIcon(ImageIO.read(is)), 150, 150);
            bi = new BufferedImage(
                    icon.getIconWidth(),
                    icon.getIconHeight(),
                    BufferedImage.TYPE_INT_ARGB);
            Graphics g = bi.createGraphics();
            icon.paintIcon(null, g, 0, 0);
            g.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Timer controllerTimer = new Timer(true);
        controllerTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                rotate();
            }
        }, 0, 10);
    }

    private void rotate() {
        double nextAngle = angle += 0.1;
        angle = nextAngle < 8 ? nextAngle : 0;
        revalidate();
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(bi.getWidth(), bi.getHeight());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.rotate((Math.PI * angle) / 4, bi.getWidth() / 2, bi.getHeight() / 2);
        g2.drawImage(bi, 0, 0, null);
    }

}
