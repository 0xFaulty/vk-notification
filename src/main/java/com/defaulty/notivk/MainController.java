package com.defaulty.notivk;

import com.defaulty.notivk.backend.SettingsWrapper;
import com.defaulty.notivk.backend.VKSDK;
import com.defaulty.notivk.gui.GUI;
import com.vk.api.sdk.objects.groups.GroupFull;
import com.vk.api.sdk.objects.wall.responses.GetResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class MainController extends TimerTask {

    private GUI gui;
    private static SettingsWrapper settings;
    private static VKSDK vksdk;

    public MainController() {

        settings = SettingsWrapper.getInstance();
        settings.loadFileSettings(); //Чтение до вызова конструктора gui
        vksdk = VKSDK.getInstance();
        gui = GUI.getInstance();
        gui.setExecuteRun(this);
        gui.runGUI(800, 800);
        if (!settings.isUserDataSet()) { //Если данных для входа в настройках нет
            gui.openBrowser(); //Получить их через браузер
        } else {
            gui.setUserPanelInfo();
            if (settings.getGroupDataList().size() > 0)
                gui.guiAddGroup(settings.getGroupsIdsList());
        }
    }

    public synchronized void run() {
        if (settings.isUserDataSet() && settings.getGroupDataList().size() > 0) {
            List<String> groupsIds = settings.getGroupsIdsList();
            ArrayList<GetResponse> getResponseArray = new ArrayList<>();
            boolean flag = false;
            for (String group : groupsIds) {
                GetResponse getResponse = vksdk.getWall(group, settings.getUserData(), 10, 0);
                if (getResponse == null) {
                    flag = true;
                    break;
                } else
                    getResponseArray.add(getResponse);
            }
            if (!flag) {
                List<GroupFull> groupFullList = vksdk.getGroupData(settings.getGroupsIdsList(), settings.getUserData());
                if (groupFullList != null) {
                    if (getResponseArray.size() == groupFullList.size() && getResponseArray.size() > 0)
                        gui.addPostFromResponse(getResponseArray, groupFullList);
                }
            }
        }
    }

}
