package com.defaulty.notivk.backend.threadpool.requests;

import com.defaulty.notivk.backend.UserData;
import com.defaulty.notivk.backend.threadpool.BackPoint;
import com.defaulty.notivk.backend.threadpool.requests.enums.RequestType;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.wall.responses.GetResponse;

/**
 * The class {@code WallRequest} используется для создания и исполнения запросов типа getWall.
 */
public class WallRequest extends Request {

    private boolean typeDomain;
    private String domain;
    private int ownerId;
    private UserData userData;
    private int count;
    private int offset;

    private GetResponse response;

    public WallRequest(String domain, UserData userData, int count, int offset, BackPoint backPoint) {
        super(RequestType.WALL, backPoint);
        super.setRequestId(domain.hashCode());
        this.typeDomain = true;
        this.domain = domain;
        this.userData = userData;
        this.count = count;
        this.offset = offset;
    }

    public WallRequest(int ownerId, UserData userData, int count, int offset, BackPoint backPoint) {
        super(RequestType.WALL, backPoint);
        super.setRequestId(Integer.toString(ownerId).hashCode());
        this.typeDomain = false;
        this.ownerId = ownerId;
        this.userData = userData;
        this.count = count;
        this.offset = offset;
    }

    public GetResponse getResponse() {
        return response;
    }

    public void processRequest() throws ClientException, ApiException {
        if (typeDomain)
            response = vksdk.getWall(domain, userData, count, offset);
        else
            response = vksdk.getWall(ownerId, userData, count, offset);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WallRequest that = (WallRequest) o;
        return getRequestId() == that.getRequestId();
    }

    @Override
    public int hashCode() {
        return getRequestId() + getClass().getName().hashCode();
    }
}
