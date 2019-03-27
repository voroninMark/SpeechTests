package com.example.speechtests.HTTP;

public interface VolleyResponseListener {
    void onError(String message);
    void onResponse(Object response);
}
