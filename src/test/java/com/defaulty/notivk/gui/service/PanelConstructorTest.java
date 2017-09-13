package com.defaulty.notivk.gui.service;

import com.defaulty.Main;
import org.junit.Test;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.io.InputStream;

import static org.junit.Assert.*;

public class PanelConstructorTest {

    @Test
    public void getPanelTest() throws Exception {
        PanelConstructor pc;
        pc = new PanelConstructor(PanelConstructor.PanelType.Post);
        pc.getPanel();
        pc = new PanelConstructor(PanelConstructor.PanelType.Group);
        pc.getPanel();
        pc = new PanelConstructor(PanelConstructor.PanelType.GroupPreview);
        pc.getPanel();
        pc = new PanelConstructor(PanelConstructor.PanelType.Info);
        pc.getPanel();
        pc = new PanelConstructor(PanelConstructor.PanelType.Popup);
        pc.getPanel();
    }

    @Test
    public void makeThumbnailTest() throws Exception {
        InputStream is = Main.class.getClassLoader().getResourceAsStream("update.png");
        ImageIconWrapper iiw = new ImageIconWrapper(new ImageIcon(ImageIO.read(is)));
        iiw.setSize(150, 150);
        iiw.setSize(11, 11);
    }

    @Test(expected = IllegalArgumentException.class)
    public void makeThumbnailZeroOrLessTest() throws Exception {
        InputStream is = Main.class.getClassLoader().getResourceAsStream("update.png");
        ImageIconWrapper iiw = new ImageIconWrapper(new ImageIcon(ImageIO.read(is)));
        iiw.setSize(0, 0);
        iiw.setSize(0, -1);
        iiw.setSize(-1, 0);
        iiw.setSize(-1, -1);
    }

    @Test
    public void sizeTest() throws Exception {
        PanelConstructor pc = new PanelConstructor(PanelConstructor.PanelType.Post);
        pc.setSize(0);
        pc.setSize(-1);
        pc.setSize(1);
    }
}