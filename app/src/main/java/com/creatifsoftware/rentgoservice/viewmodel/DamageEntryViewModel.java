package com.creatifsoftware.rentgoservice.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.creatifsoftware.rentgoservice.model.DamageItem;
import com.creatifsoftware.rentgoservice.model.response.DamageListResponse;
import com.creatifsoftware.rentgoservice.service.repository.ContractRepository;
import com.creatifsoftware.rentgoservice.utils.DateUtils;

import javax.inject.Inject;

public class DamageEntryViewModel extends AndroidViewModel {
    private static final String TAG = DamageEntryViewModel.class.getName();
    private static final MutableLiveData ABSENT = new MutableLiveData();

    private final LiveData<DamageListResponse> damageListObservable;
    //private final LiveData<DamageMasterDataResponse> damageMasterDataObservable;
    private final MutableLiveData<String> equipmentId;

    //private final MutableLiveData<String> groupCodeId;

    @Inject
    public DamageEntryViewModel(@NonNull ContractRepository contractRepository, @NonNull Application application) {
        super(application);

        this.equipmentId = new MutableLiveData<>();
        damageListObservable = Transformations.switchMap(equipmentId, input -> {
            if (input.isEmpty()) {
                return ABSENT;
            }

            return contractRepository.getDamageListByEquipment(equipmentId.getValue());
        });

        //damageMasterDataObservable = contractRepository.getDamageMasterData();
    }

    /**
     * Expose the LiveData Projects query so the UI can observe it.
     */
    public LiveData<DamageListResponse> getDamageListByEquipment() {
        return damageListObservable;
    }

//    public LiveData<DamageMasterDataResponse> getDamageMasterData() {
//        return damageMasterDataObservable;
//    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId.setValue(equipmentId);
    }

    public String convertDamageTimestampToDateTime(DamageItem item) {
        return DateUtils.convertTimestampToStringDateTime(item.damageInfo.damageDate);
    }
}
