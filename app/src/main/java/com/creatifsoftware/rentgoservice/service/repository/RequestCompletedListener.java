package com.creatifsoftware.rentgoservice.service.repository;

/**
 * Created by kerembalaban on 14/01/15.
 */
public interface RequestCompletedListener<MutableLiveData> {
    void onRequestComplete(boolean isSuccess, String message);
}
