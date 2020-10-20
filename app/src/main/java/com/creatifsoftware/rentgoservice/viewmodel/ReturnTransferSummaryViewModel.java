package com.creatifsoftware.rentgoservice.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.creatifsoftware.rentgoservice.model.TransferItem;
import com.creatifsoftware.rentgoservice.model.User;
import com.creatifsoftware.rentgoservice.model.base.BaseResponse;
import com.creatifsoftware.rentgoservice.service.repository.ContractRepository;

import javax.inject.Inject;

public class ReturnTransferSummaryViewModel extends AndroidViewModel {
    private static final String TAG = ReturnTransferSummaryViewModel.class.getName();
    private static final MutableLiveData ABSENT = new MutableLiveData();

    private final ContractRepository contractRepository;
    private final MutableLiveData<TransferItem> transferItem;
    private final MutableLiveData<User> user;

    @Inject
    public ReturnTransferSummaryViewModel(@NonNull ContractRepository contractRepository, @NonNull Application application) {
        super(application);

        this.contractRepository = contractRepository;
        this.transferItem = new MutableLiveData<>();
        this.user = new MutableLiveData<>();
    }

    /**
     * Expose the LiveData Projects query so the UI can observe it.
     */
    public LiveData<BaseResponse> getUpdateTransferObservable() {
        return contractRepository.updateTransferForReturn(transferItem.getValue(), user.getValue());
    }

    public void setTransferItem(TransferItem transferItem) {
        this.transferItem.setValue(transferItem);
    }

    public void setUser(User user) {
        this.user.setValue(user);
    }
}
