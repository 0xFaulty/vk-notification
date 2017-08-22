package com.defaulty.notivk.backend;

/**
 * The class {@code UserData} содержит данные о текущем пользователе.
 * userId - идентификационный номер пользователя,
 * userName - имя и фамилия полученные по api,
 * accessToken - токен для прохождения идентификации oauth,
 * notifyState - флаг настроек отвечающих за показ всплывающего окна уведомления.
 */
public class UserData {

    private String userId = "";
    private String userName = "";
    private String accessToken = "";
    private Boolean notifyState = true;

    private boolean userDataSet = false;

    UserData() {
    }

    UserData(String userId, String accessToken) {
        this.userId = userId;
        this.accessToken = accessToken;
        this.userDataSet = true;
    }

    UserData(int userId, String accessToken) {
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

    Boolean getNotifyState() {
        return notifyState;
    }

    void setNotifyState(Boolean notifyState) {
        this.notifyState = notifyState;
    }

    boolean isUserDataSet() {
        return userDataSet;
    }

    String getUserName() {
        return userName;
    }

    void setUserName(String userName) {
        this.userName = userName;
    }
}
