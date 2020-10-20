package com.creatifsoftware.rentgoservice.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.creatifsoftware.rentgoservice.BuildConfig;
import com.creatifsoftware.rentgoservice.R;
import com.creatifsoftware.rentgoservice.databinding.FragmentFinePriceBinding;
import com.creatifsoftware.rentgoservice.di.Injectable;
import com.creatifsoftware.rentgoservice.model.AdditionalProduct;
import com.creatifsoftware.rentgoservice.model.ContractItem;
import com.creatifsoftware.rentgoservice.model.DamageItem;
import com.creatifsoftware.rentgoservice.model.HgsItem;
import com.creatifsoftware.rentgoservice.model.User;
import com.creatifsoftware.rentgoservice.model.request.CalculateDamagesAmountRequest;
import com.creatifsoftware.rentgoservice.model.request.GetHgsAdditionalProductRequest;
import com.creatifsoftware.rentgoservice.model.response.CalculateContractRemainingAmountResponse;
import com.creatifsoftware.rentgoservice.model.response.CalculateDamagesAmountResponse;
import com.creatifsoftware.rentgoservice.model.response.CalculatePricesForUpdateContractResponse;
import com.creatifsoftware.rentgoservice.model.response.GetHgsAdditionalProductsResponse;
import com.creatifsoftware.rentgoservice.model.response.HgsTransitListResponse;
import com.creatifsoftware.rentgoservice.model.response.TrafficPenaltyResponse;
import com.creatifsoftware.rentgoservice.service.api.BasicAuthInterceptor;
import com.creatifsoftware.rentgoservice.service.api.ConnectivityInterceptor;
import com.creatifsoftware.rentgoservice.service.api.JsonApi;
import com.creatifsoftware.rentgoservice.view.adapter.ExpandableDamagePaymentListAdapter;
import com.creatifsoftware.rentgoservice.view.adapter.ExpandableExtraPaymentListAdapter;
import com.creatifsoftware.rentgoservice.view.adapter.ExpandableTollListAdapter;
import com.creatifsoftware.rentgoservice.view.adapter.ExpandableTrafficTicketListAdapter;
import com.creatifsoftware.rentgoservice.view.adapter.TotalFinePriceListAdapter;
import com.creatifsoftware.rentgoservice.view.fragment.base.BaseFragment;
import com.creatifsoftware.rentgoservice.view.fragment.contractSummary.ContractSummaryForRentalFragment;
import com.creatifsoftware.rentgoservice.viewmodel.FinePricesViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kerembalaban on 11.03.2019 at 15:20.
 */
public class FinePriceFragment extends BaseFragment implements Injectable {
    FragmentFinePriceBinding binding;
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private ExpandableTollListAdapter expandableTollListAdapter;
    private ExpandableTrafficTicketListAdapter expandableTrafficTicketListAdapter;
    private ExpandableExtraPaymentListAdapter expandableExtraPaymentListAdapter;
    private ExpandableDamagePaymentListAdapter expandableDamagePaymentListAdapter;
    private CalculateContractRemainingAmountResponse priceResponse;
    private HgsTransitListResponse hgsTransitListResponse;
    private GetHgsAdditionalProductsResponse hgsAdditionalProductsResponse;
    private TrafficPenaltyResponse trafficPenaltyResponse;
    private CalculateDamagesAmountResponse damageResponse;
    private FinePricesViewModel viewModel;
    private int counter = 0;

    /**
     * Creates project fragment for specific project ID
     */
    public static FinePriceFragment forSelectedContract(ContractItem selectedContract) {
        FinePriceFragment fragment = new FinePriceFragment();
        Bundle args = new Bundle();

        args.putSerializable(KEY_SELECTED_CONTRACT, selectedContract);
        fragment.setArguments(args);

        return fragment;
    }

    @androidx.annotation.Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @androidx.annotation.Nullable ViewGroup container,
                             @androidx.annotation.Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_fine_price, container, false);

        prepareCarDifferenceAmountLayout(null);
        prepareExpandableTollListAdapter();
        prepareExpandableExtraPaymentListAdapter();
        prepareExpandableTrafficTicketListAdapter();
        prepareExpandableDamagePaymentListAdapter();
        selectedContract.additionalProductList = new ArrayList<>();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getStepView().go(3, true);

        viewModel = ViewModelProviders.of(this,
                viewModelFactory).get(FinePricesViewModel.class);

        selectedContract = getSelectedContract();
        selectedContract.extraPaymentList = new ArrayList<>();
        counter = 0;
        observeFinePrices();
        observeTotalTollPrice();
        observeTotalTrafficTicketPrice();
        observeDamagesAmount();
        if (hgsAdditionalProductsResponse != null) {
            prepareContractExtraPaymentListForUpdate(hgsAdditionalProductsResponse.hgsAdditionalProductData);
        }

        binding.addExtraPaymentButton.setOnClickListener(view ->
                mActivity.showExtraPaymentDialog(priceResponse.otherCostAdditionalProductData));
    }

    /**
     * Prepare expandable adapters
     */
    private void prepareExpandableTollListAdapter() {
        expandableTollListAdapter = new ExpandableTollListAdapter(getContext(), hgsTransitListResponse == null ? "HGS Geçiş Toplamı" : hgsTransitListResponse.showErrorMessage ? hgsTransitListResponse.responseResult.exceptionDetail : "HGS Geçiş Toplamı");
        binding.tollAmountList.setAdapter(expandableTollListAdapter);
        binding.tollAmountList.setOnGroupExpandListener(i -> {
            binding.finePriceList.collapseGroup(0);
            binding.extraPaymentList.collapseGroup(0);
            binding.damagePaymentList.collapseGroup(0);
        });
    }

    private void prepareExpandableExtraPaymentListAdapter() {
        expandableExtraPaymentListAdapter = new ExpandableExtraPaymentListAdapter(getContext(), "Ek Bedeller");
        binding.extraPaymentList.setAdapter(expandableExtraPaymentListAdapter);
        binding.extraPaymentList.setOnGroupExpandListener(i -> {
            binding.finePriceList.collapseGroup(0);
            binding.tollAmountList.collapseGroup(0);
            binding.damagePaymentList.collapseGroup(0);
        });
    }

    private void prepareExpandableDamagePaymentListAdapter() {
        expandableDamagePaymentListAdapter = new ExpandableDamagePaymentListAdapter(getContext(), "Hasar Bedeli");
        binding.damagePaymentList.setAdapter(expandableDamagePaymentListAdapter);
        binding.damagePaymentList.setOnGroupExpandListener(i -> {
            binding.finePriceList.collapseGroup(0);
            binding.tollAmountList.collapseGroup(0);
            binding.extraPaymentList.collapseGroup(0);
        });
    }

    private void prepareExpandableTrafficTicketListAdapter() {
        expandableTrafficTicketListAdapter = new ExpandableTrafficTicketListAdapter(getContext(), "Trafik Ceza Toplamı (%25 erken ödeme indirim uygulanacaktır)");
        binding.finePriceList.setAdapter(expandableTrafficTicketListAdapter);
        binding.finePriceList.setOnGroupExpandListener(i -> {
            binding.extraPaymentList.collapseGroup(0);
            binding.tollAmountList.collapseGroup(0);
            binding.damagePaymentList.collapseGroup(0);
        });
    }

    /**
     * Call services
     */
    private void observeTotalTrafficTicketPrice() {
        JsonApi service = prepareJsonApiBuilder();
        if (viewModel.getTrafficPenaltyListObservable().hasObservers()) {
            bindTrafficPenaltyList();
            return;
        }
        viewModel.setContractItem(selectedContract);
        viewModel.setJsonApi(service);
        viewModel.getTrafficPenaltyListObservable().observe(this, response -> {
            if (response == null) {
                expandableTrafficTicketListAdapter.setDetailList(new ArrayList<>());
            } else if (response.responseResult.result) {
                trafficPenaltyResponse = response;
                selectedContract.selectedEquipment.fineList = response.fineList;
                bindTrafficPenaltyList();
            } else {
                expandableTrafficTicketListAdapter.setDetailList(new ArrayList<>());
            }
            counter++;
            hideLoading();
        });
    }

    private void observeFinePrices() {
        if (viewModel.getFinePriceObservable().hasObservers()) {
            bindFinePrice(viewModel);
            return;
        }
        showLoading();
        viewModel.setContractItem(selectedContract);
        User user = new User().getUser(getContext());
        viewModel.setUser(user);
        viewModel.getFinePriceObservable().observe(this, calculateContractRemainingAmountResponse -> {
            if (calculateContractRemainingAmountResponse == null) {
                super.hideLoading();
                showMessageDialog(getString(R.string.unknown_error_message));
            } else if (calculateContractRemainingAmountResponse.responseResult.result) {
                priceResponse = calculateContractRemainingAmountResponse;
                selectedContract.trackingNumber = priceResponse.calculatePricesForUpdateContractResponse.trackingNumber;
                selectedContract.dateNow = priceResponse.dateNow;
                selectedContract.operationType = priceResponse.calculatePricesForUpdateContractResponse.operationType;
                selectedContract.canUserStillHasCampaignBenefit = priceResponse.calculatePricesForUpdateContractResponse.canUserStillHasCampaignBenefit;
                selectedContract.additionalProductList = priceResponse.additionalProductResponse.additionalProducts;
                bindFinePrice(viewModel);
            } else {
                super.hideLoading();
                showMessageDialog(calculateContractRemainingAmountResponse.responseResult.exceptionDetail);
            }
            counter++;
            hideLoading();
        });
    }

    private void observeDamagesAmount() {
        if (viewModel.getDamagesAmountObservable().hasObservers()) {
            bindDamagePrice(viewModel);
            return;
        }

        CalculateDamagesAmountRequest request = new CalculateDamagesAmountRequest();
        request.contractId = selectedContract.contractId;
        request.damageList = new ArrayList<>();

        for (DamageItem item : selectedContract.selectedEquipment.damageList) {
            if (item.damageInfo.isNewDamage) {
                request.damageList.add(item);
            }
        }

        if (request.damageList.size() > 0) {
            viewModel.setCalculateDamageRequest(request);
            viewModel.getDamagesAmountObservable().observe(this, response -> {
                if (response == null) {
                    super.hideLoading();
                    showMessageDialog(getString(R.string.unknown_error_message));
                } else if (response.responseResult.result) {
                    damageResponse = response;
                    bindDamagePrice(viewModel);
                } else {
                    super.hideLoading();
                    showMessageDialog(response.responseResult.exceptionDetail);
                }
                counter++;
                hideLoading();
            });
        } else {
            counter++;
            hideLoading();
        }
    }

    private void observeTotalTollPrice() {
        JsonApi service = prepareJsonApiBuilder();
        if (viewModel.getHgstransitListObservable().hasObservers()) {
            bindTollList();
            return;
        }
        viewModel.setContractItem(selectedContract);
        viewModel.setJsonApi(service);
        viewModel.getHgstransitListObservable().observe(this, response -> {
            if (response == null) {
                expandableTollListAdapter.setDetailList(new ArrayList<>());
            } else if (response.responseResult.result) {
                hgsTransitListResponse = response;
                selectedContract.selectedEquipment.transits = response.transits;
                bindTollList();
            } else {
                expandableTollListAdapter = new ExpandableTollListAdapter(getContext(), response.showErrorMessage ? response.responseResult.exceptionDetail : "HGS Geçiş Toplamı");
                expandableTollListAdapter.setDetailList(new ArrayList<>());
                binding.tollAmountList.setAdapter(expandableTollListAdapter);
                expandableTollListAdapter.notifyDataSetChanged();
            }
            counter++;
            hideLoading();
        });
    }

    private void getHgsAdditionalProducts() {
        JsonApi service = prepareJsonApiBuilder();

        GetHgsAdditionalProductRequest request = new GetHgsAdditionalProductRequest();
        request.totalAmount = calculateHgsTransitListTotalAmount();
        request.contractId = selectedContract.contractId;

        service.getHgsAdditionalProducts(request).enqueue(new Callback<GetHgsAdditionalProductsResponse>() {
            @Override
            public void onResponse(Call<GetHgsAdditionalProductsResponse> call, Response<GetHgsAdditionalProductsResponse> response) {
                if (response.isSuccessful() && response.body().responseResult.result) {
                    hgsAdditionalProductsResponse = response.body();
                    prepareContractExtraPaymentListForUpdate(hgsAdditionalProductsResponse.hgsAdditionalProductData);
                    for (AdditionalProduct item : response.body().hgsAdditionalProductData) {
                        if (item.isServiceFee) {
                            priceResponse.otherAdditionalProductData.add(item);
                        }
                    }

                    hideLoadingImmediatly();
                }
            }

            @Override
            public void onFailure(Call<GetHgsAdditionalProductsResponse> call, Throwable t) {
                hideLoadingImmediatly();
            }
        });
    }

    /**
     * Data binding
     */
    private void bindFinePrice(FinePricesViewModel viewModel) {
        expandableExtraPaymentListAdapter.setDetailList(priceResponse.otherAdditionalProductData);
        expandableExtraPaymentListAdapter.notifyDataSetChanged();
        prepareContractExtraPaymentListForUpdate(priceResponse.otherAdditionalProductData);
        prepareCarDifferenceAmountLayout(priceResponse == null ? null : priceResponse.calculatePricesForUpdateContractResponse);
        calculateTotalFineAmount();
        binding.setViewModel(viewModel);
    }

    private void bindTollList() {
        if (hgsTransitListResponse != null) {
            expandableTollListAdapter.setDetailList(hgsTransitListResponse.transits);
            expandableTollListAdapter.notifyDataSetChanged();
            selectedContract.selectedEquipment.transits = hgsTransitListResponse.transits;
        }
    }

    private void bindTrafficPenaltyList() {
        if (trafficPenaltyResponse != null) {
            prepareContractExtraPaymentListForUpdate(trafficPenaltyResponse.fineAdditionalProducts);
            expandableTrafficTicketListAdapter.setDetailList(trafficPenaltyResponse.fineList);
            expandableTrafficTicketListAdapter.notifyDataSetChanged();
            selectedContract.selectedEquipment.fineList = trafficPenaltyResponse.fineList;
        }
    }

    private void bindDamagePrice(FinePricesViewModel viewModel) {
        expandableDamagePaymentListAdapter.setDetailList(damageResponse.damageList);
        expandableDamagePaymentListAdapter.notifyDataSetChanged();
        prepareContractExtraPaymentListForUpdate(damageResponse.damageProduct);
        calculateTotalFineAmount();
        binding.setViewModel(viewModel);
    }

    /**
     * Helpers
     */
    private double calculateHgsTransitListTotalAmount() {
        double total = 0;

        if (hgsTransitListResponse != null) {
            for (HgsItem item : hgsTransitListResponse.transits) {
                total += item.amount;
            }
        }

        return total;
    }

    private double calculateTrafficPenaltyListTotalAmount() {
        double total = 0;

        if (trafficPenaltyResponse != null) {
            for (AdditionalProduct item : trafficPenaltyResponse.fineAdditionalProducts) {
                if (!item.isServiceFee) {
                    total += item.tobePaidAmount;
                }
            }
        }

        return total;
    }

    private void calculateTotalFineAmount() {
        double totalPrice = 0;
        if (priceResponse != null) {
            ArrayList<AdditionalProduct> listAll = new ArrayList<>(priceResponse.otherAdditionalProductData);

            // calculate extra payment
            for (AdditionalProduct item : listAll) {
                totalPrice = totalPrice + item.tobePaidAmount;
            }

            // If rental contract has car difference price, must be added
            totalPrice = totalPrice +
                    priceResponse.calculatePricesForUpdateContractResponse.amountobePaid +
                    priceResponse.additionalProductResponse.totaltobePaidAmount;
        }

        // calculate toll total price
        totalPrice += calculateHgsTransitListTotalAmount();
        totalPrice += calculateTrafficPenaltyListTotalAmount();

        binding.totalAdditionalProductAmount.setText(String.format(Locale.getDefault(), "%.02f TL", totalPrice));
    }

    private void prepareCarDifferenceAmountLayout(CalculatePricesForUpdateContractResponse response) {
        double amountobePaid = response == null ? 0 : response.amountobePaid;
        double additionalProductAmountobePaid = response == null ? 0 : priceResponse.additionalProductResponse.totaltobePaidAmount;
        double totalAmount = amountobePaid + additionalProductAmountobePaid;

        if (totalAmount == 0) {
            binding.carDifferenceAmountTitle.setText("Geç/Erken Getirme Bedeli (Araç ve ek ürün toplamı)");
        } else if (totalAmount > 0) {
            binding.carDifferenceAmountTitle.setText("Geç Getirme Bedeli (Araç ve ek ürün toplamı)");
        } else {
            binding.carDifferenceAmountTitle.setText("Erken Getirme Bedeli (Araç ve ek ürün toplamı)");
        }

        selectedContract.carDifferenceAmount = totalAmount;
        binding.carDifferenceAmount.setText(String.format(Locale.getDefault(), "%.02f TL", totalAmount));
    }

    private void prepareContractExtraPaymentListForUpdate(List<AdditionalProduct> list) {
        if (list.size() > 0) {
            selectedContract.extraPaymentList.addAll(list);
        }
    }

    private void prepareContractExtraPaymentListForUpdate(AdditionalProduct item) {
        if (item != null) {
            selectedContract.extraPaymentList.add(item);
        }
    }

    private JsonApi prepareJsonApiBuilder() {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .addInterceptor((new BasicAuthInterceptor(BuildConfig.API_USERNAME, BuildConfig.API_PASSWORD)))
                .addInterceptor(new ConnectivityInterceptor())
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .cache(null)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.FINE_API_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(JsonApi.class);
    }

    public void extraPaymentAdded(AdditionalProduct item) {
        if (priceResponse == null) {
            priceResponse = new CalculateContractRemainingAmountResponse();
            priceResponse.otherAdditionalProductData = new ArrayList<>();
        }

        priceResponse.otherAdditionalProductData.add(item);

        expandableExtraPaymentListAdapter.setDetailList(priceResponse.otherAdditionalProductData);
        expandableExtraPaymentListAdapter.notifyDataSetChanged();
        prepareCarDifferenceAmountLayout(priceResponse == null ? null : priceResponse.calculatePricesForUpdateContractResponse);
        calculateTotalFineAmount();
        binding.setViewModel(viewModel);
        prepareContractExtraPaymentListForUpdate(item);
    }

    /**
     * Overrides
     */
    @Override
    protected void hideLoading() {
        if (counter > 3) {
            if (selectedContract.selectedEquipment.transits.size() > 0 && priceResponse != null) {
                getHgsAdditionalProducts();
            } else {
                hideLoadingImmediatly();
            }
        }
    }

    private void hideLoadingImmediatly() {
        if (trafficPenaltyResponse != null && priceResponse != null) {
            for (AdditionalProduct item : trafficPenaltyResponse.fineAdditionalProducts) {
                if (item.isServiceFee) {
                    priceResponse.otherAdditionalProductData.add(item);
                }
            }

            expandableExtraPaymentListAdapter.setDetailList(priceResponse.otherAdditionalProductData);
        }

        expandableExtraPaymentListAdapter.notifyDataSetChanged();
        calculateTotalFineAmount();
        super.hideLoading();
    }

    private void prepareAllPricesList() {
        ArrayList<AdditionalProduct> listAll = new ArrayList<>(priceResponse.otherAdditionalProductData);
        listAll.add(damageResponse.damageProduct);

        TotalFinePriceListAdapter totalFinePriceListAdapter = new TotalFinePriceListAdapter();
        totalFinePriceListAdapter.setTotalFineList(listAll);
        binding.totalList.setAdapter(totalFinePriceListAdapter);
    }

    @Override
    public void navigate() {
        List<AdditionalProduct> tempList = new ArrayList<>();

        for (AdditionalProduct item : selectedContract.extraPaymentList) {
            boolean hasValue = false;
            for (AdditionalProduct item2 : tempList) {
                if (item2.productCode == item.productCode) {
                    hasValue = true;
                }
            }

            if (!hasValue)
                tempList.add(item);
        }

        selectedContract.extraPaymentList = tempList;

        ContractSummaryForRentalFragment contractSummaryFragment = ContractSummaryForRentalFragment.forSelectedContract(selectedContract);
        changeFragment(contractSummaryFragment);
    }

    @Override
    public void messageDialogDoneButtonClicked() {
        //backButtonClicked();
        mActivity.onBackPressed();
        //mActivity.getSupportFragmentManager().popBackStackImmediate();
    }
}
