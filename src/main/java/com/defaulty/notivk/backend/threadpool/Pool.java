package com.defaulty.notivk.backend.threadpool;

import com.defaulty.notivk.backend.threadpool.requests.Request;

/**
 * Interface {@code Pool} предназначен для описания методов необходимых для
 * реализации в pool(а) запросов.
 */
public interface Pool {
    void addRequest(Request request);
    void sendThreadFinish(Request request);
}
