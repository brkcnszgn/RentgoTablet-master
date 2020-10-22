package com.creatifsoftware.filonova.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.creatifsoftware.filonova.model.ContractItem;
import com.creatifsoftware.filonova.model.CreditCard;
import com.creatifsoftware.filonova.model.User;
import com.creatifsoftware.filonova.model.base.BaseResponse;
import com.creatifsoftware.filonova.model.response.CreditCardsResponse;
import com.creatifsoftware.filonova.model.response.UpdateContractForRentalResponse;
import com.creatifsoftware.filonova.service.repository.ContractRepository;

import java.util.ArrayList;

import javax.inject.Inject;

public class ContractSummaryViewModel extends AndroidViewModel {
    private static final String TAG = ContractSummaryViewModel.class.getName();
    private static final MutableLiveData ABSENT = new MutableLiveData();

    private final ContractRepository contractRepository;
    private final LiveData<CreditCardsResponse> creditCardsObservable;
    private final MutableLiveData<ContractItem> contractItem;
    private final MutableLiveData<String> customerId;
    private final MutableLiveData<User> user;

    @Inject
    public ContractSummaryViewModel(@NonNull ContractRepository contractRepository, @NonNull Application application) {
        super(application);

        this.contractRepository = contractRepository;
        this.contractItem = new MutableLiveData<>();
        this.customerId = new MutableLiveData<>();
        this.user = new MutableLiveData<>();


        creditCardsObservable = Transformations.switchMap(customerId, input -> {
            if (input == null) {
                return ABSENT;
            }

            return contractRepository.getCustomerCreditCards(customerId.getValue());
        });
    }

    /**
     * Expose the LiveData Projects query so the UI can observe it.
     */
    public LiveData<BaseResponse> getUpdateContractForDeliveryObservable(ArrayList<CreditCard> cards) {
        return contractRepository.updateContractForDelivery(contractItem.getValue(), user.getValue(), cards);
    }

    public LiveData<UpdateContractForRentalResponse> getUpdateContractForRentalObservable() {
        return contractRepository.updateContractForRental(contractItem.getValue(), user.getValue());
    }

    public LiveData<CreditCardsResponse> getCreditCardsObservable() {
        return creditCardsObservable;
    }

    public void setContractItem(ContractItem contractItem) {
        this.contractItem.setValue(contractItem);
    }

    public void setCustomerId(String customerId) {
        this.customerId.setValue(customerId);
    }

    public void setUser(User user) {
        this.user.setValue(user);
    }
}
