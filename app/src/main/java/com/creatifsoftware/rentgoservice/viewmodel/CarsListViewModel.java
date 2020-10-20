package com.creatifsoftware.rentgoservice.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.creatifsoftware.rentgoservice.model.Equipment;
import com.creatifsoftware.rentgoservice.model.base.BaseResponse;
import com.creatifsoftware.rentgoservice.model.response.EquipmentListResponse;
import com.creatifsoftware.rentgoservice.service.repository.ContractRepository;

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
