package com.creatifsoftware.filonova.model.base;

/**
 * Created by kerembalaban on 17.02.2019 at 23:35.
 */
public class ResponseResult {
    public boolean result;
    public String exceptionDetail;

    public ResponseResult(boolean result, String message) {
        this.result = result;
        this.exceptionDetail = message;
    }
}
