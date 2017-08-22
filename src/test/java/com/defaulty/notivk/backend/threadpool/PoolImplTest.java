package com.defaulty.notivk.backend.threadpool;

import com.defaulty.notivk.backend.SettingsWrapper;
import com.defaulty.notivk.backend.threadpool.requests.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class PoolImplTest {

    private static PoolImpl pool = PoolImpl.getInstance();
    private static SettingsWrapper settings = SettingsWrapper.getInstance();

    @Test
    public void addMultiRequest() throws Exception {
        MultiRequest multiRequest = new MultiRequest(null);
        multiRequest.addRequest(new WallRequest(1, settings.getUserData(), 10, 0, null));
        multiRequest.addRequest(new GroupRequest("1", settings.getUserData(), null));
        multiRequest.addRequest(new ProfileRequest("1", settings.getUserData(), null));
        multiRequest.addRequest(new CodeRequest("1", null));
        pool.addRequest(multiRequest);
    }

    @Test
    public void addSimpleRequests() throws Exception {
        pool.addRequest(new WallRequest(1, settings.getUserData(), 10, 0, null));
        pool.addRequest(new GroupRequest("1", settings.getUserData(), null));
        pool.addRequest(new ProfileRequest("1", settings.getUserData(), null));
        pool.addRequest(new CodeRequest("1", null));
    }

    @Test(expected = NullPointerException.class)
    public void addRequestNull() throws Exception {
        pool.addRequest(null);
    }

    @Test
    public void addRequestEmpty() throws Exception {
        MultiRequest multiRequest = new MultiRequest(null);
        pool.addRequest(multiRequest);
    }

    @Test
    public void run() throws Exception {
        pool.run();
    }

    @Test(expected = NullPointerException.class)
    public void sendThreadFinishNull() throws Exception {
        pool.sendThreadFinish(null);
    }

}