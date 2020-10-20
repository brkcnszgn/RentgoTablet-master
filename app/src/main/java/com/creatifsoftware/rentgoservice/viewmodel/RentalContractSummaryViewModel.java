package com.creatifsoftware.rentgoservice.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.creatifsoftware.rentgoservice.model.AdditionalProduct;
import com.creatifsoftware.rentgoservice.model.ContractItem;
import com.creatifsoftware.rentgoservice.model.User;
import com.creatifsoftware.rentgoservice.model.response.CreditCardsResponse;
import com.creatifsoftware.rentgoservice.model.response.UpdateContractForRentalResponse;
import com.creatifsoftware.rentgoservice.service.repository.ContractRepository;

import java.util.Locale;

import javax.inject.Inject;

public class RentalContractSummaryViewModel extends AndroidViewModel {
    private static final String TAG = RentalContractSummaryViewModel.class.getName();
    private static final MutableLiveData ABSENT = new MutableLiveData();

    private final MutableLiveData<ContractItem> contractItem;
    private final MutableLiveData<User> user;
    private final LiveData<CreditCardsResponse> creditCardsObservable;
    private final ContractRepository contractRepository;

    @Inject
    public RentalContractSummaryViewModel(@NonNull ContractRepository contractRepository, @NonNull Application application) {
        super(application);

        this.contractItem = new MutableLiveData<>();
        this.contractRepository = contractRepository;
        this.user = new MutableLiveData<>();

        creditCardsObservable = Transformations.switchMap(contractItem, input -> {
            if (input.contractId == null) {
                return ABSENT;
            }

            return contractRepository.getCustomerCreditCards(contractItem.getValue().customer.customerId);
        });
    }

    /**
     * Expose the LiveData Projects query so the UI can observe it.
     */
    public LiveData<UpdateContractForRentalResponse> getUpdateContractObservable() {
        return contractRepository.updateContractForRental(contractItem.getValue(), user.getValue());
    }

    public void setContractItem(ContractItem contractItem) {
        this.contractItem.setValue(contractItem);
    }

    public void setUser(User user) {
        this.user.setValue(user);
    }

    public LiveData<CreditCardsResponse> getCreditCardsObservable() {
        return creditCardsObservable;
    }

    public String findExtraPaymentListTotalAmount(ContractItem contractItem) {
        double totalPrice = 0;
        if (contractItem != null) {

            for (AdditionalProduct item : contractItem.extraPaymentList) {
                totalPrice = totalPrice + (item.tobePaidAmount * item.value);
            }

            // add document to shorten or extended price difference
            totalPrice = totalPrice + contractItem.carDifferenceAmount;
        }

        return String.format(Locale.getDefault(), "%.2f TL", totalPrice);
    }
}
