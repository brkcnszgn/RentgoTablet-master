package com.creatifsoftware.filonova.utils;

import android.content.Context;

import com.creatifsoftware.filonova.model.AdditionalProduct;
import com.creatifsoftware.filonova.model.Branch;
import com.creatifsoftware.filonova.model.DamageDocument;
import com.creatifsoftware.filonova.model.DamageSize;
import com.creatifsoftware.filonova.model.DamageType;
import com.creatifsoftware.filonova.model.EquipmentPart;
import com.creatifsoftware.filonova.model.GroupCodeInformation;
import com.creatifsoftware.filonova.model.OptionSetItem;
import com.creatifsoftware.filonova.model.response.MasterDataResponse;

import java.util.List;

/**
 * Created by kerembalaban on 4.04.2019 at 02:46.
 */
public final class CommonMethods {
    public static final CommonMethods instance = new CommonMethods();
    private static final String KEY_MASTER_DATA_RESPONSE = "master_data_response";

    public double calculateAddedAdditionalProductsAmount(List<AdditionalProduct> additionalProducts) {
        double totalPrice = 0;
        for (AdditionalProduct item : additionalProducts) {
            totalPrice = totalPrice + (item.actualTotalAmount * item.value);
        }

        return totalPrice;
    }

    public List<Branch> getBranchList(Context context) {
        MasterDataResponse response = SharedPrefUtils.instance.getSavedObjectFromPreference(context, KEY_MASTER_DATA_RESPONSE, MasterDataResponse.class);

        return response.branchList;
    }

    public List<OptionSetItem> getEquipmentStatusList(Context context) {
        MasterDataResponse response = SharedPrefUtils.instance.getSavedObjectFromPreference(context, KEY_MASTER_DATA_RESPONSE, MasterDataResponse.class);

        return response.equipmentStatusOptionSetList;
    }

    public List<DamageType> getDamageTypeList(Context context) {
        MasterDataResponse response = SharedPrefUtils.instance.getSavedObjectFromPreference(context, KEY_MASTER_DATA_RESPONSE, MasterDataResponse.class);

        return response.damageTypeList;
    }

    public List<DamageSize> getDamageSizeList(Context context) {
        MasterDataResponse response = SharedPrefUtils.instance.getSavedObjectFromPreference(context, KEY_MASTER_DATA_RESPONSE, MasterDataResponse.class);

        return response.damageSizeList;
    }

    public List<DamageDocument> getDamageDocumentList(Context context) {
        MasterDataResponse response = SharedPrefUtils.instance.getSavedObjectFromPreference(context, KEY_MASTER_DATA_RESPONSE, MasterDataResponse.class);

        return response.damageDocumentList;
    }

    public List<EquipmentPart> getEquipmentPartList(Context context) {
        MasterDataResponse response = SharedPrefUtils.instance.getSavedObjectFromPreference(context, KEY_MASTER_DATA_RESPONSE, MasterDataResponse.class);

        return response.equipmentPartList;
    }

    public List<GroupCodeInformation> getGroupCodeList(Context context) {
        MasterDataResponse response = SharedPrefUtils.instance.getSavedObjectFromPreference(context, KEY_MASTER_DATA_RESPONSE, MasterDataResponse.class);

        return response.groupCodeList;
    }

    public GroupCodeInformation getSelectedGroupCodeInformation(String groupCodeId) {
        MasterDataResponse response = SharedPrefUtils.instance.getSavedObjectFromPreference(com.creatifsoftware.filonova.FilonovaServiceApp.getContext(), KEY_MASTER_DATA_RESPONSE, MasterDataResponse.class);
        return response.groupCodeList.stream().filter(e -> e.groupCodeId.equals(groupCodeId)).findAny().orElse(null);
    }

    public OptionSetItem getSelectedStatusInformation(Context context, int statusCode) {
        return getEquipmentStatusList(context).stream().filter(e -> e.value == statusCode).findAny().orElse(null);
    }

    public List<AdditionalProduct> getExtraServiceList(Context context) {
        MasterDataResponse response = SharedPrefUtils.instance.getSavedObjectFromPreference(context, KEY_MASTER_DATA_RESPONSE, MasterDataResponse.class);
        return response.otherAdditionalProducts;
    }
}
