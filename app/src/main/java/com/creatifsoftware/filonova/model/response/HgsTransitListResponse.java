package com.creatifsoftware.filonova.model.response;

import com.creatifsoftware.filonova.model.HgsItem;
import com.creatifsoftware.filonova.model.base.BaseResponse;

import java.util.List;

/**
 * Created by kerembalaban on 17.02.2019 at 19:47.
 */
public class HgsTransitListResponse extends BaseResponse {
    public List<HgsItem> transits;
    public boolean showErrorMessage;
}
