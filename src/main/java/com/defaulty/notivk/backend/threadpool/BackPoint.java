package com.defaulty.notivk.backend.threadpool;

import com.defaulty.notivk.backend.threadpool.requests.Request;

import java.util.List;

public interface BackPoint {

    void request(List<Request> requestList);
}
