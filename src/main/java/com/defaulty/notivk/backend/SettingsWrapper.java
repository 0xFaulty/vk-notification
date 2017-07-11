package com.defaulty.notivk.backend;

import com.defaulty.notivk.backend.xml.AppSettings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.io.File;
import java.util.Objects;
import java.util.regex.Pattern;

public class SettingsWrapper {

    private static UserData userData = new UserData();
    private List<GroupData> groupDataList = new ArrayList<>();

    private final String separator = "|";
    private final String separator2 = ",";
    private final String userId = "userId";
    private final String accessToken = "accessToken";
    private final String savedGroups = "savedGroups";
    private final String enableNotify = "enableNotify";
    private final String groupTags = "groupTags:";
    private final String tagEnabled = "tagEnabled:";

    private static SettingsWrapper ourInstance;

    public static synchronized SettingsWrapper getInstance() {
        if (ourInstance == null) ourInstance = new SettingsWrapper();
        return ourInstance;
    }

    public void loadFileSettings() {
        loadSettings();
    }

    public List<String> getGroupsIdsList() {
        List<String> list = new ArrayList<>();
        for (GroupData group : groupDataList) {
            list.add(group.getGroupNameId());
        }
        return list;
    }

    public List<GroupData> getGroupDataList() {
        return groupDataList;
    }

    public void addGroupId(String name) {
        GroupData groupData = new GroupData();
        groupData.setGroupNameId(name);
        groupDataList.add(groupData);
    }

    public void removeGroupId(String groupNameId) {
        for (GroupData group : groupDataList) {
            if (group.getGroupNameId().equals(groupNameId)) {
                groupDataList.remove(group);
                break;
            }
        }
    }

    public boolean getGroupCheckState(String groupNameId) {
        boolean out = false;
        for (GroupData group : groupDataList) {
            if (group.getGroupNameId().equals(groupNameId)) {
                out = group.isEnableTags();
                break;
            }
        }
        return out;
    }

    public List<String> getGroupTags(String groupNameId) {
        List<String> out = new ArrayList<>();
        for (GroupData group : groupDataList) {
            if (group.getGroupNameId().equals(groupNameId)) {
                out = group.getTags();
                break;
            }
        }
        return out;
    }

    public void setGroupCheckState(String groupNameId, boolean state) {
        for (GroupData group : groupDataList) {
            if (group.getGroupNameId().equals(groupNameId)) {
                group.setEnableTags(state);
                break;
            }
        }
    }

    public void setGroupTags(String groupNameId, List<String> tags) {
        for (GroupData group : groupDataList) {
            if (group.getGroupNameId().equals(groupNameId)) {
                group.setTags(tags);
                break;
            }
        }
    }

    public void setNotifyType(Boolean notifyType) {
        userData.setNotifyType(notifyType);
    }

    public void setUserName(String userName) {
        userData.setUserName(userName);
    }

    public String getUserName() {
        return userData.getUserName();
    }

    public Boolean getNotifyType() {
        return userData.getNotifyType();
    }

    public int getUserId() {
        return userData.getUserId();
    }

    public UserData getUserData() {
        return userData;
    }

    public boolean isUserDataSet() {
        return userData.isUserDataSet();
    }

    public boolean setUserData(UserData newUserData) {
        if (newUserData != null) {
            userData = newUserData;
            return true;
        }
        return false;
    }

    public void clearUserData() {
        userData = new UserData();
        groupDataList = new ArrayList<>();
    }

    public void save() {
        updateState();
        saveSettings();
    }

    private void updateState() {
        if (AppSettings.get(userId) != Integer.toString(userData.getUserId()))
            AppSettings.put(userId, Integer.toString(userData.getUserId()));
        if (AppSettings.get(accessToken) != userData.getAccessToken())
            AppSettings.put(accessToken, userData.getAccessToken());
        if (AppSettings.get(enableNotify) != userData.getNotifyType().toString())
            AppSettings.put(enableNotify, userData.getNotifyType().toString());

        StringBuilder groupIds = new StringBuilder();
        for (GroupData group : groupDataList) {
            String curGroup = group.getGroupNameId();
            groupIds.append(curGroup).append(separator);
            String curString = group.isEnableTags().toString();
            if (AppSettings.get(tagEnabled + curGroup) != curString)
                AppSettings.put(tagEnabled + curGroup, curString);
            curString = parseToString(group.getTags(), separator2);
            if (!Objects.equals(curString, "")) {
                if (AppSettings.get(groupTags + curGroup) != curString)
                    AppSettings.put(groupTags + curGroup, curString);
            }
        }

        if (AppSettings.get(savedGroups) != groupIds.toString())
            AppSettings.put(savedGroups, groupIds.toString());

    }

    private void saveSettings() {
        updateState();

        String propDir = "./";
        File file = new File(propDir, "settings.xml");
        try {
            AppSettings.save(file);
            System.out.println("saving settings done...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadSettings() {
        String propDir = "./";
        File file = new File(propDir, "settings.xml");
        int curIntVal;
        String curStrVal;

        try {
            AppSettings.clear();
            AppSettings.load(file);

            curIntVal = Integer.parseInt((String) AppSettings.get(userId));
            curStrVal = (String) AppSettings.get(accessToken);
            if (curIntVal != 0 && curStrVal != null)
                userData = new UserData(curIntVal, curStrVal);

            if (userData != null) {
                curStrVal = (String) AppSettings.get(enableNotify);
                if (curStrVal != null)
                    if (curStrVal.equals("true")) userData.setNotifyType(true);
                    else userData.setNotifyType(false);
            }

            if (groupDataList == null) groupDataList = new ArrayList<>();

            curStrVal = (String) AppSettings.get(savedGroups);
            if (curStrVal != null) {
                List<String> curGroupList = parseToList(curStrVal, separator);
                for (String group : curGroupList) {
                    GroupData groupData = new GroupData();
                    groupData.setGroupNameId(group);
                    curStrVal = (String) AppSettings.get(tagEnabled + group);
                    if (curStrVal != null) {
                        if (curStrVal.equals("true"))
                            groupData.setEnableTags(true);
                        else
                            groupData.setEnableTags(false);
                    }
                    curStrVal = (String) AppSettings.get(groupTags + group);
                    if (curStrVal != null) groupData.setTags(parseToList(curStrVal, separator2));

                    groupDataList.add(groupData);
                }

            }

            System.out.println("loading settings done...");
        } catch (java.io.FileNotFoundException eNotFound) {
            System.out.println("Settings file not found...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<String> parseToList(String s, String separator) {
        List<String> out = new ArrayList<>();
        String[] array = s.split(Pattern.quote(separator));
        out.addAll(Arrays.asList(array));
        return out;
    }

    private String parseToString(List<String> list, String separator) {
        StringBuilder sb = new StringBuilder();
        for (String s : list) {
            sb.append(s);
            sb.append(separator);
        }
        return sb.toString();
    }

}
