package com.creatifsoftware.filonova.model.response;

import com.creatifsoftware.filonova.model.ReservationItem;
import com.creatifsoftware.filonova.model.base.BaseResponse;

import java.util.List;

/**
 * Created by kerembalaban on 17.02.2019 at 19:47.
 */
public class ReservationListResponse extends BaseResponse {
    public List<ReservationItem> reservationList;
}
