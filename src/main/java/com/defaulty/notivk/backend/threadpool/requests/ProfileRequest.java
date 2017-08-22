package com.defaulty.notivk.backend.threadpool.requests;

import com.defaulty.notivk.backend.UserData;
import com.defaulty.notivk.backend.threadpool.BackPoint;
import com.defaulty.notivk.backend.threadpool.requests.enums.RequestType;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.users.UserXtrCounters;

import java.util.List;

/**
 * The class {@code ProfileRequest} используется для создания и исполнения запросов типа getUserInfo.
 */
public class ProfileRequest extends Request {

    private boolean listType;
    private String userId;
    private List<String> userIds;
    private UserData userData;

    private UserXtrCounters response;
    private List<UserXtrCounters> responseList;

    public ProfileRequest(String userId, UserData userData, BackPoint backPoint) {
        super(RequestType.PROFILE, backPoint);
        super.setRequestId(0);
        this.listType = false;
        this.userId = userId;
        this.userData = userData;
    }

    public ProfileRequest(List<String> userIds, UserData userData, BackPoint backPoint) {
        super(RequestType.PROFILE, backPoint);
        super.setRequestId(0);
        this.listType = true;
        this.userIds = userIds;
        this.userData = userData;
    }

    public UserXtrCounters getResponse() {
        return response;
    }

    public List<UserXtrCounters> getResponseList() {
        return responseList;
    }

    public void processRequest() throws ClientException, ApiException {
        if (listType)
            responseList = vksdk.getUserInfo(userIds, userData);
        else
            response = vksdk.getUserInfo(userId, userData);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProfileRequest that = (ProfileRequest) o;
        return getRequestId() == that.getRequestId();
    }

    @Override
    public int hashCode() {
        return getRequestId() + getClass().getName().hashCode();
    }

}
