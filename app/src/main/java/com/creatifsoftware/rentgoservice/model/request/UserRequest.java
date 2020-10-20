package com.creatifsoftware.rentgoservice.model.request;

import com.creatifsoftware.rentgoservice.model.base.BaseRequest;

/**
 * Created by kerembalaban on 21.02.2019 at 01:11.
 */
public class UserRequest extends BaseRequest {
    public String userName;
    public String password;
    public String version;

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
