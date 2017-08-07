package com.defaulty.notivk;

import com.defaulty.notivk.backend.SettingsWrapper;
import com.defaulty.notivk.backend.threadpool.Pool;
import com.defaulty.notivk.backend.threadpool.PoolImpl;
import com.defaulty.notivk.backend.threadpool.requests.GroupRequest;
import com.defaulty.notivk.backend.threadpool.requests.MultiRequest;
import com.defaulty.notivk.backend.threadpool.requests.Request;
import com.defaulty.notivk.backend.threadpool.requests.WallRequest;
import com.defaulty.notivk.backend.threadpool.requests.enums.RequestType;
import com.defaulty.notivk.gui.GUI;
import com.vk.api.sdk.objects.groups.GroupFull;
import com.vk.api.sdk.objects.wall.responses.GetResponse;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainController extends TimerTask {

    private GUI gui;
    private static SettingsWrapper settings;
    private static Pool pool;

    public MainController() {

        settings = SettingsWrapper.getInstance();
        settings.loadFileSettings(); //Read before gui
        gui = GUI.getInstance();
        gui.setExecuteRun(this);
        SwingUtilities.invokeLater(() -> gui.runGUI(800, 800));

        pool = PoolImpl.getInstance(); // Run pool timer

        if (!settings.isUserDataSet()) { //If have not any userdata get if
            gui.openBrowser();
        } else {
            gui.getUserPanelInfo();
            if (settings.getGroupDataList().size() > 0)
                gui.refreshGroupsPanel();
        }

        Timer controllerTimer = new Timer(true);
        controllerTimer.scheduleAtFixedRate(this, 0, getPreferredTimerValue());
    }

    public synchronized void run() {
        List<String> groupsIds = getGroupListFromSettings();
        if (groupsIds != null) {
            MultiRequest multiRequest = new MultiRequest(this::processResponseList, "Main update");
            for (String group : groupsIds)
                multiRequest.addRequest(new WallRequest(Integer.parseInt(group) * -1,
                        settings.getUserData(), 10, 0, null));
            multiRequest.addRequest(new GroupRequest(settings.getGroupsIdsList(), settings.getUserData(), null));
            multiRequest.send(pool);
        }
    }

    private synchronized void processResponseList(List<Request> requestList) {
        ArrayList<GetResponse> getResponseArray = new ArrayList<>();
        List<GroupFull> groupFullList = new ArrayList<>();
        for (Request r : requestList) {
            if (r.getRequestType() == RequestType.Wall) {
                WallRequest wallRequest = (WallRequest) r;
                getResponseArray.add(wallRequest.getResponse());
            } else if (r.getRequestType() == RequestType.Group) {
                GroupRequest groupRequest = (GroupRequest) r;
                groupFullList = groupRequest.getResponseList();
            }
        }
        if (!getResponseArray.isEmpty() && !groupFullList.isEmpty())
            gui.addPostFromResponse(getResponseArray, groupFullList);
    }

    @Nullable
    private List<String> getGroupListFromSettings() {
        if (settings.isUserDataSet() && !settings.getGroupDataList().isEmpty())
            return settings.getGroupsIdsList();
        else
            return null;
    }

    public int getPreferredTimerValue() {
        int preferredTime = 30;
        int groupCount = 0;
        List<String> groupsIds = getGroupListFromSettings();
        if (groupsIds != null) {
            groupCount = getGroupListFromSettings().size();
            if (groupCount < 5) preferredTime = 10;
            else if (groupCount < 10) preferredTime = 15;
            else if (groupCount < 20) preferredTime = 30;
            else preferredTime = groupCount * 2;
        }
        System.out.println("Groups:" + groupCount + " chosen time:" + preferredTime);
        return preferredTime * 1000;
    }

}
