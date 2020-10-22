package com.creatifsoftware.filonova.model;

import android.content.Context;

import com.creatifsoftware.filonova.model.base.ResponseResult;
import com.creatifsoftware.filonova.utils.SharedPrefUtils;

import java.io.Serializable;

/**
 * Created by kerembalaban on 21.02.2019 at 01:10.
 */
public class User implements Serializable {
    public String userId;
    public String fullname;
    public Branch userBranch;
    public ResponseResult responseResult;

    public User getUser(Context context) {
        return SharedPrefUtils.instance.getSavedObjectFromPreference(context, "user", User.class);
    }
}
