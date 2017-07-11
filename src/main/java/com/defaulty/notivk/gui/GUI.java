package com.defaulty.notivk.gui;

import com.defaulty.notivk.backend.SettingsWrapper;
import com.defaulty.notivk.backend.VKSDK;
import com.defaulty.notivk.gui.components.*;
import com.defaulty.notivk.gui.panels.*;
import com.defaulty.notivk.gui.service.Design;
import com.defaulty.notivk.gui.service.PanelConstructor;
import com.defaulty.notivk.gui.service.PanelController;
import com.vk.api.sdk.objects.groups.GroupFull;
import com.vk.api.sdk.objects.wall.responses.GetResponse;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class GUI extends JFrame {

    private static volatile GUI ourInstance = new GUI();
    private static SettingsWrapper settings = SettingsWrapper.getInstance();
    private static VKSDK vksdk = VKSDK.getInstance();
    private static Design design = Design.getInstance();

    private Runnable executeRun;
    private PanelController panelController = new PanelController();
    private JPanel mainPanelsContainer = new JPanel();

    private int lastWidth = 0;
    private int lastHeight = 0;

    private Browser browser = null;

    private LeftMenu leftMenu = new LeftMenu();
    private RightPosts rightPosts = new RightPosts();
    private RightGroups rightGroups = new RightGroups();
    private RightSettings rightSettings = new RightSettings();
    private RightUpdate rightUpdate = new RightUpdate();

    public static synchronized GUI getInstance() {
        return ourInstance;
    }

    private GUI() {
        super("NotifyVK v0.4");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void runGUI(int width, int height) {
        createGUI();
        getContentPane().add(mainPanelsContainer);
        setPreferredSize(new Dimension(width, height));
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizePanels();
                super.componentResized(e);
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                resizePanels();
                super.componentMoved(e);
            }
        });

        addWindowStateListener(e -> resizePanels());
        super.pack();
        super.setLocationRelativeTo(null);
        super.setVisible(true);
    }

    public void setExecuteRun(Runnable mainRun) {
        executeRun = mainRun;
    }

    private void createGUI() {
        mainPanelsContainer.setLayout(new BorderLayout());

        JLayeredPane rightLayer = new JLayeredPane();
        rightLayer.add(rightUpdate.getPanel(), new Integer(4));
        rightLayer.add(rightPosts.getPanel(), new Integer(3));
        rightLayer.add(rightGroups.getPanel(), new Integer(2));
        rightLayer.add(rightSettings.getPanel(), new Integer(1));

        mainPanelsContainer.add(leftMenu.getPanel(), BorderLayout.WEST);
        mainPanelsContainer.add(rightLayer);
    }

    public void setTopPanel(EnumPanels type) {
        rightPosts.setVisible(false);
        rightGroups.setVisible(false);
        rightSettings.setVisible(false);
        rightUpdate.setVisible(false);

        switch (type) {
            case Posts:
                rightPosts.setVisible(true);
                break;
            case Groups:
                rightGroups.setVisible(true);
                break;
            case Settings:
                rightSettings.setVisible(true);
                break;
            case Update:
                rightUpdate.setVisible(true);
                break;
        }
    }

    public void toggleMenuVisible() {
        leftMenu.toggleVisible();
        resizePanels();
    }

    public void addPostFromResponse(ArrayList<GetResponse> response, List<GroupFull> groupFullList) {
        panelController.addPosts(response, groupFullList);
        addPosts();
        System.out.println("GUI:addPostFromResponse");
        rightUpdate.setVisible(false);
        resizePanels();
    }

    private void addPosts() {
        ArrayList<PanelConstructor> panelConstructors = panelController.getPanelConstructors();
        for (PanelConstructor panelConstructor : panelConstructors) {
            if (!panelConstructor.isAddToContentPanel()) {
                rightPosts.add(panelConstructor.getPanel(rightPosts.getWidth(), rightPosts.getHeight()));
                panelConstructor.setAddToContentPanel();
            }
        }
    }

    public void refreshList() {
        rightUpdate.setVisible(true);
        panelController.clearArray();
        rightPosts.removeAll();
        rightPosts.updateUI();
        executeRun.run();
    }

    private void addGroupFromResponse(List<GroupFull> groupFull) {
        for (GroupFull group : groupFull) {
            addGroupFromResponse(group);
        }
    }

    private void addGroupFromResponse(GroupFull groupFull) {
        PanelConstructor newPanel = new PanelConstructor(PanelConstructor.PanelType.Group);
        newPanel.updatePanel(groupFull);
        rightGroups.add(newPanel.getPanel(rightPosts.getWidth(), rightPosts.getHeight()));
    }

    public void guiAddGroup(String groupId) {
        GroupFull groupFull = vksdk.getGroupData(groupId, settings.getUserData());
        if (groupFull != null) {
            settings.addGroupId(groupId);
            settings.save();
            addGroupFromResponse(groupFull);
            rightGroups.updateUI();
        }
    }

    public void removeGuiGroups() {
        rightGroups.removeAll();
        rightGroups.updateUI();
    }

    public void guiAddGroup(List<String> groupsIds) {
        List<GroupFull> groupFull = vksdk.getGroupData(groupsIds, settings.getUserData());
        if (groupFull != null) {
            addGroupFromResponse(groupFull);
            rightGroups.updateUI();
        }
    }

    public void setUserPanelInfo() {
        PanelConstructor panelConstructor = new PanelConstructor(PanelConstructor.PanelType.Info);

        panelConstructor.updatePanel(
                vksdk.getUserInfo(
                        settings.getUserId(),
                        settings.getUserData()
                )
        );
        panelConstructor.setPanelColor(design.getBackgroundColor());
        leftMenu.removeAll();
        leftMenu.add(panelConstructor.getPanel(rightPosts.getWidth(), rightPosts.getHeight()));
        leftMenu.updateUI();
    }

    public void resizePanels() {
        int width = getWidth();
        int height = getHeight();

        int headerPostHeight = rightPosts.getHeaderHeight();

        int rightWidth = width - 20;
        if (leftMenu.isVisible()) rightWidth -= leftMenu.getWidth();

        int rightHeight = height - headerPostHeight - 5;

        //if (lastWidth != rightWidth + width || lastHeight != rightHeight + height) {
        //System.out.println("resize");
        rightUpdate.setBounds(0, headerPostHeight, rightWidth, rightHeight - headerPostHeight);
        rightPosts.setBounds(0, 0, rightWidth, rightHeight);
        rightGroups.setBounds(0, 0, rightWidth, rightHeight);
        rightSettings.setBounds(0, 0, rightWidth, rightHeight);

        ArrayList<PanelConstructor> panelConstructors = panelController.getPanelConstructors();
        for (PanelConstructor pc : panelConstructors)
            pc.setSize(rightWidth, rightHeight);

        lastWidth = rightWidth + getWidth();
        lastHeight = rightHeight + getHeight();

        rightPosts.updateScrollsPos();
        //}
    }

    public void profileClick() {
        if (settings.isUserDataSet()) {
            int dialogResult = JOptionPane.showConfirmDialog(
                    null,
                    "Выйти из профиля '" + settings.getUserName() + "'?", "Предупреждение",
                    JOptionPane.YES_NO_OPTION);
            if (dialogResult == JOptionPane.YES_OPTION) {
                leftMenu.removeAll();
                leftMenu.updateUI();
                settings.clearUserData();
                settings.save();
                openBrowser();
            }
        }
    }

    public void openBrowser() {
        super.setEnabled(false);

        int apiClientId = 6080102;
        String apiRedirectURL = "https://oauth.vk.com/blank.html";
        String apiDisplayType = "mobile";
        String apiScopeMask = "friends,offline,groups";
        String apiVersion = "5.65";

        final String startURL = "https://oauth.vk.com/authorize?" +
                "client_id=" + apiClientId +
                "&display=" + apiDisplayType +
                "&redirect_uri=" + apiRedirectURL +
                "&scope=" + apiScopeMask +
                "&response_type=code" +
                "&v=" + apiVersion;

        if (browser == null) {
            browser = new Browser(startURL, 700, 600);
            browser.setListenerClass(() -> {
                String url = browser.getLocation();
                int codePos = url.indexOf("#code=");
                if (codePos != -1) {
                    browser.hideBrowser();
                    if (settings.setUserData(vksdk.getUserAuthData(url.substring(codePos + 6, url.length())))) {
                        settings.save();
                        setUserPanelInfo();
                        super.setEnabled(true);
                    } else
                        browser.loadUrl(startURL);
                }
            });
            browser.run();
        } else
            browser.loadUrl(startURL);
    }

}
