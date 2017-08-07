package com.defaulty.notivk.gui;

import com.defaulty.notivk.backend.SettingsWrapper;
import com.defaulty.notivk.backend.threadpool.Pool;
import com.defaulty.notivk.backend.threadpool.PoolImpl;
import com.defaulty.notivk.backend.threadpool.requests.CodeRequest;
import com.defaulty.notivk.backend.threadpool.requests.GroupRequest;
import com.defaulty.notivk.backend.threadpool.requests.ProfileRequest;
import com.defaulty.notivk.backend.threadpool.requests.Request;
import com.defaulty.notivk.gui.components.Browser;
import com.defaulty.notivk.gui.panels.*;
import com.defaulty.notivk.gui.service.Design;
import com.defaulty.notivk.gui.service.PanelConstructor;
import com.defaulty.notivk.gui.service.PanelController;
import com.vk.api.sdk.objects.groups.GroupFull;
import com.vk.api.sdk.objects.wall.responses.GetResponse;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GUI extends JFrame {

    private static GUI ourInstance = new GUI();
    private static SettingsWrapper settings = SettingsWrapper.getInstance();
    private static Design design = Design.getInstance();
    private static Pool pool = PoolImpl.getInstance();

    private Runnable executeRun;
    private Browser browser = null;
    private PanelController panelController = new PanelController();

    private JPanel mainPanelsContainer = new JPanel();
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
        JLayeredPane rightLayer = new JLayeredPane();
        rightLayer.add(rightUpdate.getPanel(), new Integer(4));
        rightLayer.add(rightPosts.getPanel(), new Integer(3));
        rightLayer.add(rightGroups.getPanel(), new Integer(2));
        rightLayer.add(rightSettings.getPanel(), new Integer(1));

        rightUpdate.setVisible(!settings.getGroupDataList().isEmpty());

        mainPanelsContainer.setLayout(new BorderLayout());
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
        rightPosts.repaintComponent();
        resizePanels();
    }

    public void addPostFromResponse(ArrayList<GetResponse> response, List<GroupFull> groupFullList) {
        panelController.addPosts(response, groupFullList);
        addPosts();
        System.out.println("GUI:addPostFromResponse");
        rightUpdate.setVisible(false);
        resizePanels();
        rightPosts.repaintComponent();
    }

    private void addPosts() {
        ArrayList<PanelConstructor> panelConstructors = panelController.getPanelConstructors();
        for (PanelConstructor panelConstructor : panelConstructors) {
            if (!panelConstructor.isAddToContentPanel()) {
                rightPosts.add(panelConstructor.getPanel(), 0);
                panelConstructor.setAddToContentPanel();
            }
        }
    }

    public void refreshList() {
        if (settings.getGroupsIdsList().size() > 0) {
            rightUpdate.setVisible(true);
            panelController.clearArray();
            rightPosts.removeAll();
            rightPosts.repaintComponent();
            new Thread(executeRun).start();
        } else
            showMessageBox("Список групп пуст.");
    }

    public void showMessageBox(String string) {
        JOptionPane.showMessageDialog(null, string);
    }

    public void deleteGroup(String groupId, String realName) {
        int dialogResult;
        dialogResult = JOptionPane.showConfirmDialog(
                null,
                "Удалить группу '" + realName + "'?", "Предупреждение",
                JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            settings.removeGroupId(groupId);
            settings.save();
            rightGroups.removeAll();
            refreshGroupsPanel();
        }
    }

    public void refreshGroupsPanel() {
        pool.addRequest(new GroupRequest(settings.getGroupsIdsList(), settings.getUserData(), this::addGroupFromResponse));
    }

    private void addGroupFromResponse(List<Request> requestList) {
        GroupRequest groupRequest = (GroupRequest) requestList.get(0);
        List<GroupFull> groupFull = groupRequest.getResponseList();
        for (GroupFull group : groupFull)
            addGroupFromResponse(group);
        rightGroups.repaintComponent();
    }

    private void addGroupFromResponse(GroupFull groupFull) {
        PanelConstructor newPanel = new PanelConstructor(PanelConstructor.PanelType.Group);
        newPanel.updatePanel(groupFull);
        rightGroups.add(newPanel.getPanel());
    }

    public void guiAddNewGroupToPanel(String name) {
        pool.addRequest(new GroupRequest(name, settings.getUserData(), this::guiAddNewGroupFromResponse));
    }

    private void guiAddNewGroupFromResponse(List<Request> requestList) {
        GroupRequest groupRequest = (GroupRequest) requestList.get(0);
        GroupFull groupFull = groupRequest.getResponse();
        settings.addGroupId(groupFull.getId());
        settings.save();
        addGroupFromResponse(groupFull);
        rightGroups.repaintComponent();
    }

    public void getUserPanelInfo() {
        pool.addRequest(new ProfileRequest(settings.getUserId(), settings.getUserData(), this::setUserPanelInfo));
    }

    private void setUserPanelInfo(List<Request> requestList) {
        ProfileRequest profileRequest = (ProfileRequest) requestList.get(0);
        PanelConstructor panelConstructor = new PanelConstructor(PanelConstructor.PanelType.Info);
        panelConstructor.updatePanel(profileRequest.getResponse());
        panelConstructor.setPanelColor(design.getBackgroundColor());
        leftMenu.removeAll();
        leftMenu.add(panelConstructor.getPanel());
        leftMenu.repaintComponent();
    }

    private void resizePanels() {
        //TODO: find way how don't use it
        resizeTask();
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.schedule(this::resizeTask, 1, TimeUnit.MILLISECONDS);
    }

    private void resizeTask() {
        int headerPostHeight = rightPosts.getHeaderHeight();
        int rightWidth = getWidth() - 20;
        int rightHeight = getHeight() - headerPostHeight - 5;

        if (leftMenu.isVisible()) rightWidth -= leftMenu.getWidth();

        rightUpdate.setBounds(0, headerPostHeight, rightWidth, rightHeight - headerPostHeight);
        rightPosts.setBounds(0, 0, rightWidth, rightHeight);
        rightGroups.setBounds(0, 0, rightWidth, rightHeight);
        rightSettings.setBounds(0, 0, rightWidth, rightHeight);

        ArrayList<PanelConstructor> panelConstructors = panelController.getPanelConstructors();
        for (PanelConstructor pc : panelConstructors)
            pc.setSize(rightWidth);

        rightPosts.updateScrollsPos();
    }

    public void profileClick() {
        if (settings.isUserDataSet()) {
            int dialogResult = JOptionPane.showConfirmDialog(
                    null,
                    "Выйти из профиля '" + settings.getUserName() + "'?", "Предупреждение",
                    JOptionPane.YES_NO_OPTION);
            if (dialogResult == JOptionPane.YES_OPTION) {
                leftMenu.removeAll();
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
                    pool.addRequest(new CodeRequest(url.substring(codePos + 6, url.length()), this::processUserData));
                }
            });
            browser.run();
        } else
            browser.loadUrl(startURL);
    }

    private void processUserData(List<Request> requestList) {
        CodeRequest codeRequest = (CodeRequest) requestList.get(0);
        if (settings.setUserData(codeRequest.getResponse())) {
            settings.save();
            getUserPanelInfo();
            super.setEnabled(true);
        } else
            openBrowser();
    }

}
