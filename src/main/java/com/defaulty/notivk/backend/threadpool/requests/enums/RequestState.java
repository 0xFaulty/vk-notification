package com.defaulty.notivk.backend.threadpool.requests.enums;

/**
 * Enum {@code RequestState} используется для обозначения состояния исполнения запроса
 * ADD - запрос добавлен в pool,
 * RUN - выполнение запроса запущено в потоке,
 * FINISH - ответ на запрос получен.
 */
public enum RequestState {
    ADD, RUN, FINISH
}
