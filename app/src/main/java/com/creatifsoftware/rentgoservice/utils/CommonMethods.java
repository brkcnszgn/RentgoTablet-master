package com.creatifsoftware.rentgoservice.utils;

import android.content.Context;

import com.creatifsoftware.rentgoservice.RentgoServiceApp;
import com.creatifsoftware.rentgoservice.model.AdditionalProduct;
import com.creatifsoftware.rentgoservice.model.Branch;
import com.creatifsoftware.rentgoservice.model.DamageDocument;
import com.creatifsoftware.rentgoservice.model.DamageSize;
import com.creatifsoftware.rentgoservice.model.DamageType;
import com.creatifsoftware.rentgoservice.model.EquipmentPart;
import com.creatifsoftware.rentgoservice.model.GroupCodeInformation;
import com.creatifsoftware.rentgoservice.model.OptionSetItem;
import com.creatifsoftware.rentgoservice.model.response.MasterDataResponse;

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
        MasterDataResponse response = SharedPrefUtils.instance.getSavedObjectFromPreference(RentgoServiceApp.getContext(), KEY_MASTER_DATA_RESPONSE, MasterDataResponse.class);
        return response.groupCodeList.stream().filter(e -> e.groupCodeId.equals(groupCodeId)).findAny().orElse(null);
    }

    public OptionSetItem getSelectedStatusInformation(Context context, int statusCode) {
        return getEquipmentStatusList(context).stream().filter(e -> e.value == statusCode).findAny().orElse(null);
    }
}
