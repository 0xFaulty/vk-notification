package com.defaulty.notivk.backend.threadpool.requests;

import com.defaulty.notivk.backend.UserData;
import com.defaulty.notivk.backend.threadpool.BackPoint;
import com.defaulty.notivk.backend.threadpool.requests.enums.RequestType;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.groups.GroupFull;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * 18/07/2017
 */
public class GroupRequest extends Request {

    private List<String> groupNameList;
    private String groupName;
    private UserData userData;

    private boolean listType;

    private List<GroupFull> responseList;
    private GroupFull response;

    public GroupRequest(@NotNull List<String> groupNameList, @NotNull UserData userData, BackPoint backPoint) {
        super(RequestType.Group, backPoint);
        super.setRequestId(0);
        for (String name : groupNameList)
            if (name.equals("")) throw new IllegalArgumentException("Empty group name in list");
        this.groupNameList = groupNameList;
        this.userData = userData;
        this.listType = true;
    }

    public GroupRequest(@NotNull String groupName, @NotNull UserData userData, BackPoint backPoint) {
        super(RequestType.Group, backPoint);
        super.setRequestId(groupName.hashCode());
        if (groupName.equals("")) throw new IllegalArgumentException("Empty group name");
        this.groupName = groupName;
        this.userData = userData;
        this.listType = false;
    }

    public List<GroupFull> getResponseList() {
        return responseList;
    }

    public GroupFull getResponse() {
        return response;
    }

    public void processRequest() throws ClientException, ApiException {
        if (listType)
            responseList = vksdk.getGroupData(groupNameList, userData);
        else
            response = vksdk.getGroupData(groupName, userData);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GroupRequest that = (GroupRequest) o;
        return getRequestId() == that.getRequestId();
    }

    @Override
    public int hashCode() {
        return getRequestId() + getClass().getName().hashCode();
    }

    public String getGroupName() {
        return groupName;
    }
}
