package com.creatifsoftware.filonova.view.fragment.dashboards;

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
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.creatifsoftware.filonova.R;
import com.creatifsoftware.filonova.databinding.FragmentTransferDashboardBinding;
import com.creatifsoftware.filonova.di.Injectable;
import com.creatifsoftware.filonova.model.TransferItem;
import com.creatifsoftware.filonova.model.User;
import com.creatifsoftware.filonova.model.response.TransferListResponse;
import com.creatifsoftware.filonova.utils.CommonMethods;
import com.creatifsoftware.filonova.utils.EnumUtils;
import com.creatifsoftware.filonova.utils.LocaleUtils;
import com.creatifsoftware.filonova.utils.RegexUtils;
import com.creatifsoftware.filonova.utils.SharedPrefUtils;
import com.creatifsoftware.filonova.view.activity.MainActivity;
import com.creatifsoftware.filonova.view.adapter.TransferListAdapter;
import com.creatifsoftware.filonova.view.callback.TransferItemClickCallback;
import com.creatifsoftware.filonova.view.callback.TransferTypeClickCallback;
import com.creatifsoftware.filonova.view.fragment.base.BaseFragment;
import com.creatifsoftware.filonova.view.fragment.transfers.EquipmentListForTransferFragment;
import com.creatifsoftware.filonova.view.fragment.transfers.TransferInformationFragment;
import com.creatifsoftware.filonova.viewmodel.TransferListViewModel;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

public class TransferDashboardFragment extends BaseFragment implements Injectable, AdapterView.OnItemSelectedListener {
    private static final int PICKUP_TRANSFER_TAB = 0;
    private static final int DROPOFF_TRANSFER_TAB = 1;
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private FragmentTransferDashboardBinding binding;
    private MainActivity mActivity;
    private final TransferTypeClickCallback transferTypeClickCallback = view -> {
        if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
            EnumUtils.TransferType transferType = EnumUtils.TransferType.NONE;
            switch (view.getId()) {
                case R.id.damage_transfer_layout:
                    transferType = EnumUtils.TransferType.DAMAGE;
                    break;
                case R.id.fault_transfer_layout:
                    transferType = EnumUtils.TransferType.FAULT;
                    break;
                case R.id.service_transfer_layout:
                    transferType = EnumUtils.TransferType.SERVICE;
                    break;
                case R.id.free_transfer_layout:
                    transferType = EnumUtils.TransferType.FREE;
                    break;
                case R.id.second_hand_transfer_layout:
                    transferType = EnumUtils.TransferType.SECOND_HAND;
                    break;
                case R.id.branch_transfer_layout:
                    transferType = EnumUtils.TransferType.BRANCH;
                    break;
            }

            TransferItem transferItem = new TransferItem();
            transferItem.transferType = transferType.getIntValue();
            transferItem.pickupBranch = new User().getUser(getContext()).userBranch;
            transferItem.dropoffBranch = new User().getUser(getContext()).userBranch;

            EquipmentListForTransferFragment equipmentListForTransferFragment = EquipmentListForTransferFragment.withTransferItem(transferItem);
            mActivity.show(equipmentListForTransferFragment);
        }
    };
    private TabLayout tabLayout;
    private TransferListViewModel viewModel;
    private TransferListAdapter transferListAdapter;
    private TransferListResponse transferListResponse;
    private TransferItem selectedTransferItem;
    private final TransferItemClickCallback transferItemClickCallback = item -> {
        if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
            selectedTransferItem = item;
            mActivity.showInputDialog("Plakayı girin", 1, "Plaka alanı boş bırakılamaz");


//            if (tabLayout.getSelectedTabPosition() == 0){
//                mActivity.prepareStepView(getDeliveryStepList());
//            }else{
//                mActivity.prepareStepView(getReturnStepList(item));
//            }
//
//            item.groupCodeInformation = CommonMethods.instance.getSelectedGroupCodeInformation(getContext(),item.selectedEquipment.groupCodeId);
//            TransferInformationFragment fragment = TransferInformationFragment.withTransferItem(item);
//            mActivity.show(fragment);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_transfer_dashboard, container, false);
        prepareRecyclerView();
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = ViewModelProviders.of(this,
                viewModelFactory).get(TransferListViewModel.class);

        binding.setTransferTypeCallback(transferTypeClickCallback);
        binding.setTransferType(EnumUtils.TransferType.NONE);
        binding.newTransferList.setVisibility(View.GONE);
        binding.transferListLayout.setVisibility(View.VISIBLE);
        binding.transferSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (transferListAdapter != null && transferListResponse != null && tabLayout.getSelectedTabPosition() == 0) {
                    transferListAdapter.setItemList(filterContracts(charSequence.toString(), transferListResponse.deliveryTransferList));
                }
                if (transferListAdapter != null && transferListResponse != null && tabLayout.getSelectedTabPosition() == 1) {
                    transferListAdapter.setItemList(filterContracts(charSequence.toString(), transferListResponse.returnTransferList));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        prepareTabLayout();
        setupSwipeRefreshLayout();
        observeTransferList();
        updateTabletIfNeeded();
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
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    private List<TransferItem> filterContracts(String query, List<TransferItem> transferItems) {
        if (TextUtils.isEmpty(query)) {
            return transferItems;
        }

        query = query.toLowerCase(LocaleUtils.TR);
        Pattern pattern = Pattern.compile(RegexUtils.instance.createTurkishRegexFor(query));
        HashSet<TransferItem> filtered = new LinkedHashSet<>();
        for (TransferItem item : transferItems) {
            Matcher transferNumber = pattern.matcher(item.transferNumber.toLowerCase(LocaleUtils.TR));
            Matcher plateNumber = pattern.matcher(item.selectedEquipment.plateNumber.toLowerCase(LocaleUtils.TR));
            if (plateNumber.find() || transferNumber.find()) {
                filtered.add(item);
            }
        }

        return new ArrayList<>(filtered);
    }

    private void setupSwipeRefreshLayout() {
        binding.swipeContainer.setOnRefreshListener(
                this::observeTransferList);

        binding.swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    private void observeTransferList() {
        viewModel.setBranchId(new User().getUser(getContext()).userBranch.branchId);
        viewModel.getTransferListObservable().observe(this, response -> {
            if (binding.swipeContainer != null) {
                binding.swipeContainer.setRefreshing(false);
            }
            if (response == null) {
                mActivity.showMessageDialog(getString(R.string.unknown_error_message));
            } else if (response.responseResult.result) {
                transferListResponse = response;
                if (tabLayout.getSelectedTabPosition() == 0) {
                    transferListAdapter.setItemList(response.deliveryTransferList);
                } else {
                    transferListAdapter.setItemList(response.returnTransferList);
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
            TextView totalDeliveryCount = tabLayout.getTabAt(0).getCustomView().findViewById(R.id.total_delivery_transfer);
            totalDeliveryCount.setText(viewModel.findDeliveryTransferCount());
            TextView firstDeliveryTime = tabLayout.getTabAt(0).getCustomView().findViewById(R.id.first_delivery_time);
            firstDeliveryTime.setText(viewModel.findFirstDeliveryTransferTime());

            // Rental Contract Dashbord Informations
            TextView totalRentalCount = tabLayout.getTabAt(1).getCustomView().findViewById(R.id.total_return_transfer);
            totalRentalCount.setText(viewModel.findReturnTransferCount());
            TextView firstRentalTime = tabLayout.getTabAt(1).getCustomView().findViewById(R.id.first_return_time);
            firstRentalTime.setText(viewModel.findFirstReturnTransferTime());
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void prepareRecyclerView() {
        transferListAdapter = new TransferListAdapter(transferItemClickCallback, getContext());
        binding.transferList.setAdapter(transferListAdapter);
        binding.transferList.addItemDecoration(new DividerItemDecoration(binding.transferList.getContext(), DividerItemDecoration.VERTICAL));
    }

    private void prepareTabLayout() {
        tabLayout = mActivity.findViewById(R.id.tabs);
        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == PICKUP_TRANSFER_TAB) {
                    binding.newTransferList.setVisibility(View.GONE);
                    binding.transferListLayout.setVisibility(View.VISIBLE);
                    tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(mActivity, R.color.dashboard_will_transfer_color));
                    binding.headerTitle.setText(getString(R.string.will_transfer_title));
                    if (transferListResponse != null) {
                        transferListAdapter.setItemList(transferListResponse.deliveryTransferList);
                    }
                } else if (tab.getPosition() == DROPOFF_TRANSFER_TAB) {
                    binding.newTransferList.setVisibility(View.GONE);
                    binding.transferListLayout.setVisibility(View.VISIBLE);
                    tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(mActivity, R.color.dashboard_future_transfer_color));
                    binding.headerTitle.setText(getString(R.string.future_transfer_title));
                    if (transferListResponse != null) {
                        transferListAdapter.setItemList(transferListResponse.returnTransferList);
                    }
                } else {
                    binding.newTransferList.setVisibility(View.VISIBLE);
                    binding.transferListLayout.setVisibility(View.GONE);
                    tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(mActivity, R.color.dashboard_new_transfer_bg));
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

    @Override
    public void getInputValue(String input, Integer tag) {
        super.getInputValue(input, tag);
        if (selectedTransferItem.selectedEquipment.plateNumber.toLowerCase().equals(input.toLowerCase())) {
            if (tabLayout.getSelectedTabPosition() == 0) {
                mActivity.prepareStepView(getDeliveryStepList());
            } else {
                mActivity.prepareStepView(getReturnStepList(selectedTransferItem));
            }

            selectedTransferItem.groupCodeInformation = CommonMethods.instance.getSelectedGroupCodeInformation(selectedTransferItem.selectedEquipment.groupCodeId);
            TransferInformationFragment fragment = TransferInformationFragment.withTransferItem(selectedTransferItem);
            mActivity.show(fragment);
        }
    }

    private ArrayList<String> getDeliveryStepList() {
        ArrayList<String> steps = new ArrayList<>();
        steps.add(getString(R.string.transfer_information_title));
        steps.add(getString(R.string.damage_entry_title));
        steps.add(getString(R.string.car_information_title));
        steps.add(getString(R.string.transfer_delivery_summary_title));

        return steps;
    }

    private ArrayList<String> getReturnStepList(TransferItem item) {
        ArrayList<String> steps = new ArrayList<>();
        steps.add(getString(R.string.transfer_information_title));
        steps.add(getString(R.string.damage_entry_title));
        if (item.transferType == EnumUtils.TransferType.DAMAGE.getIntValue()) {
            steps.add(getString(R.string.repaired_damage_entry_title));
        }
        steps.add(getString(R.string.car_information_title));
        steps.add(getString(R.string.transfer_return_summary_title));

        return steps;
    }
}
