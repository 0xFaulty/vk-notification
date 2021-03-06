package com.defaulty.notivk.gui.components;

import com.defaulty.notivk.gui.GUI;
import com.defaulty.notivk.gui.service.ButtonConstructor;
import com.defaulty.notivk.gui.service.Design;
import com.defaulty.notivk.gui.service.PanelConstructor;
import com.vk.api.sdk.objects.groups.GroupFull;
import com.vk.api.sdk.objects.wall.WallpostFull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The class {@code GroupSettings} представляет собой всплывающее окно уведомления.
 */
public class PopupPanel {

    private static Design design = Design.getInstance();

    private final JFrame frame = new JFrame();
    private Timer opacityTimer;
    private float opacity = 1;

    public PopupPanel(GroupFull groupFull, WallpostFull postFull) {

        int formWidth = 400;
        int formHeight = 150;

        PanelConstructor panelConstructor = new PanelConstructor(PanelConstructor.PanelType.Popup);
        panelConstructor.updatePanel(groupFull, postFull);
        panelConstructor.setPopupMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                GUI.getInstance().setExtendedState(JFrame.NORMAL);
                startDispose();
            }
        });
        JPanel newPanel = panelConstructor.getPanel(design.getButtonEntered());
        newPanel.setPreferredSize(new Dimension(formWidth - 10, formHeight));

        JScrollPane centerPanel = new JScrollPane(
                newPanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        centerPanel.getVerticalScrollBar().setUnitIncrement(design.getScrollUnitIncrement());

        JButton closeButton = new ButtonConstructor().getSimpleButton("Закрыть", e -> frame.dispose());
        JPanel wrapperPanel = new JPanel();
        wrapperPanel.setLayout(new BorderLayout());
        wrapperPanel.add(closeButton, BorderLayout.WEST);

        JPanel wrapperMainPanel = new JPanel();
        wrapperMainPanel.setLayout(new BorderLayout());
        wrapperMainPanel.add(closeButton, BorderLayout.NORTH);
        wrapperMainPanel.add(centerPanel);

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            public void windowDeactivated(WindowEvent e) {
                frame.setState(JFrame.NORMAL);
            }
        });
        Dimension sSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(sSize.width - formWidth, sSize.height - formHeight - 50);
        frame.setPreferredSize(new Dimension(formWidth, formHeight));
        frame.setAlwaysOnTop(true);
        frame.setUndecorated(true);
        frame.setTitle(groupFull.getName());
        frame.add(wrapperMainPanel);
        frame.pack();
        frame.setVisible(true);

        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.schedule(this::startDispose, 7, TimeUnit.SECONDS);
    }

    private void startDispose() {
        opacityTimer = new Timer(true);
        opacityTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                processDispose();
            }
        }, 0, 100);
    }

    private void processDispose() {
        opacity -= 0.1;
        if (opacity < 0) {
            opacityTimer.cancel();
            frame.dispose();
        } else
            frame.setOpacity(opacity);


    }

}
