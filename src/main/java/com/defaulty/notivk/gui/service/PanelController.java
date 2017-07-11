package com.defaulty.notivk.gui.service;

import com.defaulty.notivk.backend.SettingsWrapper;
import com.defaulty.notivk.gui.GUI;
import com.defaulty.notivk.gui.components.PopupPanel;
import com.vk.api.sdk.objects.wall.responses.GetResponse;
import com.vk.api.sdk.objects.groups.GroupFull;
import com.vk.api.sdk.objects.wall.WallpostFull;

import java.util.*;

public class PanelController {

    private ArrayList<PanelConstructor> panelConstructors = new ArrayList<>();
    private static SettingsWrapper settings = SettingsWrapper.getInstance();
    private boolean firstStart = true;

    public ArrayList<PanelConstructor> getPanelConstructors() {
        return panelConstructors;
    }

    //Добавить новые и отсортировать
    public void addPosts(ArrayList<GetResponse> getResponseArray, List<GroupFull> groupFull) {
        boolean sortFlag = false;
        for (int i = 0; i < getResponseArray.size(); i++) {
            GetResponse aGetResponseArray = getResponseArray.get(i);
            for (int j = 0; j < aGetResponseArray.getItems().size(); j++) {
                boolean flag = false;
                WallpostFull curPost = aGetResponseArray.getItems().get(j);
                for (PanelConstructor panelConstructor : panelConstructors) {
                    int curData = curPost.getDate();
                    int curId = curPost.getId();
                    if (panelConstructor.compare(curData, curId)) flag = true;
                }
                //Проверка на теги
                if (!flag && settings.getGroupCheckState(groupFull.get(i).getScreenName())) {
                    flag = !checkGroupTags(curPost, settings.getGroupTags(groupFull.get(i).getScreenName()));
                }
                //Добавить панель
                if (!flag) {
                    PanelConstructor newPanel = new PanelConstructor(PanelConstructor.PanelType.Post);
                    newPanel.updatePanel(curPost, groupFull.get(i));
                    panelConstructors.add(0, newPanel);
                    sortFlag = true;
                    //Вывести popup окно
                    if (GUI.getInstance().getState() == 1 && settings.getNotifyType() && !firstStart)
                        new PopupPanel(groupFull.get(i), curPost);
                }
            }
        }
        if (sortFlag) Collections.sort(panelConstructors);
        firstStart = false;
    }

    public void clearArray() {
        panelConstructors.clear();
    }

    private boolean checkGroupTags(WallpostFull wp, List<String> checkList) {
        boolean out = false;
        for (String tag : checkList) {
            String first = wp.getText().toUpperCase();
            String second = tag.toUpperCase();
            if (first.indexOf(second) > 0) out = true;
        }
        return out;
    }


}
