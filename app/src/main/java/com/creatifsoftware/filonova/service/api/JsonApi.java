package com.creatifsoftware.filonova.service.api;

import com.creatifsoftware.filonova.model.ContractItem;
import com.creatifsoftware.filonova.model.User;
import com.creatifsoftware.filonova.model.base.BaseRequest;
import com.creatifsoftware.filonova.model.base.BaseResponse;
import com.creatifsoftware.filonova.model.request.CalculateAvailabilityRequest;
import com.creatifsoftware.filonova.model.request.CalculateContractRemainingAmountRequest;
import com.creatifsoftware.filonova.model.request.CalculateDamagesAmountRequest;
import com.creatifsoftware.filonova.model.request.CheckBeforeContractCreationRequest;
import com.creatifsoftware.filonova.model.request.CreateQuickContractRequest;
import com.creatifsoftware.filonova.model.request.CreateTransferRequest;
import com.creatifsoftware.filonova.model.request.GetAdditionalProductListRequest;
import com.creatifsoftware.filonova.model.request.GetContractByEquipmentRequest;
import com.creatifsoftware.filonova.model.request.GetContractInformationRequest;
import com.creatifsoftware.filonova.model.request.GetContractListRequest;
import com.creatifsoftware.filonova.model.request.GetCreditCardsRequest;
import com.creatifsoftware.filonova.model.request.GetDamageListRequest;
import com.creatifsoftware.filonova.model.request.GetEquipmentInventoryListRequest;
import com.creatifsoftware.filonova.model.request.GetEquipmentListByGroupCodeRequest;
import com.creatifsoftware.filonova.model.request.GetEquipmentsByBranchRequest;
import com.creatifsoftware.filonova.model.request.GetHgsAdditionalProductRequest;
import com.creatifsoftware.filonova.model.request.GetHgsTransitListRequest;
import com.creatifsoftware.filonova.model.request.GetReservationListRequest;
import com.creatifsoftware.filonova.model.request.GetTrafficPenaltyListRequest;
import com.creatifsoftware.filonova.model.request.GetTransferListRequest;
import com.creatifsoftware.filonova.model.request.UpdateContractForDeliveryRequest;
import com.creatifsoftware.filonova.model.request.UpdateContractForRentalRequest;
import com.creatifsoftware.filonova.model.request.UpdateEquipmentInformationRequest;
import com.creatifsoftware.filonova.model.request.UpdateTransferForDeliveryRequest;
import com.creatifsoftware.filonova.model.request.UpdateTransferForReturnRequest;
import com.creatifsoftware.filonova.model.request.UserRequest;
import com.creatifsoftware.filonova.model.response.AdditionalProductListResponse;
import com.creatifsoftware.filonova.model.response.CalculateAvailabilityResponse;
import com.creatifsoftware.filonova.model.response.CalculateContractRemainingAmountResponse;
import com.creatifsoftware.filonova.model.response.CalculateDamagesAmountResponse;
import com.creatifsoftware.filonova.model.response.CheckBeforeContractCreationResponse;
import com.creatifsoftware.filonova.model.response.ContractListResponse;
import com.creatifsoftware.filonova.model.response.CreditCardsResponse;
import com.creatifsoftware.filonova.model.response.DamageListResponse;
import com.creatifsoftware.filonova.model.response.DamageMasterDataResponse;
import com.creatifsoftware.filonova.model.response.EquipmentInventoryListResponse;
import com.creatifsoftware.filonova.model.response.EquipmentListResponse;
import com.creatifsoftware.filonova.model.response.GetContractByEquipmentResponse;
import com.creatifsoftware.filonova.model.response.GetHgsAdditionalProductsResponse;
import com.creatifsoftware.filonova.model.response.HgsTransitListResponse;
import com.creatifsoftware.filonova.model.response.MasterDataResponse;
import com.creatifsoftware.filonova.model.response.QuickContractResponse;
import com.creatifsoftware.filonova.model.response.ReservationListResponse;
import com.creatifsoftware.filonova.model.response.TrafficPenaltyResponse;
import com.creatifsoftware.filonova.model.response.TransferListResponse;
import com.creatifsoftware.filonova.model.response.UpdateContractForRentalResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface JsonApi {

    @POST("getContractsByBranch")
    Call<ContractListResponse> getContractListByBranch(@Body GetContractListRequest contractListRequest);

    @POST("getReservationsByBranch")
    Call<ReservationListResponse> getReservationList(@Body GetReservationListRequest request);

    @POST("getContractByEquipment")
    Call<GetContractByEquipmentResponse> getContractByEquipment(@Body GetContractByEquipmentRequest request);

    @POST("getContractDetails")
    Call<ContractItem> getSelectedContractInformation(@Body GetContractInformationRequest contractInformationRequest);

    @POST("getEquipmentsByGroupCode")
    Call<EquipmentListResponse> getEquipmentList(@Body GetEquipmentListByGroupCodeRequest equipmentListRequest);

    @POST("getEquipmentsByBranch")
    Call<EquipmentListResponse> getEquipmentsByBranch(@Body GetEquipmentsByBranchRequest request);

    @POST("calculateAvailability")
    Call<CalculateAvailabilityResponse> calculateAvailability(@Body CalculateAvailabilityRequest request);

    @POST("getDamagesByEquipment")
    Call<DamageListResponse> getDamageListByEquipment(@Body GetDamageListRequest damageListRequest);

    @GET("getDamageMasterData")
    Call<DamageMasterDataResponse> getDamageMasterData();

    @POST("getTabletMasterData")
    Call<MasterDataResponse> getMasterData(@Body BaseRequest request);

    @POST("getEquipmentInventories")
    Call<EquipmentInventoryListResponse> getEquipmentInventories(@Body GetEquipmentInventoryListRequest equipmentInventoryListRequest);

    @POST("getAdditionalproducts")
    Call<AdditionalProductListResponse> getAdditionalProductList(@Body GetAdditionalProductListRequest additionalProductListRequest);

    @POST("updateContractForDelivery")
    Call<BaseResponse> updateContractForDelivery(@Body UpdateContractForDeliveryRequest updateContractForDeliveryRequest);

    @POST("updateContractforRental")
    Call<UpdateContractForRentalResponse> updateContractForRental(@Body UpdateContractForRentalRequest updateContractForRentalRequest);

    @POST("getTransfersByBranch")
    Call<TransferListResponse> getTransferList(@Body GetTransferListRequest request);

    @POST("createTransfer")
    Call<BaseResponse> createTransferDocument(@Body CreateTransferRequest request);

    @POST("updateTransferForDelivery")
    Call<BaseResponse> updateTransferForDelivery(@Body UpdateTransferForDeliveryRequest request);

    @POST("updateTransferForReturn")
    Call<BaseResponse> updateTransferForReturn(@Body UpdateTransferForReturnRequest request);

    @POST("getCalculatedDamagesAmounts")
    Call<CalculateDamagesAmountResponse> calculateDamagesAmount(@Body CalculateDamagesAmountRequest request);

    @POST("getCreditCardsByCustomer")
    Call<CreditCardsResponse> getCreditCardsByCustomer(@Body GetCreditCardsRequest updateContractForRentalRequest);

    @POST("login")
    Call<User> login(@Body UserRequest userRequest);

    @POST("calculateContractRemainingAmount")
    Call<CalculateContractRemainingAmountResponse> calculateContractRemainingAmount(@Body CalculateContractRemainingAmountRequest request);

    @POST("updateEquipmentInformation")
    Call<BaseResponse> updateEquipmentInformation(@Body UpdateEquipmentInformationRequest request);

    @POST("updateEquipmentStatus")
    Call<BaseResponse> updateEquipmentStatus(@Body UpdateEquipmentInformationRequest request);

    @POST("createQuickContract")
    Call<QuickContractResponse> createQuickContract(@Body CreateQuickContractRequest request);

    @POST("checkBeforeContractCreation")
    Call<CheckBeforeContractCreationResponse> checkBeforeContractCreation(@Body CheckBeforeContractCreationRequest request);

    @POST("getHgsTransitList")
    Call<HgsTransitListResponse> getHgsTransitList(@Body GetHgsTransitListRequest request);

    @POST("getEihlalFineList")
    Call<TrafficPenaltyResponse> getTrafficPenaltyList(@Body GetTrafficPenaltyListRequest request);

    @POST("getHgsAdditionalProducts")
    Call<GetHgsAdditionalProductsResponse> getHgsAdditionalProducts(@Body GetHgsAdditionalProductRequest request);

//    @FormUrlEncoded
//    @POST("cancelInvoiceByLogoInvoiceNumber")
//    Call<LogoResponse> cancelInvoice(@Field("logoInvoiceNumber") String id);
}
