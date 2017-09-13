package com.defaulty.notivk.gui.service;

import com.defaulty.notivk.backend.SettingsWrapper;
import com.defaulty.notivk.gui.GUI;
import com.defaulty.notivk.gui.components.CustomPanel;
import com.defaulty.notivk.gui.components.GroupSettings;
import com.vk.api.sdk.objects.groups.GroupFull;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import com.vk.api.sdk.objects.wall.WallpostFull;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * The class {@code PanelConstructor} создаёт структуру панели
 * переданного типа после чего методом updatePanel обрабатывается и заполняется
 * контент панели.
 */
public class PanelConstructor implements Comparable<PanelConstructor> {

    private static SettingsWrapper settings = SettingsWrapper.getInstance();
    private static Design design = Design.getInstance();

    private final static int groupPhotoHeight = 50;
    private final static int groupPhotoWidth = 50;
    private final static int postPhotoWidth = 350;
    private final static int postPhotoHeight = 350;

    private boolean addToContentPanel = false;
    private int timeIdentifier = 0;
    private int idIdentifier = 0;
    private String groupId;

    private CustomPanel mainPanel;
    private CustomPanel headerPanel;
    private CustomPanel logoPanel;
    private CustomPanel infoPanel;
    private CustomPanel textPanel;
    private CustomPanel photoPanel;

    private JLabel groupImage;
    private JLabel postImage;
    private JLabel groupName;
    private JLabel groupLabel;
    private JTextArea postText;

    private PanelType panelType;

    public enum PanelType {
        Post, Group, Info, Popup, GroupPreview
    }

    public boolean isAddToContentPanel() {
        return addToContentPanel;
    }

    public void setAddToContentPanel() {
        this.addToContentPanel = true;
    }

    public JPanel getPanel() {
        CustomPanel mainPanelWrapper = new CustomPanel();
        mainPanelWrapper.setLayout(new BoxLayout(mainPanelWrapper, BoxLayout.Y_AXIS));
        mainPanelWrapper.setBubbleColor(design.getBackgroundColor());
        mainPanelWrapper.add(mainPanel);
        mainPanelWrapper.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        return mainPanelWrapper;
    }

    public JPanel getPanel(Color color) {
        mainPanel.setBackground(color);
        return mainPanel;
    }

    public void setSize(int width) {
        if (width >= 0) {
            width = width - 50;
            postText.setMaximumSize(new Dimension(width, Integer.MAX_VALUE));
            postText.revalidate();
            postText.repaint();
        }
    }

    private void launchBrowser() {
        Desktop desktop;
        if (Desktop.isDesktopSupported()) {
            desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                try {
                    desktop.browse(new URI("https://vk.com/wall-" + groupId + "_" + idIdentifier));
                } catch (IOException | URISyntaxException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
    }

    public boolean compare(int date, int id) {
        return this.timeIdentifier == date && this.idIdentifier == id;
    }

    @Override
    public int compareTo(PanelConstructor o) {
        return this.timeIdentifier - o.timeIdentifier;
    }

    public PanelConstructor(PanelType type) {
        initMainPanel();
        switch (type) {
            case Post:
                initPostTemplate();
                break;
            case Group:
                initGroupTemplate();
                break;
            case Info:
                initInfoTemplate();
                break;
            case Popup:
                initPopupTemplate();
                break;
        }
        this.panelType = type;
    }

    public void updatePanel(WallpostFull postFull, GroupFull groupFull) { //POST
        if (groupFull.getPhoto200() != null)
            setImageByURL(groupImage, groupFull.getPhoto200(), groupPhotoWidth, groupPhotoHeight);

        groupName.setText(groupFull.getName());
        groupLabel.setText(convertTime(postFull.getDate()));
        groupId = groupFull.getId();

        idIdentifier = postFull.getId();
        timeIdentifier = postFull.getDate();

        PostFullParser parser = new PostFullParser(postFull);
        postText.setText(parser.getText());
        String photoLink = parser.getPhotoLink();
        if (photoLink != null)
            setImageByURL(postImage, photoLink, postPhotoWidth, postPhotoHeight);
    }

    public void updatePanel(GroupFull groupFull) { //PANEL
        if (groupFull.getPhoto200() != null)
            setImageByURL(groupImage, groupFull.getPhoto200(), GUI.getInstance().getWidth(), groupPhotoHeight);
        groupName.setText(groupFull.getName());
        groupLabel.setText(groupFull.getMembersCount() + " участников");
        groupId = groupFull.getId();
    }

    public void updatePanel(GroupFull groupFull, WallpostFull postFull) { //POPUP
        Dimension size = new Dimension(GUI.getInstance().getWidth(), groupPhotoHeight);

        PostFullParser parser = new PostFullParser(postFull);
        postText.setText(parser.getText());
        String photoLink = parser.getPhotoLink();
        if (photoLink != null)
            setImageByURL(groupImage, groupFull.getPhoto200(), size.width, size.height);
        else if (groupFull.getPhoto200() != null)
            setImageByURL(groupImage, groupFull.getPhoto200(), size.width, size.height);

        groupName.setText(groupFull.getName());
        groupLabel.setText(convertTime(postFull.getDate()));
        idIdentifier = postFull.getId();
        groupId = groupFull.getId();
    }

    public void updatePanel(UserXtrCounters userXtrCounters) { //PROFILE
        if (userXtrCounters != null) {
            if (userXtrCounters.getPhotoMaxOrig() != null)
                setImageByURL(groupImage, userXtrCounters.getPhotoMaxOrig(), groupPhotoWidth, groupPhotoHeight);
            String userName = userXtrCounters.getFirstName() + " " + userXtrCounters.getLastName();
            groupName.setText(userName);
            settings.setUserName(userName);
            groupLabel.setText(userXtrCounters.getId() + "");
        }
    }

    private void setImageByURL(JLabel label, String url, int width, int height) {
        try {
            ImageIconWrapper iiw = new ImageIconWrapper(new URL(url));
            iiw.setSize(width, height);
            label.setIcon(iiw);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void initMainPanel() {
        mainPanel = new CustomPanel();
        headerPanel = new CustomPanel();
        logoPanel = new CustomPanel();
        infoPanel = new CustomPanel();

        groupImage = new JLabel();
        groupName = new JLabel();
        groupLabel = new JLabel();

        logoPanel.setLayout(new BorderLayout());
        logoPanel.add(groupImage, BorderLayout.WEST);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(design.getBorderLarge());
        infoPanel.add(groupName);
        infoPanel.add(groupLabel);

        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(design.getBorderLarge());
        headerPanel.add(logoPanel, BorderLayout.WEST);
        headerPanel.add(infoPanel);

        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        Border border = BorderFactory.createLineBorder(design.getBorderColor(), 2, true);
        mainPanel.setBorder(border);
    }

    private void initPostTemplate() {
        groupLabel.setFont(design.getSecondBoldFont());
        groupLabel.setForeground(design.getThirdForeColor());

        postImage = new JLabel();
        postImage.setBorder(design.getBorderLarge());

        postText = new JTextArea();
        postText.setWrapStyleWord(true);
        postText.setLineWrap(true);
        postText.setOpaque(false);
        postText.setEditable(false);
        postText.setFont(design.getFirstFont());
        postText.setForeground(design.getFirstForeColor());
        postText.setBorder(design.getBorderLarge());

        textPanel = new CustomPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.add(postText);

        JLabel openLink = new JLabel("<html><u>Открыть в браузере</u></html>", JLabel.RIGHT);
        openLink.addMouseListener(new LinkMouseListener(this::launchBrowser));
        openLink.setFont(design.getFirstBoldFont());
        openLink.setBorder(design.getBorderLarge());
        JPanel linkPanel = new CustomPanel();
        linkPanel.setLayout(new BorderLayout());
        linkPanel.add(openLink, BorderLayout.EAST);

        photoPanel = new CustomPanel();
        photoPanel.setLayout(new BoxLayout(photoPanel, BoxLayout.Y_AXIS));
        photoPanel.add(postImage);
        photoPanel.add(linkPanel);

        mainPanel.add(textPanel, BorderLayout.CENTER);
        mainPanel.add(photoPanel, BorderLayout.SOUTH);
    }

    private void initGroupTemplate() {
        groupLabel.setFont(design.getSecondBoldFont());
        groupLabel.setForeground(design.getThirdForeColor());
        if (panelType != PanelType.GroupPreview)
            mainPanel.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        GroupSettings groupSettings = new GroupSettings(groupId, groupName.getText());
                        groupSettings.pack();
                        groupSettings.setLocationRelativeTo(null);
                        groupSettings.setVisible(true);
                    }
                }
            });
    }

    private void initInfoTemplate() {
        groupLabel.setFont(design.getSecondBoldFont());
        groupLabel.setForeground(design.getThirdForeColor());
        groupName.setFont(design.getSecondBoldFont());
        groupName.setForeground(design.getSecondForeColor());
        mainPanel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) GUI.getInstance().profileClick();
            }
        });
    }

    private void initPopupTemplate() {
        groupLabel.setFont(design.getSecondBoldFont());
        groupLabel.setForeground(design.getThirdForeColor());

        textPanel = new CustomPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

        postText = new JTextArea();
        postText.setWrapStyleWord(true);
        postText.setLineWrap(true);
        postText.setOpaque(false);
        postText.setEditable(false);
        postText.setFont(design.getFirstFont());
        postText.setForeground(design.getFirstForeColor());
        postText.setBorder(design.getBorderLarge());

        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.add(postText);

        mainPanel.add(textPanel);
    }

    public void setPopupMouseListener(MouseListener popupMouseListener) {
        postText.addMouseListener(popupMouseListener);
        mainPanel.addMouseListener(popupMouseListener);
    }

    private String convertTime(long unixSeconds) {
        Date date = new Date(unixSeconds * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss z");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+3"));
        return sdf.format(date);
    }

    public void setPanelColor(Color color) {
        headerPanel.setBubbleColor(color);
        logoPanel.setBubbleColor(color);
        infoPanel.setBubbleColor(color);
        if (panelType == PanelType.Post) {
            textPanel.setBubbleColor(color);
            photoPanel.setBubbleColor(color);
        }
        mainPanel.setBubbleColor(color);
    }

}
