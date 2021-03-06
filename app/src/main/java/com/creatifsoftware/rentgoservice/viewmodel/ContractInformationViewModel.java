package com.creatifsoftware.rentgoservice.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.creatifsoftware.rentgoservice.model.ContractItem;
import com.creatifsoftware.rentgoservice.model.request.GetContractInformationRequest;
import com.creatifsoftware.rentgoservice.service.repository.ContractRepository;

import javax.inject.Inject;

public class ContractInformationViewModel extends AndroidViewModel {
    private static final String TAG = ContractInformationViewModel.class.getName();
    private static final MutableLiveData ABSENT = new MutableLiveData();

    private final ObservableField<ContractItem> selectedContract = new ObservableField<>();
    //private final ContractRepository contractRepository;
    private final LiveData<ContractItem> contractInformationObservable;
    private final MutableLiveData<GetContractInformationRequest> getContractInformationRequest;

    @Inject
    public ContractInformationViewModel(@NonNull ContractRepository contractRepository, @NonNull Application application) {
        super(application);

        this.getContractInformationRequest = new MutableLiveData<>();
        contractInformationObservable = Transformations.switchMap(getContractInformationRequest, input -> {
            if (input.contractId.isEmpty()) {
                return ABSENT;
            }

            return contractRepository.getContractInformation(input);
        });
    }

    /**
     * Expose the LiveData Projects query so the UI can observe it.
     */
    public void setContractInformation(ContractItem contractItem) {
        this.selectedContract.set(contractItem);
    }

    public LiveData<ContractItem> getContractInformationObservable() {
        return contractInformationObservable;
        //return contractRepository.getContractInformation(getContractInformationRequest.getValue());
    }

    public void setGetContractInformationRequest(GetContractInformationRequest request) {
        //this.contractInformationObservable = new MutableLiveData<>();
        this.getContractInformationRequest.setValue(request);
    }
}
