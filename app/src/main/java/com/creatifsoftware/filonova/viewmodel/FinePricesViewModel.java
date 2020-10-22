package com.creatifsoftware.filonova.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.creatifsoftware.filonova.model.AdditionalProduct;
import com.creatifsoftware.filonova.model.ContractItem;
import com.creatifsoftware.filonova.model.User;
import com.creatifsoftware.filonova.model.request.CalculateDamagesAmountRequest;
import com.creatifsoftware.filonova.model.response.CalculateContractRemainingAmountResponse;
import com.creatifsoftware.filonova.model.response.CalculateDamagesAmountResponse;
import com.creatifsoftware.filonova.model.response.HgsTransitListResponse;
import com.creatifsoftware.filonova.model.response.TrafficPenaltyResponse;
import com.creatifsoftware.filonova.service.api.JsonApi;
import com.creatifsoftware.filonova.service.repository.ContractRepository;

import java.util.ArrayList;
import java.util.Locale;

import javax.inject.Inject;

public class FinePricesViewModel extends AndroidViewModel {
    private static final String TAG = FinePricesViewModel.class.getName();
    private static final MutableLiveData ABSENT = new MutableLiveData();

    private final LiveData<CalculateContractRemainingAmountResponse> finePriceObservable;
    private final LiveData<CalculateDamagesAmountResponse> damagesAmountObservable;
    private final LiveData<HgsTransitListResponse> hgstransitListObservable;
    private final LiveData<TrafficPenaltyResponse> trafficPenaltyListObservable;

    private final MutableLiveData<ContractItem> contractItem;
    private final MutableLiveData<JsonApi> api;
    private final MutableLiveData<CalculateDamagesAmountRequest> request;
    private final MutableLiveData<User> user;

    @Inject
    public FinePricesViewModel(@NonNull ContractRepository contractRepository, @NonNull Application application) {
        super(application);

        this.contractItem = new MutableLiveData<>();
        this.api = new MutableLiveData<>();
        this.request = new MutableLiveData<>();
        this.user = new MutableLiveData<>();
        finePriceObservable = Transformations.switchMap(contractItem, input -> {
            if (input.contractId == null) {
                return ABSENT;
            }


            return contractRepository.calculateContractRemainingAmountResponse(contractItem.getValue(), user.getValue());
        });

        damagesAmountObservable = Transformations.switchMap(contractItem, input -> {
            if (input.contractId == null) {
                return ABSENT;
            }


            return contractRepository.calculateDamagesAmount(request.getValue());
        });

        hgstransitListObservable = Transformations.switchMap(contractItem, input -> {
            if (input.contractId == null) {
                return ABSENT;
            }


            return contractRepository.getHgsTransitList(api.getValue(), contractItem.getValue());
        });

        trafficPenaltyListObservable = Transformations.switchMap(contractItem, input -> {
            if (input.contractId == null) {
                return ABSENT;
            }


            return contractRepository.getTrafficPenaltyList(api.getValue(), contractItem.getValue());
        });
    }

    /**
     * Expose the LiveData Projects query so the UI can observe it.
     */
    public LiveData<CalculateContractRemainingAmountResponse> getFinePriceObservable() {
        return finePriceObservable;
    }

    public LiveData<CalculateDamagesAmountResponse> getDamagesAmountObservable() {
        return damagesAmountObservable;
    }

    public LiveData<HgsTransitListResponse> getHgstransitListObservable() {
        return hgstransitListObservable;
    }

    public LiveData<TrafficPenaltyResponse> getTrafficPenaltyListObservable() {
        return trafficPenaltyListObservable;
    }

    public void setContractItem(ContractItem contractItem) {
        this.contractItem.setValue(contractItem);
    }

    public void setJsonApi(JsonApi api) {
        this.api.setValue(api);
    }

    public void setCalculateDamageRequest(CalculateDamagesAmountRequest request) {
        this.request.setValue(request);
    }

    public void setUser(User user) {
        this.user.setValue(user);
    }

    public String calculateTotalFineAmount() {
        CalculateContractRemainingAmountResponse finePriceResponse = finePriceObservable.getValue();
        double totalPrice = 0;
        if (finePriceResponse != null) {
            ArrayList<AdditionalProduct> listAll = new ArrayList<>(finePriceResponse.otherAdditionalProductData);

            // calculate extra payment
            for (AdditionalProduct item : listAll) {
                totalPrice = totalPrice + item.tobePaidAmount;
            }

            // calculate toll total price

            // calculate traffic fine price

            // If rental contract has car difference price, must be added
            totalPrice = totalPrice +
                    finePriceResponse.calculatePricesForUpdateContractResponse.amountobePaid +
                    finePriceResponse.additionalProductResponse.totaltobePaidAmount;
        }

        return String.format(Locale.getDefault(), "%.02f TL", totalPrice);
    }
}
