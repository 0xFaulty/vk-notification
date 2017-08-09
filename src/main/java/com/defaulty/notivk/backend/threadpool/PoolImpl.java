package com.defaulty.notivk.backend.threadpool;

import com.defaulty.notivk.backend.threadpool.requests.MultiRequest;
import com.defaulty.notivk.backend.threadpool.requests.Request;
import com.defaulty.notivk.backend.threadpool.requests.enums.RequestState;
import com.defaulty.notivk.backend.threadpool.requests.enums.RequestType;

import java.util.*;

public class PoolImpl extends TimerTask implements Pool {

    private List<Request> requestsPool = new ArrayList<>();
    private static final int MAX_CONNECTIONS = 9;

    private long lastTime = 0;
    private int currentRequestCount = 0;
    private boolean allowFlag = true;

    private static PoolImpl ourInstance;

    public static synchronized PoolImpl getInstance() {
        if (ourInstance == null) ourInstance = new PoolImpl();
        return ourInstance;
    }

    private PoolImpl() {
        Timer controllerTimer = new Timer(true);
        controllerTimer.scheduleAtFixedRate(this, 0, 10);
    }

    public void addRequest(Request request) {
        if (request.getRequestType() == RequestType.Multi) {
            if (!requestsPool.contains(request)) {
                requestsPool.add(request);
            }
        }
        else
            castToMultiRequestAndAdd(request);
    }

    private void castToMultiRequestAndAdd(Request request) {
        MultiRequest multiRequest = new MultiRequest(request.getBackPoint());
        multiRequest.addRequest(request);
        multiRequest.send(this);
    }

    @Override
    public synchronized void run() {
        int currentListElement = 0;
        if (allowFlag && new Date().getTime() - lastTime >= 2000) {
            while (currentRequestCount < MAX_CONNECTIONS && !requestsPool.isEmpty() && requestsPool.size() > currentListElement) {
                Request request = ((MultiRequest) requestsPool.get(currentListElement)).getNext();
                if (request != null) {
                    request.setRequestState(RequestState.Runned);
                    currentRequestCount++;
                    new Thread(request).start();
                } else
                    currentListElement++;
            }
            if (currentRequestCount >= MAX_CONNECTIONS) allowFlag = false;
        }
    }

    public synchronized void sendThreadFinish(Request request) {
        currentRequestCount--;
        if (currentRequestCount == 0) {
            lastTime = new Date().getTime();
            allowFlag = true;
        }
        if (request.getRequestType() == RequestType.Multi) {
            requestsPool.remove(request);
        }
    }

}
