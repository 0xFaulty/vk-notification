package com.defaulty.notivk.gui.components;

import java.awt.*;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.swing.*;

import javafx.beans.value.*;

/**
 * The class {@code Browser} представляет собой простое окно браузера с
 * возможностью назначения listener(а) на событие смены локации.
 */
public class Browser {

    private JFrame mainFrame = null;
    private WebEngine webEngine = null;

    private String currentLocation = "https://www.google.ru/";
    private String startUrl = null;
    private boolean visible = true;
    private int viewWidth = 700;
    private int viewHeight = 600;

    private Runnable runnableListener;

    private void initAndShowGUI() {
        mainFrame = new JFrame("FX");
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.getContentPane().setLayout(new BorderLayout());
        final JFXPanel fxPanel = new JFXPanel();
        JButton jButton = new JButton();
        jButton.setText("Reload Page");
        jButton.addActionListener(e -> reloadPage());
        mainFrame.setLayout(new BorderLayout());
        mainFrame.add(jButton, BorderLayout.NORTH);
        mainFrame.add(fxPanel);
        mainFrame.getContentPane().setPreferredSize(new Dimension(viewWidth - 10, viewHeight - 10));
        mainFrame.pack();
        mainFrame.setResizable(false);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(visible);
        Platform.runLater(() -> initFX(fxPanel));
    }

    private void initFX(final JFXPanel fxPanel) {
        Group group = new Group();
        Scene scene = new Scene(group);
        fxPanel.setScene(scene);
        WebView webView = new WebView();
        group.getChildren().add(webView);
        webView.setPrefSize(viewWidth, viewHeight);
        webEngine = webView.getEngine();
        webEngine.load(startUrl);
        ChangeListener<String> listener = (ov, oldLoc, loc) -> executeListener();
        webEngine.locationProperty().addListener(listener);
        webEngine.locationProperty().addListener(e -> setLocation(webEngine.getLocation()));
    }

    private void executeListener() {
        runnableListener.run();
    }

    public void setListenerClass(Runnable listener) {
        runnableListener = listener;
    }

    public void hideBrowser() {
        if (mainFrame != null) {
            mainFrame.setVisible(false);
            mainFrame.setLocationRelativeTo(null);
            java.net.CookieHandler.setDefault(new java.net.CookieManager());
        } else
            visible = false;
    }

    public void loadUrl(String url) {
        if (mainFrame == null) {
            startUrl = url;
            SwingUtilities.invokeLater(this::initAndShowGUI);
        } else {
            Platform.runLater(() -> {
                webEngine.load(url);
                mainFrame.setVisible(true);
            });
        }
    }

    public String getLocation() {
        return currentLocation;
    }

    private void setLocation(String location) {
        this.currentLocation = location;
    }

    private void reloadPage() {
        Platform.runLater(() -> webEngine.reload());
    }

    public Runnable getRunnableListener() {
        return runnableListener;
    }
}


