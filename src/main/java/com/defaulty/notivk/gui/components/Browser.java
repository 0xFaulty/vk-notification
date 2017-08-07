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

public class Browser {

    private JFrame mainFrame = null;
    private WebEngine webEngine = null;

    private String currentLocation = null;
    private String startUrl = null;
    private int viewWidth;
    private int viewHeight;

    private Runnable runnableListener;

    public Browser(String startUrl, int width, int height) {
        this.startUrl = startUrl;
        this.viewWidth = width;
        this.viewHeight = height;
    }

    public void run() {
        SwingUtilities.invokeLater(this::initAndShowGUI);
    }

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
        mainFrame.setVisible(true);
        // this will run initFX as JavaFX-Thread
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
        mainFrame.setVisible(false);
        mainFrame.setLocationRelativeTo(null);
        java.net.CookieHandler.setDefault(new java.net.CookieManager());
    }

    public void loadUrl(String url) {
        Platform.runLater(() -> {
            webEngine.load(url);
            mainFrame.setVisible(true);
        });
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

}


