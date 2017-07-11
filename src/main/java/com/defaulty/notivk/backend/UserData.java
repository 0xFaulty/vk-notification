package com.defaulty.notivk.backend;

class UserData {

    private int userId = 0;
    private String accessToken = "";
    private Boolean notifyType = true;
    private String userName = "";

    private boolean userDataSet = false;

    UserData() {
    }

    UserData(int userId, String accessToken) {
        this.userId = userId;
        this.accessToken = accessToken;
        this.userDataSet = true;
    }

    int getUserId() {
        return userId;
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
