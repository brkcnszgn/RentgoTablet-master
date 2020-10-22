package com.creatifsoftware.filonova.view.fragment.dashboards;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.creatifsoftware.filonova.R;
import com.creatifsoftware.filonova.databinding.FragmentEquipmentDashboardBinding;
import com.creatifsoftware.filonova.di.Injectable;
import com.creatifsoftware.filonova.model.Equipment;
import com.creatifsoftware.filonova.model.GroupCodeInformation;
import com.creatifsoftware.filonova.model.User;
import com.creatifsoftware.filonova.utils.EnumUtils;
import com.creatifsoftware.filonova.utils.LocaleUtils;
import com.creatifsoftware.filonova.utils.RegexUtils;
import com.creatifsoftware.filonova.utils.SharedPrefUtils;
import com.creatifsoftware.filonova.view.activity.MainActivity;
import com.creatifsoftware.filonova.view.adapter.EquipmentDashboardAdapter;
import com.creatifsoftware.filonova.view.callback.EquipmentClickCallback;
import com.creatifsoftware.filonova.view.callback.GroupCodeItemClickCallback;
import com.creatifsoftware.filonova.viewmodel.CarsListViewModel;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

public class EquipmentDashboardFragment extends Fragment implements Injectable {
    private static final int ALL_EQUIPMENTS_TAB = 0;
    private static final int AVAILABLE_EQUIPMENTS_TAB = 1;
    private static final int RENTAL_EQUIPMENTS_TAB = 2;
    private static final int IN_WASHING_EQUIPMENTS_TAB = 3;
    private static final int IN_FUEL_FILLING_EQUIPMENTS_TAB = 4;
    private static final int IN_TRANSFERS_EQUIPMENTS_TAB = 5;
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private FragmentEquipmentDashboardBinding binding;
    private EquipmentDashboardAdapter equipmentDashboardAdapter;
    private List<Equipment> equipmentList;
    private GroupCodeInformation selectedGroupCode;
    private int selectedStatusCode = EnumUtils.EquipmentStatusCode.NONE.getIntValue();
    private final GroupCodeItemClickCallback groupCodeItemClickCallback = item -> {
        if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
            selectedGroupCode = item;
            equipmentDashboardAdapter.setEquipmentList(filterEquipmentByStatusAndGroupCode(selectedStatusCode));
            equipmentDashboardAdapter.notifyDataSetChanged();
        }
    };
    private ProgressDialog progressDialog;
    private CarsListViewModel viewModel;
    private TabLayout tabLayout;
    private MainActivity mActivity;
    private final EquipmentClickCallback equipmentClickCallback = equipment -> {
        if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {


            if (equipment.statusReason == EnumUtils.EquipmentStatusCode.AVAILABLE.getIntValue()) {
                int[] items = new int[]{EnumUtils.EquipmentStatusCode.IN_FUEL_FILLING.getIntValue(),
                        EnumUtils.EquipmentStatusCode.IN_WASHING.getIntValue()};
                //EnumUtils.EquipmentStatusCode.RENTAL.getIntValue()};
                showAlertDialogForSelection(items, equipment);
            }
//            if (equipment.statusReason == EnumUtils.EquipmentStatusCode.RENTAL.getIntValue()){
//                int[] items = new int[]{EnumUtils.EquipmentStatusCode.AVAILABLE.getIntValue(),
//                                        EnumUtils.EquipmentStatusCode.IN_WASHING.getIntValue(),
//                                        EnumUtils.EquipmentStatusCode.IN_FUEL_FILLING.getIntValue()};
//                showAlertDialogForSelection(items,equipment);
//            }
            if (equipment.statusReason == EnumUtils.EquipmentStatusCode.IN_FUEL_FILLING.getIntValue()) {
                int[] items = new int[]{EnumUtils.EquipmentStatusCode.AVAILABLE.getIntValue(),
                        EnumUtils.EquipmentStatusCode.IN_WASHING.getIntValue()};
                //EnumUtils.EquipmentStatusCode.RENTAL.getIntValue()};
                showAlertDialogForSelection(items, equipment);
            }
            if (equipment.statusReason == EnumUtils.EquipmentStatusCode.IN_WASHING.getIntValue()) {
                int[] items = new int[]{EnumUtils.EquipmentStatusCode.AVAILABLE.getIntValue(),
                        EnumUtils.EquipmentStatusCode.IN_FUEL_FILLING.getIntValue()};
                //EnumUtils.EquipmentStatusCode.RENTAL.getIntValue()};
                showAlertDialogForSelection(items, equipment);
            }
//            if (equipment.statusReason == EnumUtils.EquipmentStatusCode.IN_TRANSFER.getIntValue()){
//                int[] items = new int[]{EnumUtils.EquipmentStatusCode.AVAILABLE.getIntValue(),
//                                        EnumUtils.EquipmentStatusCode.IN_WASHING.getIntValue(),
//                                        EnumUtils.EquipmentStatusCode.IN_FUEL_FILLING.getIntValue(),
//                                        EnumUtils.EquipmentStatusCode.RENTAL.getIntValue()};
//                showAlertDialogForSelection(items,equipment);
//            }

            //EquipmentInformationFragmentForEquipmentDashboard equipmentInformationFragmentForEquipmentDashboard = EquipmentInformationFragmentForEquipmentDashboard.forSelectedTransferItem(item);
            //mActivity.show(equipmentInformationFragmentForEquipmentDashboard);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @androidx.annotation.Nullable ViewGroup container,
                             @androidx.annotation.Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_equipment_dashboard, container, false);
        prepareEquipmentDashboardRecyclerView();
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
                viewModelFactory).get(CarsListViewModel.class);

        prepareTabLayout();
        setupSwipeRefreshLayout();
        observeViewModel();
        updateTabletIfNeeded();

        binding.searchPlate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (equipmentDashboardAdapter != null)
                    equipmentDashboardAdapter.setEquipmentList(filterEquipments(charSequence.toString(), selectedStatusCode == -1 ? equipmentList : filterEquipmentByStatusCode(selectedStatusCode)));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
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

    private List<Equipment> filterEquipments(String query, List<Equipment> equipmentList) {
        if (TextUtils.isEmpty(query)) {
            return equipmentList;
        }

        query = query.toLowerCase(LocaleUtils.TR);
        Pattern pattern = Pattern.compile(RegexUtils.instance.createTurkishRegexFor(query));
        HashSet<Equipment> filtered = new LinkedHashSet<>();
        for (Equipment item : equipmentList) {
            Matcher plateNumber = null;
            Matcher brandName = null;
            Matcher modelName = null;

            if (item.plateNumber != null) {
                plateNumber = pattern.matcher(item.plateNumber.toLowerCase(LocaleUtils.TR));
            }
            if (item.brandName != null) {
                brandName = pattern.matcher(item.brandName.toLowerCase(LocaleUtils.TR));
            }
            if (item.modelName != null) {
                modelName = pattern.matcher(item.modelName.toLowerCase(LocaleUtils.TR));
            }

            if ((plateNumber != null && plateNumber.find()) || (brandName != null && brandName.find()) || (modelName != null && modelName.find())) {
                filtered.add(item);
            }
        }

        return new ArrayList<>(filtered);
    }

    private void setupSwipeRefreshLayout() {
        binding.swipeContainer.setOnRefreshListener(
                this::observeViewModel);

        binding.swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void observeViewModel() {
        viewModel.setBranchId(new User().getUser(getContext()).userBranch.branchId);
        viewModel.getCarListObservable().observe(getViewLifecycleOwner(), equipmentListResponse -> {
            if (binding.swipeContainer != null) {
                binding.swipeContainer.setRefreshing(false);
            }
            if (equipmentListResponse == null) {
                mActivity.showMessageDialog(getString(R.string.unknown_error_message));
            } else if (equipmentListResponse.responseResult.result) {
                equipmentList = new ArrayList<>();
                equipmentList = equipmentListResponse.equipmentList;
                if (tabLayout.getSelectedTabPosition() == ALL_EQUIPMENTS_TAB) {
                    equipmentDashboardAdapter.setEquipmentList(equipmentList);
                } else if (tabLayout.getSelectedTabPosition() == AVAILABLE_EQUIPMENTS_TAB) {
                    equipmentDashboardAdapter.setEquipmentList(filterEquipmentByStatusCode(EnumUtils.EquipmentStatusCode.AVAILABLE.getIntValue()));
                } else if (tabLayout.getSelectedTabPosition() == RENTAL_EQUIPMENTS_TAB) {
                    equipmentDashboardAdapter.setEquipmentList(filterEquipmentByStatusCode(EnumUtils.EquipmentStatusCode.RENTAL.getIntValue()));
                } else if (tabLayout.getSelectedTabPosition() == IN_WASHING_EQUIPMENTS_TAB) {
                    equipmentDashboardAdapter.setEquipmentList(filterEquipmentByStatusCode(EnumUtils.EquipmentStatusCode.IN_WASHING.getIntValue()));
                } else if (tabLayout.getSelectedTabPosition() == IN_FUEL_FILLING_EQUIPMENTS_TAB) {
                    equipmentDashboardAdapter.setEquipmentList(filterEquipmentByStatusCode(EnumUtils.EquipmentStatusCode.IN_FUEL_FILLING.getIntValue()));
                } else {
                    equipmentDashboardAdapter.setEquipmentList(filterEquipmentByStatusCode(EnumUtils.EquipmentStatusCode.IN_TRANSFER.getIntValue()));
                }
                //prepareEquipmentDashboardRecyclerView(equipmentList);
                setDashboardLayoutInformation();
            } else {
                mActivity.showMessageDialog(equipmentListResponse.responseResult.exceptionDetail);
            }

        });
    }

    private void setDashboardLayoutInformation() {
        try {
            TextView totalEquipmentsCount = tabLayout.getTabAt(ALL_EQUIPMENTS_TAB).getCustomView().findViewById(R.id.total_count);
            totalEquipmentsCount.setText(String.valueOf(equipmentList.size()));

            TextView totalAvailableCount = tabLayout.getTabAt(AVAILABLE_EQUIPMENTS_TAB).getCustomView().findViewById(R.id.total_count);
            totalAvailableCount.setText(String.valueOf(filterEquipmentByStatusCode(EnumUtils.EquipmentStatusCode.AVAILABLE.getIntValue()).size()));

            TextView totalRentalCount = tabLayout.getTabAt(RENTAL_EQUIPMENTS_TAB).getCustomView().findViewById(R.id.total_count);
            totalRentalCount.setText(String.valueOf(filterEquipmentByStatusCode(EnumUtils.EquipmentStatusCode.RENTAL.getIntValue()).size()));

            TextView totalInWashingCount = tabLayout.getTabAt(IN_WASHING_EQUIPMENTS_TAB).getCustomView().findViewById(R.id.total_count);
            totalInWashingCount.setText(String.valueOf(filterEquipmentByStatusCode(EnumUtils.EquipmentStatusCode.IN_WASHING.getIntValue()).size()));

            TextView totalInFuelFillingCount = tabLayout.getTabAt(IN_FUEL_FILLING_EQUIPMENTS_TAB).getCustomView().findViewById(R.id.total_count);
            totalInFuelFillingCount.setText(String.valueOf(filterEquipmentByStatusCode(EnumUtils.EquipmentStatusCode.IN_FUEL_FILLING.getIntValue()).size()));

            TextView totalInTransfersCount = tabLayout.getTabAt(IN_TRANSFERS_EQUIPMENTS_TAB).getCustomView().findViewById(R.id.total_count);
            totalInTransfersCount.setText(String.valueOf(filterEquipmentByStatusCode(EnumUtils.EquipmentStatusCode.IN_TRANSFER.getIntValue()).size()));
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void prepareTabLayout() {
        tabLayout = mActivity.findViewById(R.id.tabs);
        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //binding.carsHeaderIcon.setImageTintList(tab.getCustomView().getBackgroundTintList());
                binding.allFleetSearchTitle.setText(((TextView) tab.getCustomView().findViewById(R.id.tab_title)).getText());
                binding.searchPlate.setText("");
                binding.searchPlate.clearFocus();

                if (tab.getPosition() == ALL_EQUIPMENTS_TAB) {
                    selectedStatusCode = EnumUtils.EquipmentStatusCode.NONE.getIntValue();
                    equipmentDashboardAdapter.setEquipmentList(equipmentList);
                    equipmentDashboardAdapter.notifyDataSetChanged();
                    tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(mActivity, R.color.dashboard_delivery_color));
                    binding.searchPlate.setCompoundDrawableTintList(ContextCompat.getColorStateList(mActivity, R.color.dashboard_delivery_color));
                    binding.searchPlate.setBackground(ContextCompat.getDrawable(mActivity, R.drawable.all_fleet_search_button_bg));
                } else if (tab.getPosition() == AVAILABLE_EQUIPMENTS_TAB) {
                    selectedStatusCode = EnumUtils.EquipmentStatusCode.AVAILABLE.getIntValue();
                    equipmentDashboardAdapter.setEquipmentList(filterEquipmentByStatusCode(selectedStatusCode));
                    equipmentDashboardAdapter.notifyDataSetChanged();
                    tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(mActivity, R.color.dashboard_cars_vehicles_in_the_carpark_bg));
                    binding.searchPlate.setCompoundDrawableTintList(ContextCompat.getColorStateList(mActivity, R.color.dashboard_cars_vehicles_in_the_carpark_bg));
                    binding.searchPlate.setBackground(ContextCompat.getDrawable(mActivity, R.drawable.vehicles_in_the_carpark_search_button_bg));
                } else if (tab.getPosition() == RENTAL_EQUIPMENTS_TAB) {
                    selectedStatusCode = EnumUtils.EquipmentStatusCode.RENTAL.getIntValue();
                    equipmentDashboardAdapter.setEquipmentList(filterEquipmentByStatusCode(selectedStatusCode));
                    equipmentDashboardAdapter.notifyDataSetChanged();
                    tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(mActivity, R.color.dashboard_in_use_vehicles_bg));
                    binding.searchPlate.setCompoundDrawableTintList(ContextCompat.getColorStateList(mActivity, R.color.dashboard_in_use_vehicles_bg));
                    binding.searchPlate.setBackground(ContextCompat.getDrawable(mActivity, R.drawable.in_use_vehicles_search_button_bg));
                } else if (tab.getPosition() == IN_WASHING_EQUIPMENTS_TAB) {
                    selectedStatusCode = EnumUtils.EquipmentStatusCode.IN_WASHING.getIntValue();
                    equipmentDashboardAdapter.setEquipmentList(filterEquipmentByStatusCode(selectedStatusCode));
                    equipmentDashboardAdapter.notifyDataSetChanged();
                    tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.dashboard_vehicles_in_the_washing));
                    binding.searchPlate.setCompoundDrawableTintList(ContextCompat.getColorStateList(mActivity, R.color.dashboard_vehicles_in_the_washing));
                    binding.searchPlate.setBackground(ContextCompat.getDrawable(mActivity, R.drawable.vehicles_in_the_washing_search_button_bg));
                } else if (tab.getPosition() == IN_FUEL_FILLING_EQUIPMENTS_TAB) {
                    selectedStatusCode = EnumUtils.EquipmentStatusCode.IN_FUEL_FILLING.getIntValue();
                    equipmentDashboardAdapter.setEquipmentList(filterEquipmentByStatusCode(selectedStatusCode));
                    equipmentDashboardAdapter.notifyDataSetChanged();
                    tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(mActivity, R.color.fuel_filling));
                    binding.searchPlate.setCompoundDrawableTintList(ContextCompat.getColorStateList(mActivity, R.color.fuel_filling));
                    binding.searchPlate.setBackground(ContextCompat.getDrawable(mActivity, R.drawable.fuel_filling_search_button_bg));
                } else {
                    selectedStatusCode = EnumUtils.EquipmentStatusCode.IN_TRANSFER.getIntValue();
                    equipmentDashboardAdapter.setEquipmentList(filterEquipmentByStatusCode(selectedStatusCode));
                    equipmentDashboardAdapter.notifyDataSetChanged();
                    tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(mActivity, R.color.vehicles_in_the_service));
                    binding.searchPlate.setCompoundDrawableTintList(ContextCompat.getColorStateList(mActivity, R.color.vehicles_in_the_service));
                    binding.searchPlate.setBackground(ContextCompat.getDrawable(mActivity, R.drawable.vehicles_in_the_service_search_button_bg));
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

    private List<Equipment> filterEquipmentByStatusCode(int statusCode) {
        List<Equipment> tempList = new ArrayList<>();
        for (Equipment item : equipmentList) {
            if (item.statusReason == statusCode) {
                tempList.add(item);
            }
        }

        return tempList;
    }

    private List<Equipment> filterEquipmentByStatusAndGroupCode(int statusCode) {
        List<Equipment> tempList = new ArrayList<>();
        for (Equipment item : filterEquipmentByStatusCode(statusCode)) {
            if (item.groupCodeId.equals(selectedGroupCode.groupCodeId)) {
                tempList.add(item);
            }
        }

        return tempList;
    }

    private void prepareEquipmentDashboardRecyclerView() {
        equipmentDashboardAdapter = new EquipmentDashboardAdapter(equipmentClickCallback, getContext());
        //equipmentDashboardAdapter.setEquipmentList(equipmentList);
        binding.equipmentList.addItemDecoration(new DividerItemDecoration(binding.equipmentList.getContext(), DividerItemDecoration.VERTICAL));
        binding.equipmentList.setAdapter(equipmentDashboardAdapter);
    }

    private void prepareEquipmentDashboardRecyclerView(List<Equipment> equipmentList) {
        equipmentDashboardAdapter = new EquipmentDashboardAdapter(equipmentClickCallback, getContext());
        equipmentDashboardAdapter.setEquipmentList(equipmentList);
        binding.equipmentList.addItemDecoration(new DividerItemDecoration(binding.equipmentList.getContext(), DividerItemDecoration.VERTICAL));
        binding.equipmentList.setAdapter(equipmentDashboardAdapter);
    }

    private void showAlertDialogForSelection(int[] items, Equipment equipment) {
        ArrayList<CharSequence> dialogItems = new ArrayList<>();
        for (int statusCode : items) {
            dialogItems.add(EnumUtils.EquipmentStatusCode.NONE.getName(statusCode));
        }

        CharSequence[] mStringArray = new CharSequence[dialogItems.size()];
        mStringArray = dialogItems.toArray(mStringArray);

        AlertDialog.Builder adb = new AlertDialog.Builder(mActivity);
        adb.setSingleChoiceItems(mStringArray, -1, null);
        adb.setNegativeButton(getString(R.string.dialog_button_cancel), null);
        adb.setPositiveButton(getString(R.string.dialog_button_ok), (dialogInterface, i) -> updateEquipmentStatus(equipment, items[((AlertDialog) dialogInterface).getListView().getCheckedItemPosition()]));
        adb.setTitle(R.string.equipment_status_change_alert_dialog_title);
        adb.show();
    }

    private void updateEquipmentStatus(Equipment equipment, int statusCode) {
        showLoading();
        viewModel.getUpdateEquipmentStatusObservable(equipment, statusCode).observe(this, response -> {
            hideLoading();
            if (response.responseResult.result) {
                equipmentDashboardAdapter.setEquipmentList(new ArrayList<>());
                observeViewModel();
            } else {
                Toast.makeText(mActivity, response.responseResult.exceptionDetail, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showLoading() {
        progressDialog = new ProgressDialog(mActivity);
        progressDialog.setMessage("Lütfen Bekleyin...");
        //progressDialog.setTitle("ProgressDialog");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);
    }

    private void hideLoading() {
        progressDialog.dismiss();
    }
}