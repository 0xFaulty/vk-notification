package com.defaulty.notivk.gui.service;

import com.defaulty.notivk.backend.SettingsWrapper;
import com.defaulty.notivk.gui.GUI;
import com.defaulty.notivk.gui.components.CustomPanel;
import com.defaulty.notivk.gui.components.GroupSettings;
import com.vk.api.sdk.objects.docs.Doc;
import com.vk.api.sdk.objects.groups.GroupFull;
import com.vk.api.sdk.objects.photos.Photo;
import com.vk.api.sdk.objects.photos.PhotoSizes;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import com.vk.api.sdk.objects.video.Video;
import com.vk.api.sdk.objects.wall.*;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class PanelConstructor implements Comparable<PanelConstructor> {

    private static SettingsWrapper settings = SettingsWrapper.getInstance();
    private static Design design = Design.getInstance();

    private int postPhotoHeight = 250;
    private int postPhotoWidth = 250;
    private int groupPhotoHeight = 50;
    private int groupPhotoWidth = 50;

    private boolean addToContentPanel = false;
    private int timeIdentifier = 0;
    private int idIdentifier = 0;
    private String groupId;
    private String groupNameId;

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
    private JLabel postText;

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

    public JPanel getPanel(int width, int height) {
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

    public void setSize(int width, int height) {
        width = width - 10;
        if (postText.getSize().getHeight() > 0 && postText.getSize().getWidth() > 0)
            postText.setPreferredSize(postText.getSize());
        if (photoPanel.getSize().getHeight() > 0 && photoPanel.getSize().getWidth() > 0)
            photoPanel.setPreferredSize(photoPanel.getSize());
        mainPanel.setMaximumSize(new Dimension(width, Integer.MAX_VALUE));
        mainPanel.setSize(new Dimension(width, height));
    }

    private void launchBrowser() {
        Desktop desktop;
        if (Desktop.isDesktopSupported()) {
            desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                // launch browser
                URI uri;
                try {
                    uri = new URI("https://vk.com/wall-" + groupId + "_" + idIdentifier);

                    desktop.browse(uri);
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
    public int compareTo(@NotNull PanelConstructor o) {
        return o.timeIdentifier - this.timeIdentifier;
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

    public void updatePanel(WallpostFull postFull, GroupFull groupFull) { //Posts

        if (groupFull.getPhoto200() != null) {
            try {
                groupImage.setIcon(makeThumbnail(new ImageIcon(new URL(groupFull.getPhoto200())), groupPhotoWidth, groupPhotoHeight));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        groupName.setText(groupFull.getName());
        groupLabel.setText(convertTime(postFull.getDate()));
        groupId = groupFull.getId();
        groupNameId = groupFull.getScreenName();

        idIdentifier = postFull.getId();
        timeIdentifier = postFull.getDate();

        Dimension size = new Dimension(postPhotoWidth, postPhotoHeight);

        if (postFull.getCopyHistory() != null && postFull.getCopyHistory().size() > 0)
            parseWallPost(postFull.getCopyHistory().get(0), postFull.getText(), postText, postImage, size);
        else {
            if (postFull.getAttachments() != null)
                attachmentsParse(postFull.getAttachments().get(0), postFull.getText(), postText, postImage, size);
            else
                postText.setText(formatText(postFull.getText()));

        }
        mainPanel.updateUI();
    }

    private void parseWallPost(Wallpost post, String text, JLabel outText, JLabel outImage, Dimension size) {
        List<WallpostAttachment> wpaList = post.getAttachments();
        if (wpaList != null && wpaList.size() > 0) {
            attachmentsParse(wpaList.get(0), text, outText, outImage, size);
        }
    }

    private void attachmentsParse(WallpostAttachment wpa, String text, JLabel outText, JLabel outImage, Dimension size) {

        String maxPhoto = null;
        String curText = "";

        if (text != null) text = text + "\n";

        if (wpa.getType() == WallpostAttachmentType.VIDEO) {
            maxPhoto = getMaxPreview(wpa.getVideo());
            curText = text + wpa.getVideo().getTitle();
        }
        if (wpa.getType() == WallpostAttachmentType.PHOTO) {
            maxPhoto = getMaxPreview(wpa.getPhoto());
            curText = text + wpa.getPhoto().getText();
        }
        if (wpa.getType() == WallpostAttachmentType.DOC) {
            maxPhoto = getMaxPreview(wpa.getDoc());
            curText = text + wpa.getDoc().getTitle();
        }
        if (wpa.getType() == WallpostAttachmentType.NOTE) {
            curText = text;
        }
        if (wpa.getType() == WallpostAttachmentType.LINK) {
            maxPhoto = getMaxPreview(wpa.getLink().getPhoto());
            curText = text;
        }
        if (wpa.getType() == WallpostAttachmentType.AUDIO) {
            curText = text + wpa.getAudio().getArtist() + " - " + wpa.getAudio().getTitle();
        }
        if (wpa.getType() == WallpostAttachmentType.GRAFFITI) {
            maxPhoto = getMaxPreview(wpa.getGraffiti());
            curText = text;
        }
        if (wpa.getType() == WallpostAttachmentType.POLL) {
            curText = wpa.getPoll().getQuestion();
        }

        if (maxPhoto != null) {
            try {
                outImage.setIcon(makeThumbnail(new ImageIcon(new URL(maxPhoto)), size.width, size.height));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        outText.setText(formatText(curText));

    }

    public void updatePanel(GroupFull groupFull) { //Groups

        if (groupFull.getPhoto200() != null) {
            try {
                groupImage.setIcon(makeThumbnail(new ImageIcon(new URL(groupFull.getPhoto200())), GUI.getInstance().getWidth(), groupPhotoHeight));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        groupName.setText(groupFull.getName());
        groupLabel.setText(groupFull.getMembersCount() + " участников");
        mainPanel.updateUI();

        groupId = groupFull.getId();
        groupNameId = groupFull.getScreenName();
    }

    public void updatePanel(GroupFull groupFull, WallpostFull postFull) { //Popup

        Dimension size = new Dimension(GUI.getInstance().getWidth(), groupPhotoHeight);

        if (postFull.getCopyHistory() != null && postFull.getCopyHistory().size() > 0)
            parseWallPost(postFull.getCopyHistory().get(0), postFull.getText(), postText, groupImage, size);
        else {
            if (postFull.getAttachments() != null)
                attachmentsParse(postFull.getAttachments().get(0), postFull.getText(), postText, groupImage, size);
            else {
                postText.setText(formatText(postFull.getText()));
            }
        }

        if (groupImage.getIcon() == null) {
            if (groupFull.getPhoto200() != null) {
                try {
                    groupImage.setIcon(makeThumbnail(new ImageIcon(new URL(groupFull.getPhoto200())), size.width, size.height));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }

        groupName.setText(groupFull.getName());
        groupLabel.setText(convertTime(postFull.getDate()));
        mainPanel.updateUI();

        idIdentifier = postFull.getId();
        groupId = groupFull.getId();
        groupNameId = groupFull.getScreenName();
    }

    public void updatePanel(UserXtrCounters userXtrCounters) { //Profile
        if (userXtrCounters != null) {
            if (userXtrCounters.getPhotoMaxOrig() != null) {
                try {
                    groupImage.setIcon(makeThumbnail(new ImageIcon(new URL(userXtrCounters.getPhotoMaxOrig())), groupPhotoWidth, groupPhotoHeight));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
            String userName = userXtrCounters.getFirstName() + " " + userXtrCounters.getLastName();
            groupName.setText(userName);
            settings.setUserName(userName);
            groupLabel.setText(userXtrCounters.getId() + "");
            mainPanel.updateUI();
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
        textPanel = new CustomPanel();
        photoPanel = new CustomPanel();
        postImage = new JLabel();
        postImage.setBorder(design.getBorderLarge());
        postText = new JLabel();
        postText.setFont(design.getFirstFont());
        postText.setForeground(design.getFirstForeColor());
        postText.setBorder(design.getBorderLarge());

        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.add(postText, BorderLayout.CENTER);

        photoPanel.setLayout(new BoxLayout(photoPanel, BoxLayout.Y_AXIS));
        photoPanel.add(postImage);

        mainPanel.add(textPanel, BorderLayout.CENTER);
        mainPanel.add(photoPanel, BorderLayout.SOUTH);

        mainPanel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) launchBrowser();
            }
        });
    }

    private void initGroupTemplate() {
        groupLabel.setFont(design.getSecondBoldFont());
        groupLabel.setForeground(design.getThirdForeColor());
        if (panelType != PanelType.GroupPreview)
            mainPanel.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        GroupSettings groupSettings = new GroupSettings(groupNameId);
                        groupSettings.pack();
                        groupSettings.setLocationRelativeTo(null); //Центр экрана
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
        postText = new JLabel();
        postText.setFont(design.getFirstFont());
        postText.setForeground(design.getFirstForeColor());
        postText.setBorder(design.getBorderLarge());

        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.add(postText, BorderLayout.CENTER);

        mainPanel.add(textPanel);
        mainPanel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) launchBrowser();

            }
        });
    }

    private String getMaxPreview(Photo photo) {

        String str = null;
        if (photo.getPhoto75() != null) str = photo.getPhoto75();
        if (photo.getPhoto130() != null) str = photo.getPhoto130();
        if (photo.getPhoto604() != null) str = photo.getPhoto604();
        if (photo.getPhoto807() != null) str = photo.getPhoto807();
        if (photo.getPhoto1280() != null) str = photo.getPhoto1280();
        if (photo.getPhoto2560() != null) str = photo.getPhoto2560();

        return str;
    }

    private String getMaxPreview(Video video) {

        String str = null;
        if (video.getPhoto130() != null) str = video.getPhoto130();
        if (video.getPhoto320() != null) str = video.getPhoto320();
        if (video.getPhoto800() != null) str = video.getPhoto800();

        return str;
    }

    private String getMaxPreview(Graffiti graffiti) {

        String str = null;
        if (graffiti.getPhoto200() != null) str = graffiti.getPhoto200();
        if (graffiti.getPhoto586() != null) str = graffiti.getPhoto586();

        return str;
    }

    private String getMaxPreview(Doc doc) {

        String str = null;

        if (doc.getPreview().getPhoto().getSizes() != null) {
            List<PhotoSizes> sizes = doc.getPreview().getPhoto().getSizes();
            for (PhotoSizes size : sizes) {
                str = size.getSrc();
            }
        }

        return str;
    }

    public static ImageIcon makeThumbnail(ImageIcon fullImage, int width, int height) {

        int x1 = fullImage.getIconWidth(), y1 = fullImage.getIconHeight();
        int x2, y2;

        double a = (double) x1 / (double) y1;
        double b = (double) width / (double) height;
        if (a > b) {
            x2 = width;
            y2 = (int) ((double) width / a);
        } else {
            x2 = (int) (height * a);
            y2 = height;
        }

        ImageIcon iconThumb;
        iconThumb = new ImageIcon(fullImage.getImage().getScaledInstance(x2, y2, Image.SCALE_DEFAULT));
        return iconThumb;
    }

    private String convertTime(long unixSeconds) {
        Date date = new Date(unixSeconds * 1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss z"); // the format of your date
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+3")); // give a timezone reference for formating
        return sdf.format(date);
    }

    private String formatText(String str) {
        String resStr;
        /*
        int splitSize = 60;
        int endPos;
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < str.length() - 1; i = i + splitSize) {
            if (str.length() > i + splitSize) endPos = i + splitSize; else endPos = str.length();
            sb.append(str.substring(i, endPos)).append("\n");
        }
        resStr = sb.toString();
        */
        resStr = str.replace("\n", "<br>");
        resStr = "<html>" + resStr + "</html>";
        return resStr;
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
