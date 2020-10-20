package com.creatifsoftware.rentgoservice.view.fragment.transfers;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import com.creatifsoftware.rentgoservice.R;
import com.creatifsoftware.rentgoservice.databinding.FragmentTransferSummaryBinding;
import com.creatifsoftware.rentgoservice.di.Injectable;
import com.creatifsoftware.rentgoservice.model.DamageItem;
import com.creatifsoftware.rentgoservice.model.TransferItem;
import com.creatifsoftware.rentgoservice.model.User;
import com.creatifsoftware.rentgoservice.utils.BlobStorageManager;
import com.creatifsoftware.rentgoservice.utils.EnumUtils;
import com.creatifsoftware.rentgoservice.view.adapter.DamageListAdapter;
import com.creatifsoftware.rentgoservice.view.fragment.base.BaseFragment;
import com.creatifsoftware.rentgoservice.view.fragment.dashboards.TransferDashboardFragment;
import com.creatifsoftware.rentgoservice.viewmodel.DeliveryTransferSummaryViewModel;

import javax.inject.Inject;

/**
 * Created by kerembalaban on 24.04.2019 at 17:27.
 */
public class TransferSummaryFragment extends BaseFragment implements Injectable {
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    boolean isServiceSuccess = false;
    private FragmentTransferSummaryBinding binding;
    private DeliveryTransferSummaryViewModel viewModel;

    public static TransferSummaryFragment withTransferItem(TransferItem transferItem) {
        TransferSummaryFragment fragment = new TransferSummaryFragment();
        Bundle args = new Bundle();

        args.putSerializable(KEY_SELECTED_TRANSFER, transferItem);
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_transfer_summary, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getSelectedTransfer().transferType == EnumUtils.TransferType.DAMAGE.getIntValue() &&
                getSelectedTransfer().statusCode == EnumUtils.TransferStatusCode.TRANSFERRED.getIntValue()) {
            getStepView().go(4, true);
        } else {
            getStepView().go(3, true);
        }

        viewModel = ViewModelProviders.of(this,
                viewModelFactory).get(DeliveryTransferSummaryViewModel.class);

        mActivity.showProgressBar();
        binding.setTransfer(selectedTransfer);
        if (selectedTransfer.transferType == EnumUtils.TransferType.BRANCH.getIntValue()) {
            binding.transferLocation.setText(selectedTransfer.dropoffBranch.branchName);
        } else if (selectedTransfer.transferType == EnumUtils.TransferType.DAMAGE.getIntValue() ||
                selectedTransfer.transferType == EnumUtils.TransferType.FAULT.getIntValue() ||
                selectedTransfer.transferType == EnumUtils.TransferType.SERVICE.getIntValue()) {

            binding.transferLocation.setText(selectedTransfer.serviceName);
        } else if (selectedTransfer.transferType == EnumUtils.TransferType.FREE.getIntValue()) {
            binding.transferLocation.setText(getString(R.string.free_transfer_title));
        } else {
            binding.transferLocation.setText(getString(R.string.second_hand_title));
        }

        binding.transferType.setText(EnumUtils.TransferType.NONE.getName(selectedTransfer.transferType));
        prepareDamagesRecyclerView();
    }

    private void prepareDamagesRecyclerView() {
        DamageListAdapter damageListAdapter = new DamageListAdapter(null, null, null, null, null);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 4);
        damageListAdapter.setDamageList(selectedTransfer.selectedEquipment.damageList);
        binding.damageList.setLayoutManager(layoutManager);
        binding.damageList.setAdapter(damageListAdapter);
    }

    @Override
    public void backButtonClicked() {
        super.backButtonClicked();
    }

    @Override
    public void navigate() {
        String message = String.format("%s numaralı transfer belgeniz, %s plakalı araç seçimi ile güncellenecektir, emin misiniz?"
                , selectedTransfer.transferNumber, selectedTransfer.selectedEquipment.plateNumber);

        mActivity.showConfirmationDialog(getString(R.string.dialog_button_cancel), getString(R.string.dialog_button_yes), message);
    }

    @Override
    public void confirmationButtonClicked() {
        Handler handler = new Handler();
        handler.postDelayed(this::updateTransfer, 1000);
    }

    @Override
    public void messageDialogDoneButtonClicked() {
        if (isServiceSuccess) {
            mActivity.hideProgressBar();
            TransferDashboardFragment fragment = new TransferDashboardFragment();
            mActivity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_layout, fragment, "").commit();
        }
    }

    private void updateTransfer() {
        super.showLoading();
        viewModel.setTransferItem(selectedTransfer);
        viewModel.setUser(new User().getUser(getContext()));
        viewModel.getUpdateTransferObservable().observe(getViewLifecycleOwner(), baseResponse -> {
            super.hideLoading();
            if (baseResponse == null) {
                showMessageDialog(getString(R.string.unknown_error_message));
            } else if (baseResponse.responseResult.result) {
                isServiceSuccess = true;
                //uploadDamagePhotos();
                uploadKilometerFuelImage();
                showMessageDialog(getString(R.string.transfer_success_message));
            } else {
                showMessageDialog(baseResponse.responseResult.exceptionDetail);
            }
        });
    }

    private void uploadDamagePhotos() {
        Thread thread = new Thread(() -> {
            try {
                for (DamageItem damageItem : selectedTransfer.selectedEquipment.damageList) {
                    if (damageItem.damageInfo.isNewDamage) {
                        String imageName = BlobStorageManager.instance.prepareEquipmentImageName(selectedTransfer.selectedEquipment,
                                selectedTransfer.transferNumber,
                                "delivery",
                                damageItem.damageId);
                        BlobStorageManager.instance.UploadImage(BlobStorageManager.instance.getEquipmentsContainerName(), damageItem.damagePhotoFile, imageName);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        thread.start();
    }

    private void uploadKilometerFuelImage() {
        Thread thread = new Thread(() -> {
            try {
                String imageName = BlobStorageManager.instance.prepareEquipmentImageName(selectedTransfer.selectedEquipment, selectedTransfer.transferNumber, "delivery", "kilometer_fuel_image");
                BlobStorageManager.instance.UploadImage(BlobStorageManager.instance.getEquipmentsContainerName(), selectedTransfer.selectedEquipment.kilometerFuelImageFile, imageName);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        thread.start();
    }
}
