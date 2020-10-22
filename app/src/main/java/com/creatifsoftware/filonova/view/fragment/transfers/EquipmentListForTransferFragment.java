package com.creatifsoftware.filonova.view.fragment.transfers;

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
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.creatifsoftware.filonova.R;
import com.creatifsoftware.filonova.databinding.FragmentTransferEquipmentsBinding;
import com.creatifsoftware.filonova.di.Injectable;
import com.creatifsoftware.filonova.model.Equipment;
import com.creatifsoftware.filonova.model.TransferItem;
import com.creatifsoftware.filonova.model.User;
import com.creatifsoftware.filonova.utils.CommonMethods;
import com.creatifsoftware.filonova.utils.LocaleUtils;
import com.creatifsoftware.filonova.utils.RegexUtils;
import com.creatifsoftware.filonova.view.adapter.EquipmentDashboardAdapter;
import com.creatifsoftware.filonova.view.callback.EquipmentClickCallback;
import com.creatifsoftware.filonova.view.fragment.base.BaseFragment;
import com.creatifsoftware.filonova.viewmodel.CarsListViewModel;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

public class EquipmentListForTransferFragment extends BaseFragment implements Injectable {
    private static final int ALL_EQUIPMENTS_TAB = 0;
    private final EquipmentClickCallback equipmentClickCallback = item -> {
        if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
            selectedTransfer.selectedEquipment = item;
            selectedTransfer.groupCodeInformation = CommonMethods.instance.getSelectedGroupCodeInformation(item.groupCodeId);
            CreateTransferFragment createTransferFragment = CreateTransferFragment.withTransferItem(selectedTransfer);
            mActivity.show(createTransferFragment);
        }
    };
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private FragmentTransferEquipmentsBinding binding;
    private List<Equipment> equipmentList;
    private EquipmentDashboardAdapter equipmentDashboardAdapter;

    public static EquipmentListForTransferFragment withTransferItem(TransferItem transferItem) {
        EquipmentListForTransferFragment fragment = new EquipmentListForTransferFragment();
        Bundle args = new Bundle();

        args.putSerializable(KEY_SELECTED_TRANSFER, transferItem);
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_transfer_equipments, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        CarsListViewModel viewModel = ViewModelProviders.of(this,
                viewModelFactory).get(CarsListViewModel.class);
        observeViewModel(viewModel);
        binding.searchPlate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (equipmentDashboardAdapter != null)
                    equipmentDashboardAdapter.setEquipmentList(filterEquipments(charSequence.toString(), equipmentList));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
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

    private void observeViewModel(CarsListViewModel viewModel) {
        if (viewModel.getCarListObservable().hasObservers()) {
            prepareEquipmentDashboardRecyclerView();
            setDashboardLayoutInformation();
            return;
        }
        showLoading();
        viewModel.setBranchId(new User().getUser(getContext()).userBranch.branchId);
        viewModel.getCarListObservable().observe(this, equipmentListResponse -> {
            hideLoading();
            equipmentList = new ArrayList<>();
            equipmentList = equipmentListResponse.equipmentList;
            prepareEquipmentDashboardRecyclerView();
            setDashboardLayoutInformation();
        });
    }

    private void setDashboardLayoutInformation() {
        try {
            TabLayout tabLayout = binding.tabs;
            TextView totalEquipmentsCount = tabLayout.getTabAt(ALL_EQUIPMENTS_TAB).getCustomView().findViewById(R.id.total_count);
            totalEquipmentsCount.setText(String.valueOf(equipmentList.size()));
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void prepareEquipmentDashboardRecyclerView() {
        equipmentDashboardAdapter = new EquipmentDashboardAdapter(equipmentClickCallback, getContext());
        equipmentDashboardAdapter.setEquipmentList(equipmentList);
        binding.equipmentList.addItemDecoration(new DividerItemDecoration(binding.equipmentList.getContext(), DividerItemDecoration.VERTICAL));
        binding.equipmentList.setAdapter(equipmentDashboardAdapter);
    }

}
