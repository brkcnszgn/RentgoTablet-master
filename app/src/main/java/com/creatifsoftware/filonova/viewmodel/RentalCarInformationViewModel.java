package com.creatifsoftware.filonova.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.creatifsoftware.filonova.model.response.EquipmentInventoryListResponse;
import com.creatifsoftware.filonova.service.repository.ContractRepository;

import javax.inject.Inject;

public class RentalCarInformationViewModel extends AndroidViewModel {
    private static final String TAG = RentalCarInformationViewModel.class.getName();
    private static final MutableLiveData ABSENT = new MutableLiveData();

    private final LiveData<EquipmentInventoryListResponse> equipmentInventoryListObservable;
    private final MutableLiveData<String> equipmentId;

    @Inject
    public RentalCarInformationViewModel(@NonNull ContractRepository contractRepository, @NonNull Application application) {
        super(application);

        this.equipmentId = new MutableLiveData<>();
        equipmentInventoryListObservable = Transformations.switchMap(equipmentId, input -> {
            if (input.isEmpty()) {
                Log.i(TAG, "ContractInformationViewModel contractNumber is absent!!!");
                return ABSENT;
            }

            Log.i(TAG, "ContractInformationViewModel contractNumber is " + equipmentId.getValue());

            return contractRepository.getEquipmentInventoryList(equipmentId.getValue());
        });
    }

    /**
     * Expose the LiveData Projects query so the UI can observe it.
     */
    public LiveData<EquipmentInventoryListResponse> getEquipmentInventoryListObservable() {
        return equipmentInventoryListObservable;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId.setValue(equipmentId);
    }
}
