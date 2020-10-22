package com.creatifsoftware.filonova.model.response;

import com.creatifsoftware.filonova.model.FinePriceItem;
import com.creatifsoftware.filonova.model.base.BaseResponse;

import java.util.List;

/**
 * Created by kerembalaban on 11.03.2019 at 16:05.
 */
public class FinePriceResponse extends BaseResponse {
    public List<FinePriceItem> tollAmountList;
    public List<FinePriceItem> fineAmountList;
    public List<FinePriceItem> extraPaymentList;
    public FinePriceItem damageAmount;
    //hgsKalemleri
    //trafik ceza kalemleri

}
