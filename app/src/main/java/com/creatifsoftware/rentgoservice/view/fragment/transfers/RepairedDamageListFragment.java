package com.creatifsoftware.rentgoservice.view.fragment.transfers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import com.creatifsoftware.rentgoservice.R;
import com.creatifsoftware.rentgoservice.databinding.FragmentDamageToBeRepairedBinding;
import com.creatifsoftware.rentgoservice.di.Injectable;
import com.creatifsoftware.rentgoservice.model.DamageItem;
import com.creatifsoftware.rentgoservice.model.Equipment;
import com.creatifsoftware.rentgoservice.model.TransferItem;
import com.creatifsoftware.rentgoservice.view.adapter.RepairedDamageListAdapter;
import com.creatifsoftware.rentgoservice.view.callback.DamageItemClickCallback;
import com.creatifsoftware.rentgoservice.view.fragment.base.BaseFragment;
import com.creatifsoftware.rentgoservice.view.fragment.equipmentInformation.EquipmentInformationFragmentForTransfer;
import com.creatifsoftware.rentgoservice.viewmodel.DamageEntryViewModel;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by kerembalaban on 16.02.2019 at 18:38.
 */
public class RepairedDamageListFragment extends BaseFragment implements Injectable {
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private RepairedDamageListAdapter damageListAdapter;
    private final DamageItemClickCallback damageItemClickCallback = item -> {
        if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
            item.isRepaired = !item.isRepaired;
            damageListAdapter.notifyDataSetChanged();
        }
    };
    private FragmentDamageToBeRepairedBinding binding;
    private Equipment selectedEquipment;
    private boolean isServiceCalled = false;
    private DamageEntryViewModel viewModel;

    public static RepairedDamageListFragment forSelectedTransfer(TransferItem transferItem) {
        RepairedDamageListFragment fragment = new RepairedDamageListFragment();
        Bundle args = new Bundle();

        args.putSerializable(KEY_SELECTED_TRANSFER, transferItem);
        args.putSerializable(KEY_SELECTED_EQUIPMENT, transferItem.selectedEquipment);
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        damageListAdapter = new RepairedDamageListAdapter(damageItemClickCallback);

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_damage_to_be_repaired, container, false);
        binding.damageList.setAdapter(damageListAdapter);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getStepView().go(2, true);
        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(DamageEntryViewModel.class);

        Bundle bundle = getArguments();
        if (bundle != null) {
            selectedEquipment = (Equipment) bundle.getSerializable(KEY_SELECTED_EQUIPMENT);
            prepareDamageListRecyclerView();
        }

        //observeViewModel(viewModel);
    }

    private void prepareDamageListRecyclerView() {
        ArrayList<DamageItem> damages = new ArrayList<>();
        for (DamageItem item : selectedEquipment.damageList) {
            if (!item.damageInfo.isNewDamage) {
                damages.add(item);
            }
        }
        damageListAdapter = new RepairedDamageListAdapter(damageItemClickCallback);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        binding.damageList.setLayoutManager(layoutManager);
        damageListAdapter.setDamageList(damages);
        binding.damageList.setAdapter(damageListAdapter);
    }

    private void observeViewModel(final DamageEntryViewModel viewModel) {
        if (viewModel.getDamageListByEquipment().hasObservers()) {
            damageListAdapter.setDamageList(selectedEquipment.damageList);
            return;
        }

        super.showLoading();
        viewModel.setEquipmentId(selectedEquipment.equipmentId);
        viewModel.getDamageListByEquipment().observe(this, damageListResponse -> {
            hideLoading();
            if (damageListResponse == null) {
                isServiceCalled = true;
                showMessageDialog(getString(R.string.unknown_error_message));
            } else if (damageListResponse.responseResult.result) {
                selectedEquipment.damageList = damageListResponse.damageList;
                damageListAdapter.setDamageList(damageListResponse.damageList);
            } else {
                isServiceCalled = true;
                showMessageDialog(getString(R.string.unknown_error_message));
            }
        });
    }

    @Override
    public void messageDialogDoneButtonClicked() {
        if (isServiceCalled) {
            viewModel.getDamageListByEquipment().removeObservers(this);
            mActivity.onBackPressed();
            //mActivity.getSupportFragmentManager().popBackStackImmediate();
        }
    }

    @Override
    protected void navigate() {
        EquipmentInformationFragmentForTransfer nextFragment = EquipmentInformationFragmentForTransfer.forSelectedTransfer(selectedTransfer);
        mActivity.show(nextFragment);
    }
}
