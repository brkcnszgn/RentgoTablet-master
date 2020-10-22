package com.creatifsoftware.filonova.view.fragment.transfers;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.creatifsoftware.filonova.R;
import com.creatifsoftware.filonova.di.Injectable;
import com.creatifsoftware.filonova.model.DamageItem;
import com.creatifsoftware.filonova.model.TransferItem;
import com.creatifsoftware.filonova.model.User;
import com.creatifsoftware.filonova.utils.BlobStorageManager;
import com.creatifsoftware.filonova.view.fragment.dashboards.TransferDashboardFragment;
import com.creatifsoftware.filonova.viewmodel.ReturnTransferSummaryViewModel;

import javax.inject.Inject;

/**
 * Created by kerembalaban on 24.04.2019 at 17:27.
 */
public class TransferSummaryForReturnFragment extends TransferSummaryFragment implements Injectable {
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private ReturnTransferSummaryViewModel viewModel;

    public static TransferSummaryForReturnFragment withTransferItem(TransferItem transferItem) {
        TransferSummaryForReturnFragment fragment = new TransferSummaryForReturnFragment();
        Bundle args = new Bundle();

        args.putSerializable(KEY_SELECTED_TRANSFER, transferItem);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = ViewModelProviders.of(this,
                viewModelFactory).get(ReturnTransferSummaryViewModel.class);
    }

    @Override
    public void navigate() {
        String message = String.format("%s numaralı transfer belgeniz, %s plakalı araç iadesi ile kapatılacaktır, emin misiniz?"
                , selectedTransfer.transferNumber, selectedTransfer.selectedEquipment.plateNumber);
        mActivity.showConfirmationDialog(getString(R.string.dialog_button_cancel), getString(R.string.dialog_button_yes), message);
    }

    @Override
    public void confirmationButtonClicked() {
        updateReturnTransfer();
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

    private void updateReturnTransfer() {
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
                    String imageName = BlobStorageManager.instance.prepareEquipmentImageName(selectedTransfer.selectedEquipment, selectedTransfer.transferNumber, "return", damageItem.damageId);
                    BlobStorageManager.instance.UploadImage(BlobStorageManager.instance.getEquipmentsContainerName(), damageItem.damagePhotoFile, imageName);
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
                String imageName = BlobStorageManager.instance.prepareEquipmentImageName(selectedTransfer.selectedEquipment, selectedTransfer.transferNumber, "return", "kilometer_fuel_image");
                BlobStorageManager.instance.UploadImage(BlobStorageManager.instance.getEquipmentsContainerName(), selectedTransfer.selectedEquipment.kilometerFuelImageFile, imageName);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        thread.start();
    }
}
