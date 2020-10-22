package com.creatifsoftware.filonova.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.creatifsoftware.filonova.model.Equipment;
import com.creatifsoftware.filonova.model.base.BaseResponse;
import com.creatifsoftware.filonova.model.response.EquipmentListResponse;
import com.creatifsoftware.filonova.service.repository.ContractRepository;

import javax.inject.Inject;

public class CarsListViewModel extends AndroidViewModel {

    private static final String TAG = CarsListViewModel.class.getName();
    private static final MutableLiveData ABSENT = new MutableLiveData();

    private final ContractRepository contractRepository;
    private final LiveData<EquipmentListResponse> carsReportObservable;
    private final MutableLiveData<String> branchId;

    @Inject
    public CarsListViewModel(@NonNull ContractRepository contractRepository, @NonNull Application application) {
        super(application);


        this.contractRepository = contractRepository;
        this.branchId = new MutableLiveData<>();

        carsReportObservable = Transformations.switchMap(branchId, input -> {
            if (input.isEmpty()) {
                return ABSENT;
            }

            return contractRepository.getEquipmentsByBranch(branchId.getValue());
        });
    }

    public LiveData<EquipmentListResponse> getCarListObservable() {
        return carsReportObservable;
    }

    public LiveData<BaseResponse> getUpdateEquipmentStatusObservable(Equipment equipment, int statusCode) {
        return contractRepository.updateEquipmentStatus(equipment, statusCode);
    }

    public void setBranchId(String branchId) {
        this.branchId.setValue(branchId);
    }

}
