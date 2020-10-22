package com.creatifsoftware.filonova.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.creatifsoftware.filonova.model.ReservationItem;
import com.creatifsoftware.filonova.model.User;
import com.creatifsoftware.filonova.model.response.CreditCardsResponse;
import com.creatifsoftware.filonova.model.response.QuickContractResponse;
import com.creatifsoftware.filonova.service.repository.ContractRepository;

import javax.inject.Inject;

public class ReservationInformationViewModel extends AndroidViewModel {
    private static final String TAG = ReservationInformationViewModel.class.getName();
    private static final MutableLiveData ABSENT = new MutableLiveData();

    private final ContractRepository contractRepository;
    private final LiveData<CreditCardsResponse> creditCardsObservable;
    private final MutableLiveData<ReservationItem> reservationItem;
    private final MutableLiveData<String> customerId;
    private final MutableLiveData<User> user;

    @Inject
    public ReservationInformationViewModel(@NonNull ContractRepository contractRepository, @NonNull Application application) {
        super(application);

        this.contractRepository = contractRepository;
        this.user = new MutableLiveData<>();
        this.customerId = new MutableLiveData<>();
        this.reservationItem = new MutableLiveData<>();


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

    public LiveData<CreditCardsResponse> getCreditCardsObservable() {
        return creditCardsObservable;
    }

    public LiveData<QuickContractResponse> getCreateQuickContractObservable() {
        return contractRepository.createQuickContract(reservationItem.getValue(), user.getValue());
    }

    public void setCustomerId(String customerId) {
        this.customerId.setValue(customerId);
    }

    public void setReservationItem(ReservationItem reservationItem) {
        this.reservationItem.setValue(reservationItem);
    }

    public void setUser(User user) {
        this.user.setValue(user);
    }
}
