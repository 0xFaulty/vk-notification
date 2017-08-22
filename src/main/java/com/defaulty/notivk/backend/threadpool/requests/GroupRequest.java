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
 * The class {@code ProfileRequest} используется для создания и исполнения запросов типа getGroupData.
 */
public class GroupRequest extends Request {

    private List<String> groupNameList;
    private String groupName;
    private UserData userData;

    private boolean listType;

    private List<GroupFull> responseList;
    private GroupFull response;

    public GroupRequest(@NotNull List<String> groupIdList, @NotNull UserData userData, BackPoint backPoint) {
        super(RequestType.GROUP, backPoint);
        super.setRequestId(0);
        for (String name : groupIdList)
            if (name.equals("")) throw new IllegalArgumentException("Empty group name in list");
        this.groupNameList = groupIdList;
        this.userData = userData;
        this.listType = true;
    }

    public GroupRequest(@NotNull String groupId, @NotNull UserData userData, BackPoint backPoint) {
        super(RequestType.GROUP, backPoint);
        super.setRequestId(groupId.hashCode());
        if (groupId.equals("")) throw new IllegalArgumentException("Empty group name");
        this.groupName = groupId;
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
