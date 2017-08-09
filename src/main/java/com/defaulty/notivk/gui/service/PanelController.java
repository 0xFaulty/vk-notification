package com.defaulty.notivk.gui.service;

import com.defaulty.notivk.backend.SettingsWrapper;
import com.defaulty.notivk.gui.GUI;
import com.defaulty.notivk.gui.components.PopupPanel;
import com.vk.api.sdk.objects.groups.GroupFull;
import com.vk.api.sdk.objects.wall.WallpostFull;
import com.vk.api.sdk.objects.wall.responses.GetResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class PanelController {

    private ArrayList<PanelConstructor> panelConstructors = new ArrayList<>();
    private static SettingsWrapper settings = SettingsWrapper.getInstance();
    private boolean firstStart = true;

    public ArrayList<PanelConstructor> getPanelConstructors() {
        return panelConstructors;
    }

    //Добавить новые и отсортировать
    public synchronized void addPosts(ArrayList<GetResponse> getResponseArray, List<GroupFull> groupFull) {
        System.out.println("PanelController: addPosts START:" + getTime());
        boolean sortFlag = false;
        for (int i = 0; i < getResponseArray.size(); i++) {
            GetResponse aGetResponseArray = getResponseArray.get(i);
            if (aGetResponseArray != null) {
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
            } else {
                String groupId = groupFull.get(i).getId();
                String realName = groupFull.get(i).getName();
                GUI.getInstance().showMessageBox("Группа '" + realName + "' имеет закрытую или пустую стену. " +
                        "Во избежания увеличения времени ожидания загрузки рекомендуется удалить группу!");
                GUI.getInstance().deleteGroup(groupId, realName);
            }
            GUI.getInstance().updateLoadingLabel(i + 1, getResponseArray.size());
        }
        if (sortFlag) Collections.sort(panelConstructors);
        firstStart = false;
        System.out.println("PanelController: addPosts STOP:" + getTime());
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

    private String getTime() {
        return new SimpleDateFormat("HH:mm:ss.SSS").format(Calendar.getInstance().getTime());
    }


}
