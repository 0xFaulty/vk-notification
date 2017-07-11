package com.defaulty.notivk.backend;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiParamException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.UserAuthResponse;
import com.vk.api.sdk.objects.groups.GroupFull;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import com.vk.api.sdk.objects.wall.responses.GetResponse;
import com.vk.api.sdk.queries.groups.GroupField;
import com.vk.api.sdk.queries.users.UserField;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class VKSDK {

    private VkApiClient vk = null;

    private static VKSDK ourInstance;

    public static synchronized VKSDK getInstance() {
        if (ourInstance == null) ourInstance = new VKSDK();
        return ourInstance;
    }

    private VKSDK() {
        TransportClient transportClient = HttpTransportClient.getInstance();
        vk = new VkApiClient(transportClient);
    }

    public UserData getUserAuthData(String code) {

        final int APP_ID = 6080102;
        final String REDIRECT_URI = "https://oauth.vk.com/blank.html";
        final String CLIENT_SECRET = "r6YsJq31mlWiNONX64Mq";
        try {
            UserAuthResponse authResponse = vk.oauth()
                    .userAuthorizationCodeFlow(APP_ID, CLIENT_SECRET, REDIRECT_URI, code)
                    .execute();
            return new UserData(authResponse.getUserId(), authResponse.getAccessToken());
        } catch (Exception e) {
            System.out.println("VKSDK.getUserAuthData: " + e.getMessage());
        }
        return null;
    }

    @Nullable
    public GetResponse getWall(String groupName, UserData userData, int count, int offset) {
        UserActor actor = new UserActor(userData.getUserId(), userData.getAccessToken());
        try {
            return vk.wall().get(actor)
                    .domain(groupName)
                    .count(count)
                    .offset(offset)
                    .execute();


        } catch (Exception e) {
            System.out.println("VKSDK.getWall: " + e.getMessage());
        }

        return null;
    }

    @Nullable
    public GetResponse getWall(int ownerId, UserData userData, int count, int offset) {
        UserActor actor = new UserActor(userData.getUserId(), userData.getAccessToken());
        try {
            return vk.wall().get(actor)
                    .ownerId(ownerId)
                    .count(count)
                    .offset(offset)
                    .execute();
        } catch (Exception e) {
            System.out.println("VKSDK.getWall: " + e.getMessage());
        }

        return null;
    }

    @Nullable
    public ArrayList<GroupFull> getGroupData(List<String> groupNames, UserData userData) {
        return getGroupResponse(groupNames, userData);
    }

    @Nullable
    public GroupFull getGroupData(String groupNames, UserData userData) {
        List<String> stringList = new ArrayList<>();
        stringList.add(groupNames);
        ArrayList<GroupFull> groupFullArrayList = getGroupResponse(stringList, userData);
        if (groupFullArrayList != null) {
            if (groupFullArrayList.size() > 0) {
                return groupFullArrayList.get(0);
            }
        }
        return null;
    }

    @Nullable
    private ArrayList<GroupFull> getGroupResponse(List<String> groupNames, UserData userData) {

        UserActor actor = new UserActor(userData.getUserId(), userData.getAccessToken());

        try {
            List<GroupFull> groups = vk.groups().getById(actor)
                    .groupIds(groupNames)
                    .fields(GroupField.MEMBERS_COUNT)
                    .execute();
            return new ArrayList<>(groups);

        } catch (ApiParamException e) {
            //System.out.println("Groups not found, not a error")
        } catch (Exception e) {
            System.out.println("VKSDK.getGroupResponse: " + e.getMessage());
        }

        return null;
    }

    @Nullable
    public ArrayList<UserXtrCounters> getUserInfo(List<String> userIds, UserData userData) {
        return getUserInfoResponse(userIds, userData);
    }

    @Nullable
    public UserXtrCounters getUserInfo(int userIds, UserData userData) {
        return getUserInfo(userIds + "", userData);
    }

    @Nullable
    public UserXtrCounters getUserInfo(String userIds, UserData userData) {
        List<String> stringList = new ArrayList<>();
        stringList.add(userIds);
        ArrayList<UserXtrCounters> userXtrCountersArrayList = getUserInfoResponse(stringList, userData);
        if (userXtrCountersArrayList != null) {
            if (userXtrCountersArrayList.size() > 0) {
                return userXtrCountersArrayList.get(0);
            }
        }
        return null;
    }

    @Nullable
    private ArrayList<UserXtrCounters> getUserInfoResponse(List<String> groupNames, UserData userData) {

        UserActor actor = new UserActor(userData.getUserId(), userData.getAccessToken());
        try {
            return new ArrayList<>(vk.users().get(actor)
                    .userIds(groupNames)
                    .fields(UserField.ABOUT)
                    .fields(UserField.PHOTO_MAX_ORIG)
                    .execute());
        } catch (Exception e) {
            System.out.println("VKSDK.getUserInfoResponse: " + e.getMessage());
        }

        return null;
    }

}
