package com.creatifsoftware.rentgoservice.model.response;

import com.creatifsoftware.rentgoservice.model.DamageDocument;
import com.creatifsoftware.rentgoservice.model.DamageSize;
import com.creatifsoftware.rentgoservice.model.DamageType;
import com.creatifsoftware.rentgoservice.model.EquipmentPart;
import com.creatifsoftware.rentgoservice.model.base.BaseResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by kerembalaban on 7.04.2019 at 23:36.
 */
public class DamageMasterDataResponse extends BaseResponse implements Serializable {
    public List<EquipmentPart> equipmentPartList;
    public List<DamageType> damageTypeList;
    public List<DamageSize> damageSizeList;
    public List<DamageDocument> damageDocumentList;
}
