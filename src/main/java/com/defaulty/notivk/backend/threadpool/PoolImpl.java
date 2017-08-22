package com.defaulty.notivk.backend.threadpool;

import com.defaulty.notivk.backend.threadpool.requests.MultiRequest;
import com.defaulty.notivk.backend.threadpool.requests.Request;
import com.defaulty.notivk.backend.threadpool.requests.enums.RequestState;
import com.defaulty.notivk.backend.threadpool.requests.enums.RequestType;

import java.util.*;

/**
 * The class {@code PoolImpl} представляет собой pool который запускает запросы
 * из контейнера запросов типа {@code MultiRequest} с ограничение по колличеству
 * в заданное время.
 */
public class PoolImpl extends TimerTask implements Pool {

    private List<Request> requestsPool = new ArrayList<>();
    private static final int MAX_CONNECTIONS = 9;
    private static final int STREAM_TIMEOUT = 2000;

    private long lastTime;
    private int currentRequestCount;
    private boolean allowFlag = true;

    private static PoolImpl ourInstance;

    public static synchronized PoolImpl getInstance() {
        if (ourInstance == null) ourInstance = new PoolImpl();
        return ourInstance;
    }

    private PoolImpl() {
        Timer controllerTimer = new Timer(true);
        controllerTimer.scheduleAtFixedRate(this, 0, 1000);
    }

    public void addRequest(Request request) {
        if (request != null) {
            if (request.getRequestType() == RequestType.MULTI) {
                if (!requestsPool.contains(request)) {
                    requestsPool.add(request);
                }
            } else
                castToMultiRequestAndAdd(request);
        }
        else
            throw new NullPointerException("Request can't be null");
    }

    private void castToMultiRequestAndAdd(Request request) {
        MultiRequest multiRequest = new MultiRequest(request.getBackPoint());
        multiRequest.addRequest(request);
        addRequest(multiRequest);
    }

    @Override
    public synchronized void run() {
        if (allowFlag && new Date().getTime() - lastTime >= STREAM_TIMEOUT) {
            for (int i = 0; i < requestsPool.size(); i++) {
                if (currentRequestCount > MAX_CONNECTIONS || requestsPool.isEmpty()) break;
                Request request = ((MultiRequest) requestsPool.get(i)).getNextRequest();
                if (request != null) {
                    request.setRequestState(RequestState.RUN);
                    currentRequestCount++;
                    new Thread(request).start();
                }
            }
            if (currentRequestCount >= MAX_CONNECTIONS) allowFlag = false;
        }
    }

    public synchronized void sendThreadFinish(Request request) {
        if (request != null) {
            currentRequestCount--;
            if (currentRequestCount == 0) {
                lastTime = new Date().getTime();
                allowFlag = true;
            }
            if (request.getRequestType() == RequestType.MULTI) requestsPool.remove(request);
        }
        else
            throw new NullPointerException("Send null request for finish thread");
    }

}
