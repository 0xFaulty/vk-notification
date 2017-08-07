package com.defaulty.notivk.backend;

public class UserData {

    private String userId = "";
    private String accessToken = "";
    private Boolean notifyType = true;
    private String userName = "";

    private boolean userDataSet = false;

    public UserData() {
    }

    public UserData(String userId, String accessToken) {
        this.userId = userId;
        this.accessToken = accessToken;
        this.userDataSet = true;
    }

    public UserData(int userId, String accessToken) {
        this.userId = Integer.toString(userId);
        this.accessToken = accessToken;
        this.userDataSet = true;
    }

    String getUserId() {
        return userId;
    }

    int getIntUserId() {
        return Integer.parseInt(userId);
    }

    String getAccessToken() {
        return accessToken;
    }

    Boolean getNotifyType() {
        return notifyType;
    }

    void setNotifyType(Boolean notifyType) {
        this.notifyType = notifyType;
    }

    public boolean isUserDataSet() {
        return userDataSet;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
