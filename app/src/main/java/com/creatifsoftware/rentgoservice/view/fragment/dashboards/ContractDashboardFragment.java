package com.creatifsoftware.rentgoservice.view.fragment.dashboards;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.creatifsoftware.rentgoservice.BuildConfig;
import com.creatifsoftware.rentgoservice.R;
import com.creatifsoftware.rentgoservice.databinding.FragmentContractDashboardBinding;
import com.creatifsoftware.rentgoservice.di.Injectable;
import com.creatifsoftware.rentgoservice.model.ContractItem;
import com.creatifsoftware.rentgoservice.model.ReservationItem;
import com.creatifsoftware.rentgoservice.model.User;
import com.creatifsoftware.rentgoservice.model.request.GetContractInformationRequest;
import com.creatifsoftware.rentgoservice.model.response.CheckBeforeContractCreationResponse;
import com.creatifsoftware.rentgoservice.model.response.ContractListResponse;
import com.creatifsoftware.rentgoservice.model.response.ReservationListResponse;
import com.creatifsoftware.rentgoservice.utils.EnumUtils;
import com.creatifsoftware.rentgoservice.utils.LocaleUtils;
import com.creatifsoftware.rentgoservice.utils.RegexUtils;
import com.creatifsoftware.rentgoservice.utils.SharedPrefUtils;
import com.creatifsoftware.rentgoservice.view.activity.MainActivity;
import com.creatifsoftware.rentgoservice.view.adapter.DeliveryContractAdapter;
import com.creatifsoftware.rentgoservice.view.adapter.RentalContractAdapter;
import com.creatifsoftware.rentgoservice.view.adapter.ReservationListAdapter;
import com.creatifsoftware.rentgoservice.view.callback.ContractClickCallback;
import com.creatifsoftware.rentgoservice.view.callback.ReservationClickCallback;
import com.creatifsoftware.rentgoservice.view.fragment.ReservationInformationFragment;
import com.creatifsoftware.rentgoservice.view.fragment.base.BaseFragment;
import com.creatifsoftware.rentgoservice.view.fragment.contractInformation.ContractInformationForDeliveryFragment;
import com.creatifsoftware.rentgoservice.view.fragment.contractInformation.ContractInformationForRentalFragment;
import com.creatifsoftware.rentgoservice.viewmodel.ContractListViewModel;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

/**
 * Created by kerembalaban on 12.02.2019 at 22:23.
 */
public class ContractDashboardFragment extends BaseFragment implements Injectable {
    public static final String TAG = "ProjectListFragment";
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private DeliveryContractAdapter deliveryContractAdapter;
    private ReservationListAdapter reservationListAdapter;
    private RentalContractAdapter rentalContractAdapter;
    private FragmentContractDashboardBinding binding;
    private ContractListResponse contractListResponse;
    private ReservationListResponse reservationListResponse;
    private ContractListViewModel viewModel;
    private MainActivity mActivity;
    private final ContractClickCallback contractClickCallback = item -> {
        if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
            getContractInformation(item);
//            if (binding.rentalHeaderLayout.getVisibility() == View.GONE){
//                mActivity.prepareStepView(getDeliveryStepList());
//                ContractInformationForDeliveryFragment contractInformationFragment = ContractInformationForDeliveryFragment.forSelectedContract(item);
//                mActivity.show(contractInformationFragment);
//            }else{
//                mActivity.prepareStepView(getRentalStepList());
//                ContractInformationForRentalFragment contractInformationFragment = ContractInformationForRentalFragment.forSelectedContract(item);
//                mActivity.show(contractInformationFragment);
//            }
        }
    };
    private final ReservationClickCallback reservationClickCallback = item -> {
        if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
            viewModel.setReservationItem(item);
            mActivity.showLoading();
            viewModel.checkBeforeContractCreation().observe(getViewLifecycleOwner(), new Observer<CheckBeforeContractCreationResponse>() {
                @Override
                public void onChanged(CheckBeforeContractCreationResponse response) {
                    mActivity.hideLoading();
                    if (response == null) {
                        mActivity.showMessageDialog(getString(R.string.unknown_error_message));
                    } else if (response.responseResult.result) {
                        item.customer.paymentMethod = response.paymentMethod;
                        ReservationInformationFragment reservationInformationFragment = ReservationInformationFragment.forSelectedReservation(item);
                        mActivity.show(reservationInformationFragment);
                    } else {
                        mActivity.showMessageDialog(response.responseResult.exceptionDetail);
                    }
                }
            });
        }
    };
    private TabLayout tabLayout;

    @androidx.annotation.Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @androidx.annotation.Nullable ViewGroup container,
                             @androidx.annotation.Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_contract_dashboard, container, false);
        setDeliveryContractAdapter();
        setRentalContractAdapter();
        setReservationListAdapter();
        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.mActivity = (MainActivity) context;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mActivity.hideProgressBar();
    }

    @Override
    public void onActivityCreated(@androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        viewModel = ViewModelProviders.of(this,
                viewModelFactory).get(ContractListViewModel.class);

        observeDocumentList();
        prepareTabLayout();
        setupSwipeRefreshLayout();
        updateTabletIfNeeded();

        User user = new User();
        binding.setUser(user.getUser(getContext()));
        binding.setViewModel(viewModel);
        binding.deliveryContractSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (deliveryContractAdapter != null && contractListResponse != null)
                    deliveryContractAdapter.setContractList(filterContracts(charSequence.toString(), contractListResponse.deliveryContractList));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.rentalContractSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (rentalContractAdapter != null && contractListResponse != null)
                    rentalContractAdapter.setContractList(filterContracts(charSequence.toString(), contractListResponse.rentalContractList));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.quickContractSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (reservationListAdapter != null && reservationListResponse != null)
                    reservationListAdapter.setItemList(filterReservations(charSequence.toString(), reservationListResponse.reservationList));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.searchPlate.setOnClickListener(view -> mActivity.showPlateNumberDialog());
    }

    private void updateTabletIfNeeded() {

        String versionDetail = SharedPrefUtils.instance.getSavedObjectFromPreference(mActivity, "version_detail", String.class);
        if (versionDetail != null) {
            AlertDialog.Builder adb = new AlertDialog.Builder(mActivity);
            adb.setCancelable(false);
            adb.setPositiveButton(getString(R.string.new_version_download_button_title), (dialogInterface, i) -> downloadNewVersion());
            adb.setMessage(versionDetail);
            adb.setTitle(getString(R.string.new_version_alert_message));
            adb.show();
        }
    }

    private void downloadNewVersion() {
        SharedPrefUtils.instance.saveObject(mActivity, "version_detail", null);
        String url = "http://newmongodb.rentgo.com:6060";
        if (!BuildConfig.IS_PROD) {
            url = "http://newmongodb.rentgo.com:6060/internaltest";
        }
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    private void setupSwipeRefreshLayout() {
        binding.swipeContainer.setOnRefreshListener(
                this::observeDocumentList);

        binding.swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    public void searchContractByPlate(String plateNumber) {
        viewModel.setPlateNumber(plateNumber.toUpperCase().trim());
        mActivity.showLoading();
        viewModel.getContractByPlateNumber().observe(getViewLifecycleOwner(), response -> {
            mActivity.hideLoading();
            if (response.responseResult.result) {
                contractListResponse.rentalContractList.add(0, response.rentalContract);
                rentalContractAdapter.notifyDataSetChanged();
                setDashboardLayoutInformation();
            } else {
                mActivity.showMessageDialog(response.responseResult.exceptionDetail);
            }
        });
    }

    private List<ContractItem> filterContracts(String query, List<ContractItem> contractList) {
        if (TextUtils.isEmpty(query)) {
            return contractList;
        }

        query = query.toLowerCase(LocaleUtils.TR);
        Pattern pattern = Pattern.compile(RegexUtils.instance.createTurkishRegexFor(query));
        HashSet<ContractItem> filtered = new LinkedHashSet<>();
        for (ContractItem item : contractList) {
            Matcher plateNumber = null;
            Matcher contractNumber = pattern.matcher(item.contractNumber.toLowerCase(LocaleUtils.TR));
            Matcher customerName = pattern.matcher(item.customer.fullName.toLowerCase(LocaleUtils.TR));
            if (tabLayout.getSelectedTabPosition() == 1) {
                plateNumber = pattern.matcher(item.rentalEquipment.plateNumber.toLowerCase(LocaleUtils.TR));
            }
            if (contractNumber.find() || customerName.find() || (plateNumber != null && plateNumber.find())) {
                filtered.add(item);
            }
        }

        return new ArrayList<>(filtered);
    }

    private List<ReservationItem> filterReservations(String query, List<ReservationItem> reservationItems) {
        if (TextUtils.isEmpty(query)) {
            return reservationItems;
        }

        query = query.toLowerCase(LocaleUtils.TR);
        Pattern pattern = Pattern.compile(RegexUtils.instance.createTurkishRegexFor(query));
        HashSet<ReservationItem> filtered = new LinkedHashSet<>();
        for (ReservationItem item : reservationItems) {
            Matcher reservationNumber = pattern.matcher(item.reservationNumber.toLowerCase(LocaleUtils.TR));
            Matcher customerName = pattern.matcher(item.customer.fullName.toLowerCase(LocaleUtils.TR));
            if (reservationNumber.find() || customerName.find()) {
                filtered.add(item);
            }
        }

        return new ArrayList<>(filtered);
    }

    private void prepareTabLayout() {
        tabLayout = mActivity.findViewById(R.id.tabs);
        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    //setDeliveryContractAdapter();
                    binding.deliveryHeaderLayout.setVisibility(View.VISIBLE);
                    binding.rentalHeaderLayout.setVisibility(View.GONE);
                    binding.quickContractHeaderLayout.setVisibility(View.GONE);
                    if (contractListResponse != null) {
                        deliveryContractAdapter.setContractList(contractListResponse.deliveryContractList);
                        binding.contractList.setAdapter(deliveryContractAdapter);
                    }

                    tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(mActivity, R.color.dashboard_delivery_color));
                } else if (tab.getPosition() == 1) {
                    //setRentalContractAdapter();
                    binding.deliveryHeaderLayout.setVisibility(View.GONE);
                    binding.rentalHeaderLayout.setVisibility(View.VISIBLE);
                    binding.quickContractHeaderLayout.setVisibility(View.GONE);
                    if (contractListResponse != null) {
                        rentalContractAdapter.setContractList(contractListResponse.rentalContractList);
                        binding.contractList.setAdapter(rentalContractAdapter);
                    }
                    tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(mActivity, R.color.dashboard_rental_color));
                } else {
                    //setReservationListAdapter();
                    binding.deliveryHeaderLayout.setVisibility(View.GONE);
                    binding.rentalHeaderLayout.setVisibility(View.GONE);
                    binding.quickContractHeaderLayout.setVisibility(View.VISIBLE);
                    if (contractListResponse != null) {
                        reservationListAdapter.setItemList(reservationListResponse.reservationList);
                        binding.contractList.setAdapter(reservationListAdapter);
                    }
                    tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(mActivity, R.color.dashboard_fast_contract_color));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void observeDocumentList() {
        observeContractList();
        observeReservationList();
    }

    private void observeContractList() {
        viewModel.setBranchId(new User().getUser(getContext()).userBranch.branchId);
        viewModel.getContractListObservable().observe(this, response -> {
            if (binding.swipeContainer != null) {
                binding.swipeContainer.setRefreshing(false);
            }
            if (response == null) {
                mActivity.showMessageDialog(getString(R.string.unknown_error_message));
            } else if (response.responseResult.result) {
                contractListResponse = response;
                deliveryContractAdapter.setContractList(response.deliveryContractList);
                if (rentalContractAdapter != null && tabLayout.getSelectedTabPosition() == 1) {
                    rentalContractAdapter.setContractList(contractListResponse.rentalContractList);
                    binding.contractList.setAdapter(rentalContractAdapter);
                }

                if (deliveryContractAdapter != null && tabLayout.getSelectedTabPosition() == 0) {
                    deliveryContractAdapter.setContractList(contractListResponse.deliveryContractList);
                    binding.contractList.setAdapter(deliveryContractAdapter);
                }
                setDashboardLayoutInformation();
            } else {
                mActivity.showMessageDialog(response.responseResult.exceptionDetail);
            }
        });
    }

    private void observeReservationList() {
        viewModel.setBranchId(new User().getUser(getContext()).userBranch.branchId);
        viewModel.getReservationListObservable().observe(this, response -> {
            if (binding.swipeContainer != null) {
                binding.swipeContainer.setRefreshing(false);
            }
            if (response == null) {
                mActivity.showMessageDialog(getString(R.string.unknown_error_message));
            } else if (response.responseResult.result) {
                reservationListResponse = response;
                if (reservationListAdapter != null) {
                    reservationListAdapter.setItemList(reservationListResponse.reservationList);
                }
                setDashboardLayoutInformation();
            } else {
                mActivity.showMessageDialog(response.responseResult.exceptionDetail);
            }
        });
    }

    private void setDashboardLayoutInformation() {
        try {
            // Delivery Contract Dashbord Informations
            TextView totalDeliveryCount = tabLayout.getTabAt(0).getCustomView().findViewById(R.id.total_delivery_count);
            totalDeliveryCount.setText(viewModel.findDeliveryContractCount());
            TextView firstDeliveryTime = tabLayout.getTabAt(0).getCustomView().findViewById(R.id.first_delivery_time);
            firstDeliveryTime.setText(viewModel.findFirstDeliveryContractTime());

            // Rental Contract Dashbord Informations
            TextView totalRentalCount = tabLayout.getTabAt(1).getCustomView().findViewById(R.id.total_collect_count);
            totalRentalCount.setText(viewModel.findRentalContractCount());
            TextView firstRentalTime = tabLayout.getTabAt(1).getCustomView().findViewById(R.id.first_collect_time);
            firstRentalTime.setText(viewModel.findFirstRentalContractTime());

            TextView totalQuickContractCount = tabLayout.getTabAt(2).getCustomView().findViewById(R.id.total_quick_contract_count);
            totalQuickContractCount.setText(viewModel.findQuickContractCount());
            TextView firstQuickContractTime = tabLayout.getTabAt(2).getCustomView().findViewById(R.id.first_quick_contract_time);
            firstQuickContractTime.setText(viewModel.findQuickContractTime());
        } catch (Exception e) {
            //Toast.makeText(getContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
        }
    }

    private void setDeliveryContractAdapter() {
        deliveryContractAdapter = new DeliveryContractAdapter(contractClickCallback);
        //binding.contractList.invalidateItemDecorations();
        //binding.contractList.setAdapter(deliveryContractAdapter);
        binding.contractList.addItemDecoration(new DividerItemDecoration(binding.contractList.getContext(), DividerItemDecoration.VERTICAL));
    }

    private void setRentalContractAdapter() {
        rentalContractAdapter = new RentalContractAdapter(contractClickCallback);
        //binding.contractList.invalidateItemDecorations();
        //binding.contractList.setAdapter(rentalContractAdapter);
        binding.contractList.addItemDecoration(new DividerItemDecoration(binding.contractList.getContext(), DividerItemDecoration.VERTICAL));
    }

    private void setReservationListAdapter() {
        reservationListAdapter = new ReservationListAdapter(reservationClickCallback);
        //binding.contractList.invalidateItemDecorations();
        //binding.contractList.setAdapter(reservationListAdapter);
        binding.contractList.addItemDecoration(new DividerItemDecoration(binding.contractList.getContext(), DividerItemDecoration.VERTICAL));
    }

    private void showContractInformation(ContractItem item) {
        if (binding.rentalHeaderLayout.getVisibility() == View.GONE) {
            mActivity.prepareStepView(getDeliveryStepList());
            ContractInformationForDeliveryFragment contractInformationFragment = ContractInformationForDeliveryFragment.forSelectedContract(item);
            mActivity.show(contractInformationFragment);
        } else {
            mActivity.prepareStepView(getRentalStepList());
            ContractInformationForRentalFragment contractInformationFragment = ContractInformationForRentalFragment.forSelectedContract(item);
            mActivity.show(contractInformationFragment);
        }
    }

    private void getContractInformation(ContractItem item) {
        super.showLoading();
        GetContractInformationRequest request = new GetContractInformationRequest();
        request.setContractId(item.contractId);
        if (binding.rentalHeaderLayout.getVisibility() == View.GONE) {
            request.setStatusCode(EnumUtils.ContractStatusCode.WAITING_FOR_DELIVERY.getIntValue());
        } else {
            request.setStatusCode(EnumUtils.ContractStatusCode.RENTAL.getIntValue());
        }

        viewModel.getContractInformationObservable(request).observe(this, contractItem -> {
            if (contractItem == null) {
                super.hideLoading();
                showMessageDialog(getString(R.string.unknown_error_message));
            } else if (!contractItem.responseResult.result) {
                super.hideLoading();
                showMessageDialog(contractItem.responseResult.exceptionDetail);
            } else {
                super.hideLoading();
                selectedContract = contractItem;
                selectedContract.depositAmount = selectedContract.groupCodeInformation.depositAmount;
                showContractInformation(selectedContract);
            }
        });
    }

    private ArrayList<String> getDeliveryStepList() {
        ArrayList<String> steps = new ArrayList<>();
        steps.add(getString(R.string.contract_information));
        steps.add(getString(R.string.equipment_list_title));
        steps.add(getString(R.string.damage_entry_title));
        steps.add(getString(R.string.car_information_title));
        steps.add(getString(R.string.additional_products_title));
        steps.add(getString(R.string.additional_service_title));
        steps.add(getString(R.string.delivery_summary_title));

        return steps;
    }

    private ArrayList<String> getRentalStepList() {
        ArrayList<String> steps = new ArrayList<>();
        steps.add(getString(R.string.contract_information));
        steps.add(getString(R.string.damage_entry_title));
        steps.add(getString(R.string.car_information_title));
        steps.add(getString(R.string.extra_payment_title));
        steps.add(getString(R.string.rental_summary_title));

        return steps;
    }
}
