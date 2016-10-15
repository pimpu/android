package com.alchemistdigital.buxa.utilities;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by user on 8/23/2016.
 */
public class RestClient {
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.get(url, params, responseHandler);
    }

    public static void post(String url, RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.post(url, params, responseHandler);
    }

    public static void postWithHeader(String url, String apiKeyHeader, RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.addHeader("Authorization", apiKeyHeader);
        client.post(url, params, responseHandler);
    }

    public static void put(String url, RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.put(url, params, responseHandler);
    }
}
