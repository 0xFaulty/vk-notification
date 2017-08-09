package com.defaulty.notivk.backend.threadpool;

import com.defaulty.notivk.backend.threadpool.requests.Request;

public interface Pool {
    void addRequest(Request request);
    void sendThreadFinish(Request request);
}
