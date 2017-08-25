package com.defaulty.notivk.backend.threadpool.requests;

import com.defaulty.notivk.backend.UserData;
import com.defaulty.notivk.backend.threadpool.BackPoint;
import com.defaulty.notivk.backend.threadpool.requests.enums.RequestType;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;

/**
 * The class {@code ProfileRequest} используется для создания и исполнения запросов типа getUserAuthData.
 */
public class CodeRequest extends Request {

    private String code;
    private UserData response;

    public CodeRequest(String code, BackPoint backPoint) {
        super(RequestType.CODE, backPoint);
        super.setRequestId(0);
        this.code = code;
    }

    public UserData getResponse() {
        return response;
    }

    public void processRequest() throws ClientException, ApiException {
        response = vksdk.getUserAuthData(code);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CodeRequest that = (CodeRequest) o;
        return getRequestId() == that.getRequestId();
    }

    @Override
    public int hashCode() {
        return getRequestId() + getClass().getName().hashCode();
    }

}
