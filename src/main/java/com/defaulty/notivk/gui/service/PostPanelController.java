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

/**
 * The class {@code PostPanelController} управляет преобразованием данных полученных от vk api
 * в панели с контентом, отфильтровывает по тегам, сортирует по времени, выводит всплывающее
 * окно при необходимости, а также выводит предложение для удаления групп с закрытой или
 * пустой стеной.
 */
public class PostPanelController {

    private List<PanelConstructor> panelConstructors = new ArrayList<>();
    private static SettingsWrapper settings = SettingsWrapper.getInstance();
    private boolean firstStart = true;

    public List<PanelConstructor> getPanelConstructors() {
        return panelConstructors;
    }

    public synchronized void addPosts(List<GetResponse> wallResponseList, List<GroupFull> groupList) {
        System.out.println("PostPanelController: addPosts START: " + getTime());
        boolean sortFlag = false;
        for (int i = 0; i < wallResponseList.size(); i++) {
            GetResponse wallResponse = wallResponseList.get(i);
            if (wallResponse != null) {
                for (int j = 0; j < wallResponse.getItems().size(); j++) {
                    boolean flag = false;
                    WallpostFull curPost = wallResponse.getItems().get(j);
                    for (PanelConstructor panelConstructor : panelConstructors) {
                        int curData = curPost.getDate();
                        int curId = curPost.getId();
                        if (panelConstructor.compare(curData, curId)) flag = true;
                    }
                    //Проверка на теги
                    if (!flag && settings.getGroupCheckState(groupList.get(i).getScreenName())) {
                        flag = !checkGroupTags(curPost, settings.getGroupTags(groupList.get(i).getScreenName()));
                    }
                    //Добавить панель
                    if (!flag) {
                        PanelConstructor newPanel = new PanelConstructor(PanelConstructor.PanelType.Post);
                        newPanel.updatePanel(curPost, groupList.get(i));
                        panelConstructors.add(0, newPanel);
                        sortFlag = true;
                        //Вывести popup окно
                        if (GUI.getInstance().getState() == 1 && settings.getNotifyState() && !firstStart)
                            new PopupPanel(groupList.get(i), curPost);
                    }
                }
            } else {
                String groupId = groupList.get(i).getId();
                String realName = groupList.get(i).getName();
                GUI.getInstance().showMessageBox("Группа '" + realName + "' имеет закрытую или пустую стену. " +
                        "Во избежания увеличения времени ожидания загрузки рекомендуется удалить группу!");
                GUI.getInstance().deleteGroup(groupId, realName);
            }
            GUI.getInstance().updateLoadingLabel(i + 1, wallResponseList.size());
        }
        if (sortFlag) Collections.sort(panelConstructors);
        firstStart = false;
        System.out.println("PostPanelController: addPosts STOP:  " + getTime());
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
