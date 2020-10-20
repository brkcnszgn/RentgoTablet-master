package com.creatifsoftware.rentgoservice.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.creatifsoftware.rentgoservice.model.ContractItem;
import com.creatifsoftware.rentgoservice.model.Equipment;
import com.creatifsoftware.rentgoservice.model.User;
import com.creatifsoftware.rentgoservice.model.base.BaseResponse;
import com.creatifsoftware.rentgoservice.model.response.CalculateAvailabilityResponse;
import com.creatifsoftware.rentgoservice.model.response.EquipmentListResponse;
import com.creatifsoftware.rentgoservice.service.repository.ContractRepository;

import javax.inject.Inject;

/**
 * Created by kerembalaban on 17.02.2019 at 17:01.
 */
public class EquipmentListViewModel extends AndroidViewModel {
    private static final String TAG = ContractInformationViewModel.class.getName();
    private static final MutableLiveData ABSENT = new MutableLiveData();
    private final LiveData<EquipmentListResponse> equipmentListObservable;
    private final LiveData<CalculateAvailabilityResponse> calculateAvailabilityObservable;
    private final MutableLiveData<String> groupCodeId;
    private final MutableLiveData<ContractItem> contractItem;
    private final MutableLiveData<User> user;
    private final ContractRepository contractRepository;

    @Inject
    public EquipmentListViewModel(@NonNull ContractRepository contractRepository, @NonNull Application application) {
        super(application);

        this.groupCodeId = new MutableLiveData<>();
        this.contractItem = new MutableLiveData<>();
        this.user = new MutableLiveData<>();
        this.contractRepository = contractRepository;

        equipmentListObservable = Transformations.switchMap(groupCodeId, input -> {
            if (input.isEmpty()) {
                Log.i(TAG, "ContractInformationViewModel contractNumber is absent!!!");
                return ABSENT;
            }

            Log.i(TAG, "ContractInformationViewModel contractNumber is " + groupCodeId.getValue());

            return contractRepository.getEquipmentList(groupCodeId.getValue(), user.getValue());
        });


        calculateAvailabilityObservable = Transformations.switchMap(contractItem, input -> {
            if (input.contractId == null) {
                return ABSENT;
            }

            return contractRepository.calculateAvailability(contractItem.getValue(), user.getValue());
        });
    }

    public LiveData<EquipmentListResponse> getEquipmentListObservable() {
        return equipmentListObservable;
    }

    public LiveData<CalculateAvailabilityResponse> getCalculateAvailabilityObservable() {
        return calculateAvailabilityObservable;
    }

    public void setContractItem(ContractItem contractItem) {
        this.contractItem.setValue(contractItem);
    }

    public LiveData<BaseResponse> getUpdateEquipmentStatusObservable(Equipment equipment, int statusCode) {
        return contractRepository.updateEquipmentStatus(equipment, statusCode);
    }

    public void setUser(User user) {
        this.user.setValue(user);
    }

    public void setGroupCodeId(String groupCodeId) {
        this.groupCodeId.setValue(groupCodeId);
    }
}
