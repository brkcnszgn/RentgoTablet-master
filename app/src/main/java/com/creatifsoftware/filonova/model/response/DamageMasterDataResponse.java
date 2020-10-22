package com.creatifsoftware.filonova.model.response;

import com.creatifsoftware.filonova.model.DamageDocument;
import com.creatifsoftware.filonova.model.DamageSize;
import com.creatifsoftware.filonova.model.DamageType;
import com.creatifsoftware.filonova.model.EquipmentPart;
import com.creatifsoftware.filonova.model.base.BaseResponse;

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
