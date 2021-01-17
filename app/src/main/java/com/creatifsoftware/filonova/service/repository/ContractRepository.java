package com.creatifsoftware.filonova.service.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.creatifsoftware.filonova.model.AdditionalProduct;
import com.creatifsoftware.filonova.model.AvailabilityGroupCodeInformation;
import com.creatifsoftware.filonova.model.Branch;
import com.creatifsoftware.filonova.model.ContractItem;
import com.creatifsoftware.filonova.model.CreditCard;
import com.creatifsoftware.filonova.model.DamageItem;
import com.creatifsoftware.filonova.model.Equipment;
import com.creatifsoftware.filonova.model.ReservationItem;
import com.creatifsoftware.filonova.model.TransferItem;
import com.creatifsoftware.filonova.model.User;
import com.creatifsoftware.filonova.model.base.BaseRequest;
import com.creatifsoftware.filonova.model.base.BaseResponse;
import com.creatifsoftware.filonova.model.base.ResponseResult;
import com.creatifsoftware.filonova.model.request.CalculateAvailabilityRequest;
import com.creatifsoftware.filonova.model.request.CalculateContractRemainingAmountRequest;
import com.creatifsoftware.filonova.model.request.CalculateDamagesAmountRequest;
import com.creatifsoftware.filonova.model.request.CheckBeforeContractCreationRequest;
import com.creatifsoftware.filonova.model.request.ContractInformation;
import com.creatifsoftware.filonova.model.request.CreateQuickContractRequest;
import com.creatifsoftware.filonova.model.request.CreateTransferRequest;
import com.creatifsoftware.filonova.model.request.EquipmentInformation;
import com.creatifsoftware.filonova.model.request.GetAdditionalProductListRequest;
import com.creatifsoftware.filonova.model.request.GetContractByEquipmentRequest;
import com.creatifsoftware.filonova.model.request.GetContractInformationRequest;
import com.creatifsoftware.filonova.model.request.GetContractListRequest;
import com.creatifsoftware.filonova.model.request.GetCreditCardsRequest;
import com.creatifsoftware.filonova.model.request.GetDamageListRequest;
import com.creatifsoftware.filonova.model.request.GetEquipmentInventoryListRequest;
import com.creatifsoftware.filonova.model.request.GetEquipmentListByGroupCodeRequest;
import com.creatifsoftware.filonova.model.request.GetEquipmentsByBranchRequest;
import com.creatifsoftware.filonova.model.request.GetHgsTransitListRequest;
import com.creatifsoftware.filonova.model.request.GetReservationListRequest;
import com.creatifsoftware.filonova.model.request.GetTrafficPenaltyListRequest;
import com.creatifsoftware.filonova.model.request.GetTransferListRequest;
import com.creatifsoftware.filonova.model.request.GivenPath;
import com.creatifsoftware.filonova.model.request.PaymentInformation;
import com.creatifsoftware.filonova.model.request.QuickContractCustomerParameter;
import com.creatifsoftware.filonova.model.request.QuickContractPriceInformation;
import com.creatifsoftware.filonova.model.request.UpdateContractForDeliveryRequest;
import com.creatifsoftware.filonova.model.request.UpdateContractForRentalRequest;
import com.creatifsoftware.filonova.model.request.UpdateEquipmentInformationRequest;
import com.creatifsoftware.filonova.model.request.UpdateTransferForDeliveryRequest;
import com.creatifsoftware.filonova.model.request.UpdateTransferForReturnRequest;
import com.creatifsoftware.filonova.model.request.UserInformation;
import com.creatifsoftware.filonova.model.request.UserRequest;
import com.creatifsoftware.filonova.model.response.AdditionalProductListResponse;
import com.creatifsoftware.filonova.model.response.CalculateAvailabilityResponse;
import com.creatifsoftware.filonova.model.response.CalculateContractRemainingAmountResponse;
import com.creatifsoftware.filonova.model.response.CalculateDamagesAmountResponse;
import com.creatifsoftware.filonova.model.response.CheckBeforeContractCreationResponse;
import com.creatifsoftware.filonova.model.response.ContractListResponse;
import com.creatifsoftware.filonova.model.response.CreditCardsResponse;
import com.creatifsoftware.filonova.model.response.DamageListResponse;
import com.creatifsoftware.filonova.model.response.EquipmentInventoryListResponse;
import com.creatifsoftware.filonova.model.response.EquipmentListResponse;
import com.creatifsoftware.filonova.model.response.FinePriceResponse;
import com.creatifsoftware.filonova.model.response.GetContractByEquipmentResponse;
import com.creatifsoftware.filonova.model.response.HgsTransitListResponse;
import com.creatifsoftware.filonova.model.response.MasterDataResponse;
import com.creatifsoftware.filonova.model.response.QuickContractResponse;
import com.creatifsoftware.filonova.model.response.ReservationListResponse;
import com.creatifsoftware.filonova.model.response.TrafficPenaltyResponse;
import com.creatifsoftware.filonova.model.response.TransferListResponse;
import com.creatifsoftware.filonova.model.response.UpdateContractForRentalResponse;
import com.creatifsoftware.filonova.service.api.JsonApi;
import com.creatifsoftware.filonova.utils.EnumUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class ContractRepository {
    private final JsonApi jsonApi;

    @Inject
    public ContractRepository(JsonApi jsonApi) {
        this.jsonApi = jsonApi;
    }

    private static ContractListResponse convertJsonStringToContractJsonArray(String s) {
        return new Gson().fromJson(s, ContractListResponse.class);
    }

    private static DamageListResponse convertJsonStringToDamagesJsonArray(String s) {
        return new Gson().fromJson(s, DamageListResponse.class);
    }

    private static List<Equipment> convertJsonStringToEquipmentJsonArray(String s) {
        Type listType = new TypeToken<List<Equipment>>() {
        }.getType();
        return new Gson().fromJson(s, listType);
    }

    private static List<AdditionalProduct> convertJsonStringToAdditionalProductJsonArray(String s) {
        Type listType = new TypeToken<List<AdditionalProduct>>() {
        }.getType();
        return new Gson().fromJson(s, listType);
    }

    private static EquipmentListResponse convertJsonStringToCarsItemJsonArray(String s) {
        return new Gson().fromJson(s, EquipmentListResponse.class);
    }

    private static FinePriceResponse convertJsonStringToFinePriceJsonArray(String s) {
        return new Gson().fromJson(s, FinePriceResponse.class);
    }

    private static ContractItem convertJsonStringToJsonObject(String s) {
        return new Gson().fromJson(s, ContractItem.class);
    }

    public LiveData<List<String>> getImageList(GivenPath request) {
        final MutableLiveData<List<String>> data = new MutableLiveData<>();

        jsonApi.getBlobsByGivenPath(request).enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                data.setValue(new ArrayList<>());
            }
        });

        //data.setValue(convertJsonStringToContractJsonArray(MockData.contractListMockData()));

        return data;
    }

    public LiveData<User> login(UserRequest request) {
        final MutableLiveData<User> data = new MutableLiveData<>();

        jsonApi.login(request).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                User user = new User();
                user.responseResult = new ResponseResult(false, t.getLocalizedMessage());
                data.setValue(user);
            }
        });

        //data.setValue(convertJsonStringToContractJsonArray(MockData.contractListMockData()));

        return data;
    }

    public LiveData<ContractListResponse> getContractList(String branchId) {
        final MutableLiveData<ContractListResponse> data = new MutableLiveData<>();

        GetContractListRequest request = new GetContractListRequest();
        request.setBranchId(branchId);

        jsonApi.getContractListByBranch(request).enqueue(new Callback<ContractListResponse>() {
            @Override
            public void onResponse(Call<ContractListResponse> call, Response<ContractListResponse> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<ContractListResponse> call, Throwable t) {
                // TODO better errocr handling in part #2 ...
                ContractListResponse response = new ContractListResponse();
                response.responseResult = new ResponseResult(false, t.getLocalizedMessage());
                data.setValue(response);
            }
        });

        //data.setValue(convertJsonStringToContractJsonArray(MockData.contractListMockData()));

        return data;
    }

    public LiveData<ReservationListResponse> getReservationList(String branchId) {
        final MutableLiveData<ReservationListResponse> data = new MutableLiveData<>();

        GetReservationListRequest request = new GetReservationListRequest();
        request.setBranchId(branchId);

        jsonApi.getReservationList(request).enqueue(new Callback<ReservationListResponse>() {
            @Override
            public void onResponse(Call<ReservationListResponse> call, Response<ReservationListResponse> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<ReservationListResponse> call, Throwable t) {
                ReservationListResponse response = new ReservationListResponse();
                response.responseResult = new ResponseResult(false, t.getLocalizedMessage());
                data.setValue(response);
            }
        });

        //data.setValue(convertJsonStringToContractJsonArray(MockData.contractListMockData()));

        return data;
    }

    public LiveData<CreditCardsResponse> getCustomerCreditCards(String customerId) {
        final MutableLiveData<CreditCardsResponse> data = new MutableLiveData<>();

        GetCreditCardsRequest request = new GetCreditCardsRequest();
        request.setCustomerId(customerId);
        jsonApi.getCreditCardsByCustomer(request).enqueue(new Callback<CreditCardsResponse>() {
            @Override
            public void onResponse(Call<CreditCardsResponse> call, Response<CreditCardsResponse> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<CreditCardsResponse> call, Throwable t) {
                CreditCardsResponse response = new CreditCardsResponse();
                response.responseResult.result = false;
                response.responseResult = new ResponseResult(false, t.getLocalizedMessage());
                data.setValue(response);
            }
        });

        //data.setValue(convertJsonStringToContractJsonArray(MockData.contractListMockData()));

        return data;
    }

    public LiveData<EquipmentListResponse> getEquipmentsByBranch(String branchId) {
        final MutableLiveData<EquipmentListResponse> data = new MutableLiveData<>();

        GetEquipmentsByBranchRequest request = new GetEquipmentsByBranchRequest();
        request.setBranchId(branchId);

        jsonApi.getEquipmentsByBranch(request).enqueue(new Callback<EquipmentListResponse>() {
            @Override
            public void onResponse(Call<EquipmentListResponse> call, Response<EquipmentListResponse> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<EquipmentListResponse> call, Throwable t) {
                EquipmentListResponse response = new EquipmentListResponse();
                response.responseResult = new ResponseResult(false, t.getLocalizedMessage());
                data.setValue(response);
            }
        });

        //data.setValue(convertJsonStringToEquipmentJsonArray(MockData.equipmentListMockData()));

        return data;
    }

    public LiveData<DamageListResponse> getDamageListByEquipment(String equipmentId) {
        final MutableLiveData<DamageListResponse> data = new MutableLiveData<>();

        GetDamageListRequest request = new GetDamageListRequest();
        request.setEquipmentId(equipmentId);

        jsonApi.getDamageListByEquipment(request).enqueue(new Callback<DamageListResponse>() {
            @Override
            public void onResponse(Call<DamageListResponse> call, Response<DamageListResponse> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<DamageListResponse> call, Throwable t) {
                DamageListResponse response = new DamageListResponse();
                response.responseResult = new ResponseResult(false, t.getLocalizedMessage());
                data.setValue(response);
            }
        });

        //data.setValue(convertJsonStringToDamagesJsonArray(MockData.damageListMockData()));

        return data;
    }

    public LiveData<MasterDataResponse> getMasterData() {
        final MutableLiveData<MasterDataResponse> data = new MutableLiveData<>();
        BaseRequest request = new BaseRequest();
        jsonApi.getMasterData(request).enqueue(new Callback<MasterDataResponse>() {
            @Override
            public void onResponse(Call<MasterDataResponse> call, Response<MasterDataResponse> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<MasterDataResponse> call, Throwable t) {
                MasterDataResponse response = new MasterDataResponse();
                response.responseResult = new ResponseResult(false, t.getLocalizedMessage());
                data.setValue(response);
            }
        });

        return data;
    }

    public LiveData<GetContractByEquipmentResponse> getContractByPlateNumber(String plateNumber) {
        final MutableLiveData<GetContractByEquipmentResponse> data = new MutableLiveData<>();

        GetContractByEquipmentRequest request = new GetContractByEquipmentRequest();
        request.setPlateNumber(plateNumber);
        jsonApi.getContractByEquipment(request).enqueue(new Callback<GetContractByEquipmentResponse>() {
            @Override
            public void onResponse(Call<GetContractByEquipmentResponse> call, Response<GetContractByEquipmentResponse> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<GetContractByEquipmentResponse> call, Throwable t) {
                GetContractByEquipmentResponse response = new GetContractByEquipmentResponse();
                response.responseResult = new ResponseResult(false, t.getLocalizedMessage());
                data.setValue(response);
            }
        });

        return data;
    }

    public LiveData<EquipmentListResponse> getEquipmentList(String groupCodeId, User user) {
        final MutableLiveData<EquipmentListResponse> data = new MutableLiveData<>();

        GetEquipmentListByGroupCodeRequest request = new GetEquipmentListByGroupCodeRequest();
        request.setBranchId(user.userBranch.branchId);
        request.setGroupCodeId(groupCodeId);

        jsonApi.getEquipmentList(request).enqueue(new Callback<EquipmentListResponse>() {
            @Override
            public void onResponse(Call<EquipmentListResponse> call, Response<EquipmentListResponse> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<EquipmentListResponse> call, Throwable t) {
                EquipmentListResponse response = new EquipmentListResponse();
                response.responseResult = new ResponseResult(false, t.getLocalizedMessage());
                data.setValue(response);
            }
        });

        //data.setValue(convertJsonStringToEquipmentJsonArray(MockData.equipmentListMockData()));

        return data;
    }

    public LiveData<CalculateAvailabilityResponse> calculateAvailability(ContractItem contractItem, User user) {
        final MutableLiveData<CalculateAvailabilityResponse> data = new MutableLiveData<>();

        CalculateAvailabilityRequest request = new CalculateAvailabilityRequest();
        request.dropoffBranch = new Branch();
        request.contractInformation = new ContractInformation();

        request.dropoffBranch.branchId = user.userBranch.branchId;
        request.dropoffBranch.branchName = user.userBranch.branchName;

        request.contractInformation.contractId = contractItem.contractId;
        request.contractInformation.contactId = contractItem.customer.customerId;
        request.contractInformation.segment = contractItem.customer.segment;
        request.contractInformation.priceCodeId = contractItem.customer.priceCodeId;

        jsonApi.calculateAvailability(request).enqueue(new Callback<CalculateAvailabilityResponse>() {
            @Override
            public void onResponse(Call<CalculateAvailabilityResponse> call, Response<CalculateAvailabilityResponse> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<CalculateAvailabilityResponse> call, Throwable t) {
                CalculateAvailabilityResponse response = new CalculateAvailabilityResponse();
                response.responseResult = new ResponseResult(false, t.getLocalizedMessage());
                data.setValue(response);
            }
        });

        //data.setValue(convertJsonStringToEquipmentJsonArray(MockData.equipmentListMockData()));

        return data;
    }

    public LiveData<QuickContractResponse> createQuickContract(ReservationItem reservationItem, User user) {
        final MutableLiveData<QuickContractResponse> data = new MutableLiveData<>();

        CreateQuickContractRequest request = new CreateQuickContractRequest();
        request.customerInformation = new QuickContractCustomerParameter();
        request.priceInformation = new QuickContractPriceInformation();
        request.userInformation = new UserInformation();

        request.userInformation.userId = user.userId;
        request.customerInformation.contactId = reservationItem.customer.customerId;

        request.priceInformation.creditCardData = new ArrayList<>();
        if (reservationItem.customer.paymentMethod == EnumUtils.PaymentMethod.CURRENT.getIntValue() ||
                reservationItem.customer.paymentMethod == EnumUtils.PaymentMethod.FULL_CREDIT.getIntValue()) {
        } else {
            for (CreditCard item : reservationItem.customer.cardList) {
                if (item.isSelected) {
                    request.priceInformation.creditCardData.add(item);
                }
            }
        }

        request.reservationId = reservationItem.reservationId;
        request.reservationPNR = reservationItem.pnrNumber;

        jsonApi.createQuickContract(request).enqueue(new Callback<QuickContractResponse>() {
            @Override
            public void onResponse(Call<QuickContractResponse> call, Response<QuickContractResponse> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<QuickContractResponse> call, Throwable t) {
                QuickContractResponse response = new QuickContractResponse();
                response.responseResult = new ResponseResult(false, t.getLocalizedMessage());
                data.setValue(response);
            }
        });

        //data.setValue(convertJsonStringToEquipmentJsonArray(MockData.equipmentListMockData()));

        return data;
    }

    public LiveData<CheckBeforeContractCreationResponse> checkBeforeContractCreation(ReservationItem reservationItem) {
        final MutableLiveData<CheckBeforeContractCreationResponse> data = new MutableLiveData<>();
        CheckBeforeContractCreationRequest request = new CheckBeforeContractCreationRequest();

        request.reservationId = reservationItem.reservationId;
        request.contactId = reservationItem.customer.customerId;
        request.pickupDateTimeStamp = reservationItem.pickupTimestamp;
        request.isQuickContract = true;

        jsonApi.checkBeforeContractCreation(request).enqueue(new Callback<CheckBeforeContractCreationResponse>() {
            @Override
            public void onResponse(Call<CheckBeforeContractCreationResponse> call, Response<CheckBeforeContractCreationResponse> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<CheckBeforeContractCreationResponse> call, Throwable t) {
                CheckBeforeContractCreationResponse response = new CheckBeforeContractCreationResponse();
                response.responseResult = new ResponseResult(false, t.getLocalizedMessage());
                data.setValue(response);
            }
        });

        //data.setValue(convertJsonStringToEquipmentJsonArray(MockData.equipmentListMockData()));

        return data;
    }

    public LiveData<CalculateDamagesAmountResponse> calculateDamagesAmount(CalculateDamagesAmountRequest request) {
        final MutableLiveData<CalculateDamagesAmountResponse> data = new MutableLiveData<>();
        jsonApi.calculateDamagesAmount(request).enqueue(new Callback<CalculateDamagesAmountResponse>() {
            @Override
            public void onResponse(Call<CalculateDamagesAmountResponse> call, Response<CalculateDamagesAmountResponse> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<CalculateDamagesAmountResponse> call, Throwable t) {
                CalculateDamagesAmountResponse response = new CalculateDamagesAmountResponse();
                response.responseResult = new ResponseResult(false, t.getLocalizedMessage());
                data.setValue(response);
            }
        });

        //data.setValue(convertJsonStringToEquipmentJsonArray(MockData.equipmentListMockData()));

        return data;
    }

    public LiveData<AdditionalProductListResponse> getAdditionalProductList(ContractItem contractItem, String groupCodeId) {
        final MutableLiveData<AdditionalProductListResponse> data = new MutableLiveData<>();

        GetAdditionalProductListRequest request = new GetAdditionalProductListRequest();
        request.contractId = contractItem.contractId;
        request.groupCodeId = groupCodeId;
        request.customerId = contractItem.customer.customerId;

        jsonApi.getAdditionalProductList(request).enqueue(new Callback<AdditionalProductListResponse>() {
            @Override
            public void onResponse(Call<AdditionalProductListResponse> call, Response<AdditionalProductListResponse> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<AdditionalProductListResponse> call, Throwable t) {
                AdditionalProductListResponse response = new AdditionalProductListResponse();
                response.responseResult = new ResponseResult(false, t.getLocalizedMessage());
                data.setValue(response);
            }
        });

        //data.setValue(convertJsonStringToAdditionalProductJsonArray(MockData.additionalProductListMockData()));

        return data;
    }

    public LiveData<ContractItem> getContractInformation(GetContractInformationRequest request) {
        final MutableLiveData<ContractItem> data = new MutableLiveData<>();
        jsonApi.getSelectedContractInformation(request).enqueue(new Callback<ContractItem>() {
            @Override
            public void onResponse(Call<ContractItem> call, Response<ContractItem> response) {
                if (response.isSuccessful()) {
                    simulateDelay();
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<ContractItem> call, Throwable t) {
                ContractItem response = new ContractItem();
                response.responseResult = new ResponseResult(false, t.getLocalizedMessage());
                data.setValue(response);
            }
        });

        //data.setValue(convertJsonStringToJsonObject(MockData.contractInformationMockData()));

        return data;
    }

    public LiveData<EquipmentInventoryListResponse> getEquipmentInventoryList(String equipmentId) {
        final MutableLiveData<EquipmentInventoryListResponse> data = new MutableLiveData<>();

        GetEquipmentInventoryListRequest request = new GetEquipmentInventoryListRequest();
        request.setEquipmentId(equipmentId);

        jsonApi.getEquipmentInventories(request).enqueue(new Callback<EquipmentInventoryListResponse>() {
            @Override
            public void onResponse(Call<EquipmentInventoryListResponse> call, Response<EquipmentInventoryListResponse> response) {
                if (response.isSuccessful()) {
                    simulateDelay();
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<EquipmentInventoryListResponse> call, Throwable t) {
                EquipmentInventoryListResponse response = new EquipmentInventoryListResponse();
                response.responseResult = new ResponseResult(false, t.getLocalizedMessage());
                data.setValue(response);
            }
        });

        //data.setValue(convertJsonStringToJsonObject(MockData.contractInformationMockData()));

        return data;
    }

    public LiveData<CalculateContractRemainingAmountResponse> calculateContractRemainingAmountResponse(ContractItem contractItem, User user) {
        final MutableLiveData<CalculateContractRemainingAmountResponse> data = new MutableLiveData<>();

        CalculateContractRemainingAmountRequest request = new CalculateContractRemainingAmountRequest();
        request.dropoffBranch = new Branch();
        request.contractInformation = new ContractInformation();
        request.equipmentInformation = new EquipmentInformation();

        request.dropoffBranch.branchId = user.userBranch.branchId;
        request.dropoffBranch.branchName = user.userBranch.branchName;

        request.contractInformation.contractId = contractItem.contractId;
        request.contractInformation.contactId = contractItem.customer.customerId;
        request.contractInformation.segment = contractItem.customer.segment;
        request.contractInformation.manuelDropoffTimeStamp = contractItem.manuelDropoffTimeStamp;
        request.contractInformation.isManuelProcess = contractItem.isManuelProcess;
        request.contractInformation.priceCodeId = contractItem.customer.priceCodeId;

        request.equipmentInformation.equipmentId = contractItem.selectedEquipment.equipmentId;
        request.equipmentInformation.plateNumber = contractItem.selectedEquipment.plateNumber;
        request.equipmentInformation.currentKmValue = contractItem.selectedEquipment.currentKmValue;
        request.equipmentInformation.firstKmValue = contractItem.selectedEquipment.kmValue;
        request.equipmentInformation.currentFuelValue = contractItem.selectedEquipment.currentFuelValue;
        request.equipmentInformation.firstFuelValue = contractItem.selectedEquipment.fuelValue;
        request.equipmentInformation.equipmentInventoryData = contractItem.selectedEquipment.inventoryList;
        request.equipmentInformation.isEquipmentChanged = contractItem.isEquipmentChanged;

        //request.campaignId = contractItem.campaignId;

        jsonApi.calculateContractRemainingAmount(request).enqueue(new Callback<CalculateContractRemainingAmountResponse>() {
            @Override
            public void onResponse(Call<CalculateContractRemainingAmountResponse> call, Response<CalculateContractRemainingAmountResponse> response) {
                if (response.isSuccessful()) {
                    simulateDelay();
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<CalculateContractRemainingAmountResponse> call, Throwable t) {
                CalculateContractRemainingAmountResponse response = new CalculateContractRemainingAmountResponse();
                response.responseResult = new ResponseResult(false, t.getLocalizedMessage());
                data.setValue(response);
            }
        });

        return data;
    }

    public LiveData<BaseResponse> updateContractForDelivery(ContractItem contractItem, User user, ArrayList<CreditCard> cards) {
        final MutableLiveData<BaseResponse> data = new MutableLiveData<>();

        UpdateContractForDeliveryRequest request = new UpdateContractForDeliveryRequest();
        request.userInformation = new UserInformation();
        request.contractInformation = new ContractInformation();
        request.equipmentInformation = new EquipmentInformation();
        request.paymentInformation = new PaymentInformation();
        //request.changedEquipmentData = new AvailabilityGroupCodeInformation();
        request.paymentInformation.creditCardData = new ArrayList<>();
        request.additionalProducts = new ArrayList<>();
        request.damageData = new ArrayList<>();

        request.userInformation.userId = user.userId;

        request.contractInformation.contractId = contractItem.contractId;
        request.contractInformation.contactId = contractItem.customer.customerId;
        request.contractInformation.PickupDateTimeStamp = contractItem.pickupTimestamp;
        request.contractInformation.DropoffTimeStamp = contractItem.dropoffTimestamp;
        request.contractInformation.manuelPickupDateTimeStamp = contractItem.manuelPickupDateTimeStamp;
        request.contractInformation.isManuelProcess = contractItem.isManuelProcess;

        request.equipmentInformation.equipmentId = contractItem.selectedEquipment.equipmentId;
        request.equipmentInformation.plateNumber = contractItem.selectedEquipment.plateNumber;
        request.equipmentInformation.currentKmValue = contractItem.selectedEquipment.currentKmValue;
        request.equipmentInformation.firstKmValue = contractItem.selectedEquipment.kmValue;
        request.equipmentInformation.currentFuelValue = contractItem.selectedEquipment.currentFuelValue;
        request.equipmentInformation.firstFuelValue = contractItem.selectedEquipment.fuelValue;
        request.equipmentInformation.equipmentInventoryData = contractItem.selectedEquipment.inventoryList;
        request.equipmentInformation.groupCodeInformationId = contractItem.groupCodeInformation.groupCodeId;
        request.equipmentInformation.isEquipmentChanged = contractItem.isEquipmentChanged;

        request.campaignId = contractItem.campaignId;

        for (DamageItem damageItem : contractItem.selectedEquipment.damageList) {
            if (damageItem.damageInfo.isNewDamage) {
                request.damageData.add(damageItem);
            }
        }

        if (contractItem.updatedGroupCodeInformation != null) {
            request.changedEquipmentData = new AvailabilityGroupCodeInformation(contractItem.updatedGroupCodeInformation);
            if (contractItem.updatedGroupCodeInformation.changeType == EnumUtils.EquipmentChangeType.UPSELL.getIntValue() ||
                    contractItem.updatedGroupCodeInformation.changeType == EnumUtils.EquipmentChangeType.DOWNSELL.getIntValue()) {
                request.campaignId = null;
            }
        }

        if (contractItem.additionalProductList != null) {
            for (AdditionalProduct item : contractItem.additionalProductList) {
                if (item.isChecked) {
                    request.additionalProducts.add(item);
                }
            }
        }

        if (cards.size() > 0) {
            request.paymentInformation.creditCardData = cards;
        }

        jsonApi.updateContractForDelivery(request).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccessful()) {
                    simulateDelay();
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                BaseResponse response = new BaseResponse();
                response.responseResult = new ResponseResult(false, t.getLocalizedMessage());
                data.setValue(response);
            }
        });

        //data.setValue(convertJsonStringToJsonObject(MockData.contractInformationMockData()));

        return data;
    }

    public LiveData<UpdateContractForRentalResponse> updateContractForRental(ContractItem contractItem, User user) {
        final MutableLiveData<UpdateContractForRentalResponse> data = new MutableLiveData<>();

        UpdateContractForRentalRequest request = new UpdateContractForRentalRequest();
        request.userInformation = new UserInformation();
        request.contractInformation = new ContractInformation();
        request.equipmentInformation = new EquipmentInformation();
        request.paymentInformation = new PaymentInformation();
        request.paymentInformation.creditCardData = new ArrayList<>();
        request.additionalProducts = new ArrayList<>();
        request.otherAdditionalProducts = new ArrayList<>();
        request.damageData = new ArrayList<>();

        request.userInformation.userId = user.userId;
        request.userInformation.branchId = user.userBranch.branchId;

        request.contractInformation.contractId = contractItem.contractId;
        request.contractInformation.contactId = contractItem.customer.customerId;
        request.contractInformation.PickupDateTimeStamp = contractItem.pickupTimestamp;
        request.contractInformation.DropoffTimeStamp = contractItem.dropoffTimestamp;
        request.contractInformation.manuelDropoffTimeStamp = contractItem.manuelDropoffTimeStamp;
        request.contractInformation.isManuelProcess = contractItem.isManuelProcess;
        request.contractInformation.dropoffBranch = user.userBranch;

        request.equipmentInformation.equipmentId = contractItem.selectedEquipment.equipmentId;
        request.equipmentInformation.plateNumber = contractItem.selectedEquipment.plateNumber;
        request.equipmentInformation.currentKmValue = contractItem.selectedEquipment.currentKmValue;
        request.equipmentInformation.firstKmValue = contractItem.selectedEquipment.kmValue;
        request.equipmentInformation.currentFuelValue = contractItem.selectedEquipment.currentFuelValue;
        request.equipmentInformation.firstFuelValue = contractItem.selectedEquipment.fuelValue;
        request.equipmentInformation.equipmentInventoryData = contractItem.selectedEquipment.inventoryList;
        request.equipmentInformation.groupCodeInformationId = contractItem.groupCodeInformation.groupCodeId;
        request.equipmentInformation.isEquipmentChanged = contractItem.isEquipmentChanged;


        // if customer couldn't pay, complete contract with debt
        request.contractDeptStatus = contractItem.hasPaymentError ?
                EnumUtils.ContractDebtStatus.COMPLETE_WITH_DEBPT.getIntValue() :
                EnumUtils.ContractDebtStatus.COMPLETE_WITHOUT_DEBPT.getIntValue();

        request.campaignId = contractItem.campaignId;
        request.trackingNumber = contractItem.trackingNumber;
        request.operationType = contractItem.operationType;
        request.canUserStillHasCampaignBenefit = contractItem.canUserStillHasCampaignBenefit;
        request.dateNow = contractItem.dateNow;
        request.transits = contractItem.selectedEquipment.transits;
        request.fineList = contractItem.selectedEquipment.fineList;

        for (DamageItem damageItem : contractItem.selectedEquipment.damageList) {
            if (damageItem.damageInfo.isNewDamage) {
                request.damageData.add(damageItem);
            }
        }

        for (CreditCard selectedCreditCard : contractItem.customer.cardList) {
            if (selectedCreditCard.isSelected) {
                request.paymentInformation.creditCardData.add(selectedCreditCard);
            }
        }

        //add extrapaymentList
        if (contractItem.extraPaymentList != null) {
            for (AdditionalProduct item : contractItem.extraPaymentList) {
                if (item.value > 0) {
                    request.otherAdditionalProducts.add(item);
                }
            }
        }

        //update additionalprodcuts
        if (contractItem.additionalProductList != null) {
            for (AdditionalProduct item : contractItem.additionalProductList) {
                if (item.value > 0) {
                    request.additionalProducts.add(item);
                }
            }
        }

        jsonApi.updateContractForRental(request).enqueue(new Callback<UpdateContractForRentalResponse>() {
            @Override
            public void onResponse(Call<UpdateContractForRentalResponse> call, Response<UpdateContractForRentalResponse> response) {
                if (response.isSuccessful()) {
                    simulateDelay();
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<UpdateContractForRentalResponse> call, Throwable t) {
                UpdateContractForRentalResponse response = new UpdateContractForRentalResponse();
                response.responseResult = new ResponseResult(false, t.getLocalizedMessage());
                data.setValue(response);
            }
        });

        //data.setValue(convertJsonStringToJsonObject(MockData.contractInformationMockData()));

        return data;
    }

    public LiveData<BaseResponse> createTransfer(TransferItem transferItem, User user) {
        final MutableLiveData<BaseResponse> data = new MutableLiveData<>();

        CreateTransferRequest request = new CreateTransferRequest();
        request.userInformation = new UserInformation();
        request.equipmentInformation = new EquipmentInformation();

        request.userInformation.userId = user.userId;

        request.equipmentInformation.equipmentId = transferItem.selectedEquipment.equipmentId;
        request.equipmentInformation.groupCodeInformationId = transferItem.selectedEquipment.groupCodeId;

        request.pickupBranch = transferItem.pickupBranch;
        request.dropoffBranch = transferItem.dropoffBranch;
        request.estimatedPickupTimestamp = transferItem.estimatedPickupTimestamp;
        request.estimatedDropoffTimestamp = transferItem.estimatedDropoffTimestamp;
        request.transferType = transferItem.transferType;
        request.serviceName = transferItem.serviceName;
        request.description = transferItem.description;

        jsonApi.createTransferDocument(request).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccessful()) {
                    simulateDelay();
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                BaseResponse response = new BaseResponse();
                response.responseResult = new ResponseResult(false, t.getLocalizedMessage());
                data.setValue(response);
            }
        });

        //data.setValue(convertJsonStringToJsonObject(MockData.contractInformationMockData()));

        return data;
    }

    public LiveData<BaseResponse> updateTransferForDelivery(TransferItem transferItem, User user) {
        final MutableLiveData<BaseResponse> data = new MutableLiveData<>();

        UpdateTransferForDeliveryRequest request = new UpdateTransferForDeliveryRequest();
        request.userInformation = new UserInformation();
        request.equipmentInformation = new EquipmentInformation();
        request.damageList = new ArrayList<>();

        request.transferId = transferItem.transferId;

        request.userInformation.userId = user.userId;

        request.equipmentInformation.equipmentId = transferItem.selectedEquipment.equipmentId;
        request.equipmentInformation.plateNumber = transferItem.selectedEquipment.plateNumber;
        request.equipmentInformation.currentKmValue = transferItem.selectedEquipment.currentKmValue;
        request.equipmentInformation.firstKmValue = transferItem.selectedEquipment.kmValue;
        request.equipmentInformation.currentFuelValue = transferItem.selectedEquipment.currentFuelValue;
        request.equipmentInformation.firstFuelValue = transferItem.selectedEquipment.fuelValue;
        request.equipmentInformation.equipmentInventoryData = transferItem.selectedEquipment.inventoryList;

        for (DamageItem damageItem : transferItem.selectedEquipment.damageList) {
            if (damageItem.damageInfo.isNewDamage) {
                request.damageList.add(damageItem);
            }
        }

        jsonApi.updateTransferForDelivery(request).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccessful()) {
                    simulateDelay();
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                BaseResponse response = new BaseResponse();
                response.responseResult = new ResponseResult(false, t.getLocalizedMessage());
                data.setValue(response);
            }
        });

        //data.setValue(convertJsonStringToJsonObject(MockData.contractInformationMockData()));

        return data;
    }

    public LiveData<BaseResponse> updateTransferForReturn(TransferItem transferItem, User user) {
        final MutableLiveData<BaseResponse> data = new MutableLiveData<>();

        UpdateTransferForReturnRequest request = new UpdateTransferForReturnRequest();
        request.userInformation = new UserInformation();
        request.equipmentInformation = new EquipmentInformation();
        request.damageList = new ArrayList<>();

        request.transferId = transferItem.transferId;

        request.userInformation.userId = user.userId;
        request.userInformation.branchId = user.userBranch.branchId;

        request.equipmentInformation.equipmentId = transferItem.selectedEquipment.equipmentId;
        request.equipmentInformation.plateNumber = transferItem.selectedEquipment.plateNumber;
        request.equipmentInformation.currentKmValue = transferItem.selectedEquipment.currentKmValue;
        request.equipmentInformation.firstKmValue = transferItem.selectedEquipment.kmValue;
        request.equipmentInformation.currentFuelValue = transferItem.selectedEquipment.currentFuelValue;
        request.equipmentInformation.firstFuelValue = transferItem.selectedEquipment.fuelValue;
        request.equipmentInformation.equipmentInventoryData = transferItem.selectedEquipment.inventoryList;

        for (DamageItem damageItem : transferItem.selectedEquipment.damageList) {
            if (damageItem.damageInfo.isNewDamage) {
                request.damageList.add(damageItem);
            }
            if (damageItem.isRepaired) {
                request.damageList.add(damageItem);
            }
        }

        jsonApi.updateTransferForReturn(request).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccessful()) {
                    simulateDelay();
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                BaseResponse response = new BaseResponse();
                response.responseResult = new ResponseResult(false, t.getLocalizedMessage());
                data.setValue(response);
            }
        });

        //data.setValue(convertJsonStringToJsonObject(MockData.contractInformationMockData()));

        return data;
    }

    public LiveData<BaseResponse> updateEquipmentInformation(Equipment selectedEquipment, int statusCode) {
        final MutableLiveData<BaseResponse> data = new MutableLiveData<>();

        UpdateEquipmentInformationRequest request = new UpdateEquipmentInformationRequest();
        request.equipmentInformation = new EquipmentInformation();

        request.equipmentInformation.equipmentId = selectedEquipment.equipmentId;
        request.equipmentInformation.groupCodeInformationId = selectedEquipment.groupCodeId;
        request.equipmentInformation.plateNumber = selectedEquipment.plateNumber;
        request.equipmentInformation.currentKmValue = selectedEquipment.currentKmValue;
        request.equipmentInformation.firstKmValue = selectedEquipment.kmValue;
        request.equipmentInformation.currentFuelValue = selectedEquipment.currentFuelValue;
        request.equipmentInformation.firstFuelValue = selectedEquipment.fuelValue;
        request.equipmentInformation.equipmentInventoryData = selectedEquipment.inventoryList;

        request.statusCode = statusCode;

        jsonApi.updateEquipmentInformation(request).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccessful()) {
                    simulateDelay();
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                BaseResponse response = new BaseResponse();
                response.responseResult = new ResponseResult(false, t.getLocalizedMessage());
                data.setValue(response);
            }
        });

        return data;
    }

    public LiveData<BaseResponse> updateEquipmentStatus(Equipment selectedEquipment, int statusCode) {
        final MutableLiveData<BaseResponse> data = new MutableLiveData<>();

        UpdateEquipmentInformationRequest request = new UpdateEquipmentInformationRequest();
        request.equipmentInformation = new EquipmentInformation();

        request.equipmentInformation.equipmentId = selectedEquipment.equipmentId;
        request.statusCode = statusCode;

        jsonApi.updateEquipmentStatus(request).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccessful()) {
                    simulateDelay();
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                BaseResponse response = new BaseResponse();
                response.responseResult = new ResponseResult(false, t.getLocalizedMessage());
                data.setValue(response);
            }
        });

        return data;
    }

    public LiveData<TransferListResponse> getTransferList(String branchId) {
        final MutableLiveData<TransferListResponse> data = new MutableLiveData<>();

        GetTransferListRequest request = new GetTransferListRequest();
        request.setBranchId(branchId);

        jsonApi.getTransferList(request).enqueue(new Callback<TransferListResponse>() {
            @Override
            public void onResponse(Call<TransferListResponse> call, Response<TransferListResponse> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<TransferListResponse> call, Throwable t) {
                TransferListResponse response = new TransferListResponse();
                response.responseResult = new ResponseResult(false, t.getLocalizedMessage());
                data.setValue(response);
            }
        });

        //data.setValue(convertJsonStringToContractJsonArray(MockData.contractListMockData()));

        return data;
    }

    public LiveData<HgsTransitListResponse> getHgsTransitList(JsonApi api, ContractItem contractItem) {
        final MutableLiveData<HgsTransitListResponse> data = new MutableLiveData<>();

        GetHgsTransitListRequest request = new GetHgsTransitListRequest();
        request.productId = contractItem.selectedEquipment.hgsNumber;
        request.isManuelProcess = contractItem.isManuelProcess;
        if (contractItem.isManuelProcess) {
            request.startDateTimeStamp = contractItem.pickupTimestamp;
            request.finishDateTimeStamp = contractItem.manuelDropoffTimeStamp;
        } else {
            request.startDateTimeStamp = contractItem.pickupTimestamp;
            request.finishDateTimeStamp = contractItem.dropoffTimestamp;
        }
        api.getHgsTransitList(request).enqueue(new Callback<HgsTransitListResponse>() {
            @Override
            public void onResponse(Call<HgsTransitListResponse> call, Response<HgsTransitListResponse> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<HgsTransitListResponse> call, Throwable t) {
                HgsTransitListResponse response = new HgsTransitListResponse();
                response.responseResult = new ResponseResult(false, t.getLocalizedMessage());
                data.setValue(response);
            }
        });

        return data;
    }

    public LiveData<TrafficPenaltyResponse> getTrafficPenaltyList(JsonApi api, ContractItem contractItem) {
        final MutableLiveData<TrafficPenaltyResponse> data = new MutableLiveData<>();

        GetTrafficPenaltyListRequest request = new GetTrafficPenaltyListRequest();
        request.plateNumber = contractItem.selectedEquipment.plateNumber;
        request.isManuelProcess = contractItem.isManuelProcess;

        if (contractItem.isManuelProcess) {
            request.pickupDateTimeStamp = contractItem.pickupTimestamp;
            request.dropoffDatetimeStamp = contractItem.manuelDropoffTimeStamp;
        } else {
            request.pickupDateTimeStamp = contractItem.pickupTimestamp;
            request.dropoffDatetimeStamp = contractItem.dropoffTimestamp;
        }

        api.getTrafficPenaltyList(request).enqueue(new Callback<TrafficPenaltyResponse>() {
            @Override
            public void onResponse(Call<TrafficPenaltyResponse> call, Response<TrafficPenaltyResponse> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<TrafficPenaltyResponse> call, Throwable t) {
                TrafficPenaltyResponse response = new TrafficPenaltyResponse();
                response.responseResult = new ResponseResult(false, t.getLocalizedMessage());
                data.setValue(response);
            }
        });

        return data;
    }

    private void simulateDelay() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
