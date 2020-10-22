package com.creatifsoftware.filonova.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.creatifsoftware.filonova.model.ContractItem;
import com.creatifsoftware.filonova.model.response.TransferListResponse;
import com.creatifsoftware.filonova.service.repository.ContractRepository;
import com.creatifsoftware.filonova.utils.DateUtils;

import javax.inject.Inject;

public class TransferListViewModel extends AndroidViewModel {
    private static final String TAG = ContractInformationViewModel.class.getName();
    private static final MutableLiveData ABSENT = new MutableLiveData();

    private final LiveData<TransferListResponse> transferListObservable;
    private final MutableLiveData<String> branchId;

    public ObservableField<ContractItem> selectedContract = new ObservableField<>();

    @Inject
    public TransferListViewModel(@NonNull ContractRepository contractRepository, @NonNull Application application) {
        super(application);

        this.branchId = new MutableLiveData<>();

        transferListObservable = Transformations.switchMap(branchId, input -> {
            if (input.isEmpty()) {
                return ABSENT;
            }

            return contractRepository.getTransferList(branchId.getValue());
        });
    }

    /**
     * Expose the LiveData Projects query so the UI can observe it.
     */
    public LiveData<TransferListResponse> getTransferListObservable() {
        return transferListObservable;
    }

    public void setBranchId(String branchId) {
        this.branchId.setValue(branchId);
    }

    public String findDeliveryTransferCount() {
        return String.valueOf(transferListObservable.getValue().deliveryTransferList.size());
    }

    public String findReturnTransferCount() {
        return String.valueOf(transferListObservable.getValue().returnTransferList.size());
    }

    public String findFirstDeliveryTransferTime() {
        if (transferListObservable.getValue().deliveryTransferList.size() > 0) {
            long timeStamp = transferListObservable.getValue().deliveryTransferList.get(0).estimatedPickupTimestamp;
            return DateUtils.convertTimestampToStringTime(timeStamp);
        } else {
            return "- -";
        }
    }

    public String findFirstReturnTransferTime() {
        if (transferListObservable.getValue().returnTransferList.size() > 0) {
            long timeStamp = transferListObservable.getValue().returnTransferList.get(0).estimatedDropoffTimestamp;
            return DateUtils.convertTimestampToStringTime(timeStamp);
        } else {
            return "- -";
        }
    }
}
