package com.creatifsoftware.filonova.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.creatifsoftware.filonova.model.ContractItem;
import com.creatifsoftware.filonova.model.ReservationItem;
import com.creatifsoftware.filonova.model.request.GetContractInformationRequest;
import com.creatifsoftware.filonova.model.response.CheckBeforeContractCreationResponse;
import com.creatifsoftware.filonova.model.response.ContractListResponse;
import com.creatifsoftware.filonova.model.response.GetContractByEquipmentResponse;
import com.creatifsoftware.filonova.model.response.ReservationListResponse;
import com.creatifsoftware.filonova.service.repository.ContractRepository;
import com.creatifsoftware.filonova.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

public class ContractListViewModel extends AndroidViewModel {
    private static final String TAG = ContractInformationViewModel.class.getName();
    private static final MutableLiveData ABSENT = new MutableLiveData();

    private final LiveData<ContractListResponse> contractListObservable;
    private final LiveData<ReservationListResponse> reservationListObservable;
    private final MutableLiveData<String> plateNumber;
    private final MutableLiveData<ReservationItem> reservationItem;
    private final MutableLiveData<String> branchId;
    private final ContractRepository contractRepository;
    //private final LiveData<ContractItem> contractInformationObservable;
    //private final MutableLiveData<GetContractInformationRequest> getContractInformationRequest;

    public ObservableField<ContractItem> selectedContract = new ObservableField<>();

    @Inject
    public ContractListViewModel(@NonNull ContractRepository contractRepository, @NonNull Application application) {
        super(application);

        this.plateNumber = new MutableLiveData<>();
        this.reservationItem = new MutableLiveData<>();
        this.branchId = new MutableLiveData<>();
        this.contractRepository = contractRepository;

        contractListObservable = Transformations.switchMap(branchId, input -> {
            if (input.isEmpty()) {
                return ABSENT;
            }

            return contractRepository.getContractList(branchId.getValue());
        });

        reservationListObservable = Transformations.switchMap(branchId, input -> {
            if (input.isEmpty()) {
                return ABSENT;
            }

            return contractRepository.getReservationList(branchId.getValue());
        });
    }

    /**
     * Expose the LiveData Projects query so the UI can observe it.
     */
    public LiveData<ContractListResponse> getContractListObservable() {
        return contractListObservable;
    }

    public LiveData<ReservationListResponse> getReservationListObservable() {
        return reservationListObservable;
    }

    public LiveData<GetContractByEquipmentResponse> getContractByPlateNumber() {
        return contractRepository.getContractByPlateNumber(plateNumber.getValue());
    }

    public LiveData<CheckBeforeContractCreationResponse> checkBeforeContractCreation() {
        return contractRepository.checkBeforeContractCreation(reservationItem.getValue());
    }

    public void setReservationItem(ReservationItem reservationItem) {
        this.reservationItem.setValue(reservationItem);
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber.setValue(plateNumber);
    }

    public void setBranchId(String branchId) {
        this.branchId.setValue(branchId);
    }

    public LiveData<ContractItem> getContractInformationObservable(GetContractInformationRequest request) {
        return contractRepository.getContractInformation(request);
    }

    public String findDeliveryContractCount() {
        return String.valueOf(contractListObservable.getValue().deliveryContractList.size());
    }

    public String findRentalContractCount() {
        return String.valueOf(contractListObservable.getValue().rentalContractList.size());
    }

    public String findQuickContractCount() {
        return String.valueOf(reservationListObservable.getValue().reservationList.size());
    }

    public String findFirstDeliveryContractTime() {
        if (contractListObservable.getValue().deliveryContractList.size() > 0) {
            long timeStamp = contractListObservable.getValue().deliveryContractList.get(0).pickupTimestamp;
            return DateUtils.convertTimestampToStringTime(timeStamp);
        } else {
            return "- -";
        }
    }

    public String findFirstRentalContractTime() {
        if (contractListObservable.getValue().rentalContractList.size() > 0) {
            long timeStamp = contractListObservable.getValue().rentalContractList.get(0).dropoffTimestamp;
            return DateUtils.convertTimestampToStringTime(timeStamp);
        } else {
            return "- -";
        }
    }

    public String findQuickContractTime() {
        if (reservationListObservable.getValue().reservationList.size() > 0) {
            long timeStamp = reservationListObservable.getValue().reservationList.get(0).pickupTimestamp;
            return DateUtils.convertTimestampToStringTime(timeStamp);
        } else {
            return "- -";
        }
    }

    public String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        return mdformat.format(calendar.getTime());
    }

    public String getCurrentDate() {
        return DateUtils.getDateWithMonthYearName(new Date());
    }
}
