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

public abstract class Request implements Runnable {

    protected static VKSDK vksdk = VKSDK.getInstance();
    protected static Pool pool = PoolImpl.getInstance();

    private static int lastRequestId = 0;
    private int requestId;

    private RequestState requestState;
    private RequestType requestType;
    private BackPoint backPoint;

    public Request(RequestType requestType, BackPoint backPoint) {
        this.requestType = requestType;
        this.backPoint = backPoint;
        this.requestState = RequestState.Added;
    }

    public void send(Pool pool) {
        if (pool != null) {
            //this.pool = pool; //TODO: сделать pool не singleton
            pool.addRequest(this);
        } else
            throw new IllegalArgumentException("Poll must be not null");
    }

    protected void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getRequestId() {
        return requestId;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    protected int generateNext() {
        lastRequestId++;
        return lastRequestId;
    }

    public void setBackPoint(BackPoint backPoint) {
        this.backPoint = backPoint;
    }

    public RequestState getRequestState() {
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
        List<Request> list = new ArrayList<>();
        list.add(this);
        backPoint.request(list);
    }

    public synchronized void run() {
        try {
            processRequest();
            requestState = RequestState.Finished;
        } catch (ApiTooManyException e) {
            requestState = RequestState.Added;
            //System.out.println("Too many requests, set RequestState.Added for " + requestType);
        } catch (ApiParamException | ApiAccessException e) {
            requestState = RequestState.Finished;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        pool.sendThreadFinish(this);
        if (requestState == RequestState.Finished) goBackPoint();

    }

    abstract void processRequest() throws ClientException, ApiException;
}
