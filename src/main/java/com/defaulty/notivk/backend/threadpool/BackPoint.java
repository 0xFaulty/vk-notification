package com.defaulty.notivk.backend.threadpool;

import com.defaulty.notivk.backend.threadpool.requests.Request;

import java.util.List;

/**
 * Interface {@code BackPoint} предназначен для описания метода который передается в
 * запрос в качестве точки возврата, в которую будет возвращён исходный запрос с
 * включенным в себя полученным результатом.
 */
public interface BackPoint {
    void send(List<Request> requestList);
}
