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

public class CreateTransferViewModel extends AndroidViewModel {
    private final MutableLiveData<TransferItem> transferItem;
    private final MutableLiveData<User> user;
    private final ContractRepository contractRepository;

    @Inject
    public CreateTransferViewModel(@NonNull ContractRepository contractRepository, @NonNull Application application) {
        super(application);

        this.transferItem = new MutableLiveData<>();
        this.user = new MutableLiveData<>();
        this.contractRepository = contractRepository;
    }

    /**
     * Expose the LiveData Projects query so the UI can observe it.
     */
    public LiveData<BaseResponse> getCreateTransferObservable() {
        return contractRepository.createTransfer(transferItem.getValue(), user.getValue());
    }

    public void setTransferItem(TransferItem transferItem) {
        this.transferItem.setValue(transferItem);
    }

    public void setUser(User user) {
        this.user.setValue(user);
    }
}
