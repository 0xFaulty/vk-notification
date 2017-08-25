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

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The class {@code NotifyController} отвечает за загрузку и инициализации основных
 * компонентов приложения после чего исполняет роль основного таймера
 * для добавления запроса получения данных.
 */
public class NotifyController extends TimerTask {

    private static GUI gui = GUI.getInstance();
    private static SettingsWrapper settings = SettingsWrapper.getInstance();
    private static Pool pool = PoolImpl.getInstance();

    public NotifyController() {
        gui.setExecuteRun(this);
        SwingUtilities.invokeLater(() -> gui.runGUI(810, 600));

        if (!settings.isUserDataSet()) {
            gui.openBrowser();
        } else {
            gui.getUserPanelInfo();
            if (!settings.getGroupDataList().isEmpty()) gui.refreshGroupsPanel();
        }

        Timer controllerTimer = new Timer(true);
        controllerTimer.scheduleAtFixedRate(this, 0, getPreferredTimerValue());
    }

    public void run() {
        List<String> groupsIds = getGroupListFromSettings();
        if (groupsIds != null) {
            MultiRequest multiRequest = new MultiRequest(this::processResponseList, "Main update");
            for (String group : groupsIds)
                multiRequest.addRequest(new WallRequest(Integer.parseInt(group) * -1,
                        settings.getUserData(), 10, 0, null));
            multiRequest.addRequest(new GroupRequest(settings.getGroupIdList(), settings.getUserData(), null));
            pool.addRequest(multiRequest);
        }
    }

    private synchronized void processResponseList(List<Request> requestList) {
        if (requestList != null && !requestList.isEmpty()) {
            ArrayList<GetResponse> getResponseArray = new ArrayList<>();
            List<GroupFull> groupFullList = new ArrayList<>();
            for (Request r : requestList) {
                if (r.getRequestType() == RequestType.WALL) {
                    WallRequest wallRequest = (WallRequest) r;
                    getResponseArray.add(wallRequest.getResponse());
                } else if (r.getRequestType() == RequestType.GROUP) {
                    GroupRequest groupRequest = (GroupRequest) r;
                    groupFullList = groupRequest.getResponseList();
                }
            }
            if (!getResponseArray.isEmpty() && !groupFullList.isEmpty())
                gui.addPostFromResponse(getResponseArray, groupFullList);
        }
    }

    private List<String> getGroupListFromSettings() {
        if (settings.isUserDataSet() && !settings.getGroupDataList().isEmpty())
            return settings.getGroupIdList();
        else
            return null;
    }

    private int getPreferredTimerValue() {
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
        System.out.println("Group count = " + groupCount + ", Selected update time = " + preferredTime);
        return preferredTime * 1000;
    }

}
