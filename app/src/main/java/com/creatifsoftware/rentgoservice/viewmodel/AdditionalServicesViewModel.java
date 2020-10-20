package com.creatifsoftware.rentgoservice.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.creatifsoftware.rentgoservice.service.repository.ContractRepository;

import javax.inject.Inject;

public class AdditionalServicesViewModel extends AndroidViewModel {
    private static final String TAG = AdditionalServicesViewModel.class.getName();
    private static final MutableLiveData ABSENT = new MutableLiveData();

    //private final LiveData<BaseResponse> updateContractObservable;
    //private final MutableLiveData<ContractItem> contractItem;

    @Inject
    public AdditionalServicesViewModel(@NonNull ContractRepository contractRepository, @NonNull Application application) {
        super(application);

    }

//    public LiveData<BaseResponse> getUpdateContractObservable() {
//        return updateContractObservable;
//    }
//
//    public void setContractItem(ContractItem contractItem){
//        this.contractItem.setValue(contractItem);
//    }
}
