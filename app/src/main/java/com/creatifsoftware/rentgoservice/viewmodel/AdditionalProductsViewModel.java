package com.creatifsoftware.rentgoservice.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.creatifsoftware.rentgoservice.model.ContractItem;
import com.creatifsoftware.rentgoservice.model.response.AdditionalProductListResponse;
import com.creatifsoftware.rentgoservice.service.repository.ContractRepository;

import javax.inject.Inject;

public class AdditionalProductsViewModel extends AndroidViewModel {
    private static final String TAG = AdditionalProductsViewModel.class.getName();
    private static final MutableLiveData ABSENT = new MutableLiveData();

    //private final LiveData<AdditionalProductListResponse> additionalProductListObservable;
    private final ContractRepository contractRepository;
    private final MutableLiveData<ContractItem> contractItem;
    private final MutableLiveData<String> groupCodeId;

    @Inject
    public AdditionalProductsViewModel(@NonNull ContractRepository contractRepository, @NonNull Application application) {
        super(application);

        this.contractRepository = contractRepository;
        this.contractItem = new MutableLiveData<>();
        this.groupCodeId = new MutableLiveData<>();
//        additionalProductListObservable = Transformations.switchMap(contractItem, input -> {
//            if (input.contractId == null) {
//                return ABSENT;
//            }
//
//
//            return contractRepository.getAdditionalProductList(contractItem.getValue());
//        });
    }

    /**
     * Expose the LiveData Projects query so the UI can observe it.
     */
    public LiveData<AdditionalProductListResponse> getAdditionalProductListObservable() {
        //return additionalProductListObservable;
        return contractRepository.getAdditionalProductList(contractItem.getValue(), groupCodeId.getValue());
    }

    public void setContractItem(ContractItem contractItem) {
        this.contractItem.setValue(contractItem);
    }

    public void setGroupCodeId(String groupCodeId) {
        this.groupCodeId.setValue(groupCodeId);
    }
}
