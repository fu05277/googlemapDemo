package com.example.cyut.googlemapmaster.Conn;

import com.loopj.android.http.AsyncHttpClient;

/**
 * Created by CYUT on 2018/6/1.
 */

public class BaseAsyncHttpClient {
    protected static AsyncHttpClient mClient = init();

    private static AsyncHttpClient init() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(50 * 1000);
        client.setEnableRedirects(true, true, true);
        return client;
    }

}
