package com.defaulty.notivk.backend.threadpool.requests;

import com.defaulty.notivk.backend.threadpool.BackPoint;
import com.defaulty.notivk.backend.threadpool.requests.enums.RequestState;
import com.defaulty.notivk.backend.threadpool.requests.enums.RequestType;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;

import java.util.ArrayList;
import java.util.List;

/**
 * The class {@code MultiRequest} используется для создания и исполнения контейнеров запросов любого типа.
 */
public class MultiRequest extends Request {

    private List<Request> requestsList = new ArrayList<>();

    public MultiRequest(BackPoint backPoint) {
        super(RequestType.MULTI, backPoint);
        super.setRequestId(generateNext());
    }

    /**
     * @param backPoint используется для определения метода который будет вызван
     *                  при завершении всех запросов в контейнере.
     * @param singleTaskName используется для обозначения одинаковых задач
     *                       чтобы избежать их дублирования в pool.
     */
    public MultiRequest(BackPoint backPoint, String singleTaskName) {
        super(RequestType.MULTI, backPoint);
        super.setRequestId(singleTaskName.hashCode());
    }

    public void addRequest(Request request) {
        if (request.getRequestType() == RequestType.MULTI)
            throw new IllegalArgumentException("You can't add MultiRequest in MultiRequest");
        else if (!requestsList.contains(request)) {
            requestsList.add(request);
        } else
            System.out.println(request.getRequestType() + " in MULTI container already added");
    }

    public Request getNextRequest() {
        for (Request request : requestsList) {
            if (request.getRequestState() == RequestState.ADD) {
                request.setBackPoint(this::backPoint);
                return request;
            }
        }
        return null;
    }

    @Override
    public void run() {
        boolean flag = false;
        for (Request r : requestsList)
            if (r.getRequestState() != RequestState.FINISH) flag = true;
        if (!flag) {
            pool.sendThreadFinish(this);
            BackPoint backPoint = getBackPoint();
            if (backPoint != null) backPoint.send(requestsList);
        }
    }

    @Override
    void processRequest() throws ClientException, ApiException {
    }

    private void backPoint(List<Request> request) {
        run();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MultiRequest that = (MultiRequest) o;
        return getRequestId() == that.getRequestId();
    }

    @Override
    public int hashCode() {
        return getRequestId() + getClass().getName().hashCode();
    }

}
