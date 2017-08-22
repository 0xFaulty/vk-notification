package com.defaulty.notivk.backend;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.UserAuthResponse;
import com.vk.api.sdk.objects.groups.GroupFull;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import com.vk.api.sdk.objects.wall.responses.GetResponse;
import com.vk.api.sdk.queries.groups.GroupField;
import com.vk.api.sdk.queries.users.UserField;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * The class {@code VKSDK} представляет собой обертку в которой формируются
 * и исполняюстся запросы к {@code VkApiClient}.
 */
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

    public UserData getUserAuthData(String code) throws ClientException, ApiException {

        final int APP_ID = 6080102;
        final String REDIRECT_URI = "https://oauth.vk.com/blank.html";
        final String CLIENT_SECRET = "r6YsJq31mlWiNONX64Mq";
        UserAuthResponse authResponse = vk.oauth()
                .userAuthorizationCodeFlow(APP_ID, CLIENT_SECRET, REDIRECT_URI, code)
                .execute();
        return new UserData(authResponse.getUserId(), authResponse.getAccessToken());
    }

    @Nullable
    public GetResponse getWall(String domain, UserData userData, int count, int offset) throws ClientException, ApiException {
        UserActor actor = new UserActor(userData.getIntUserId(), userData.getAccessToken());
        return vk.wall().get(actor)
                .domain(domain)
                .count(count)
                .offset(offset)
                .execute();
    }

    @Nullable
    public GetResponse getWall(int ownerId, UserData userData, int count, int offset) throws ClientException, ApiException {
        UserActor actor = new UserActor(userData.getIntUserId(), userData.getAccessToken());
        return vk.wall().get(actor)
                .ownerId(ownerId)
                .count(count)
                .offset(offset)
                .execute();
    }

    @Nullable
    public ArrayList<GroupFull> getGroupData(List<String> groupNames, UserData userData) throws ClientException, ApiException {
        return getGroupResponse(groupNames, userData);
    }

    @Nullable
    public GroupFull getGroupData(String groupName, UserData userData) throws ClientException, ApiException {
        List<String> stringList = new ArrayList<>();
        stringList.add(groupName);
        ArrayList<GroupFull> groupFullArrayList = getGroupResponse(stringList, userData);
        if (groupFullArrayList.size() > 0) {
            return groupFullArrayList.get(0);
        }
        return null;
    }

    @NotNull
    private ArrayList<GroupFull> getGroupResponse(List<String> groupNames, UserData userData) throws ClientException, ApiException {
        UserActor actor = new UserActor(userData.getIntUserId(), userData.getAccessToken());
        List<GroupFull> groups = vk.groups().getById(actor)
                .groupIds(groupNames)
                .fields(GroupField.MEMBERS_COUNT)
                .execute();
        return new ArrayList<>(groups);
    }

    @Nullable
    public List<UserXtrCounters> getUserInfo(List<String> userIds, UserData userData) throws ClientException, ApiException {
        return getUserInfoResponse(userIds, userData);
    }

    @Nullable
    public UserXtrCounters getUserInfo(String userId, UserData userData) throws ClientException, ApiException {
        List<String> stringList = new ArrayList<>();
        stringList.add(userId);
        List<UserXtrCounters> userXtrCountersArrayList = getUserInfoResponse(stringList, userData);
        if (userXtrCountersArrayList.size() > 0) {
            return userXtrCountersArrayList.get(0);
        }
        return null;
    }

    @NotNull
    private List<UserXtrCounters> getUserInfoResponse(List<String> groupNames, UserData userData) throws ClientException, ApiException {
        UserActor actor = new UserActor(userData.getIntUserId(), userData.getAccessToken());
        return new ArrayList<>(vk.users().get(actor)
                .userIds(groupNames)
                .fields(UserField.ABOUT)
                .fields(UserField.PHOTO_MAX_ORIG)
                .execute());
    }

}
