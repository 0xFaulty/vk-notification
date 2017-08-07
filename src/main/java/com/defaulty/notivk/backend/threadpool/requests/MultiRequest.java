package com.defaulty.notivk.backend.threadpool.requests;

import com.defaulty.notivk.backend.threadpool.BackPoint;
import com.defaulty.notivk.backend.threadpool.requests.enums.RequestState;
import com.defaulty.notivk.backend.threadpool.requests.enums.RequestType;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;

import java.util.ArrayList;
import java.util.List;

/**
 * MultiRequest guarantees that all contained requests will be execute lossless
 */

public class MultiRequest extends Request {

    private List<Request> requestsList = new ArrayList<>();

    public MultiRequest(BackPoint backPoint) {
        super(RequestType.Multi, backPoint);
        super.setRequestId(generateNext());
    }

    public MultiRequest(BackPoint backPoint, String singleTaskName) {
        super(RequestType.Multi, backPoint);
        //Not add task with same hashcode before finish current
        super.setRequestId(singleTaskName.hashCode());
    }

    public void addRequest(Request request) {
        if (request.getRequestType() == RequestType.Multi)
            throw new IllegalArgumentException("You can't add MultiRequest in MultiRequest");
        else if (!requestsList.contains(request)) {
            requestsList.add(request);
        } else
            System.out.println(request.getRequestType() + " in Multi container already added");
    }

    public Request getNext() {
        int currentListElement = 0;
        Request request;

        while (requestsList.size() > currentListElement) {
            request = requestsList.get(currentListElement);
            if (request.getRequestState() == RequestState.Added) {
                request.setBackPoint(this::backPoint);
                return request;
            } else
                currentListElement++;
        }
        return null;
    }

    @Override
    public void run() {
        boolean flag = false;
        for (Request r : requestsList) {
            if (r.getRequestState() != RequestState.Finished) flag = true;
        }
        if (!flag) {
            setRequestState(RequestState.Finished);
            getBackPoint().request(requestsList);
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
