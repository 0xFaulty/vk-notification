package com.defaulty.notivk.backend.threadpool.requests;

import com.defaulty.notivk.backend.VKSDK;
import com.defaulty.notivk.backend.threadpool.BackPoint;
import com.defaulty.notivk.backend.threadpool.Pool;
import com.defaulty.notivk.backend.threadpool.PoolImpl;
import com.defaulty.notivk.backend.threadpool.requests.enums.RequestState;
import com.defaulty.notivk.backend.threadpool.requests.enums.RequestType;
import com.vk.api.sdk.exceptions.*;

import java.util.ArrayList;
import java.util.List;

/**
 * The abstract class {@code Request} включает в себя общие методы контейнера запроса.
 */
public abstract class Request implements Runnable {

    static VKSDK vksdk = VKSDK.getInstance();
    static Pool pool = PoolImpl.getInstance();

    private static int lastRequestId;
    private int requestId;

    private RequestState requestState;
    private RequestType requestType;
    private BackPoint backPoint;

    public Request(RequestType requestType, BackPoint backPoint) {
        this.requestType = requestType;
        this.backPoint = backPoint;
        this.requestState = RequestState.ADD;
    }

    void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    int getRequestId() {
        return requestId;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    int generateNext() {
        return ++lastRequestId;
    }

    void setBackPoint(BackPoint backPoint) {
        this.backPoint = backPoint;
    }

    RequestState getRequestState() {
        return requestState;
    }

    public void setRequestState(RequestState requestState) {
        this.requestState = requestState;
    }

    public BackPoint getBackPoint() {
        return backPoint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Request that = (Request) o;
        return getRequestId() == that.getRequestId();
    }

    @Override
    public int hashCode() {
        return getRequestId();
    }

    private synchronized void goBackPoint() {
        if (backPoint != null) {
            List<Request> list = new ArrayList<>();
            list.add(this);
            backPoint.send(list);
        }
    }

    public synchronized void run() {
        try {
            processRequest();
            requestState = RequestState.FINISH;
        } catch (ApiTooManyException e) {
            requestState = RequestState.ADD;
        } catch (ClientException e) {
            if (e.getMessage().indexOf("Code is invalid or expired.") > 0)
                requestState = RequestState.FINISH;
            else
                requestState = RequestState.ADD;
        } catch (ApiParamException | ApiAccessException e) {
            requestState = RequestState.FINISH;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        pool.sendThreadFinish(this);
        if (requestState == RequestState.FINISH) goBackPoint();

    }

    abstract void processRequest() throws ClientException, ApiException;
}
