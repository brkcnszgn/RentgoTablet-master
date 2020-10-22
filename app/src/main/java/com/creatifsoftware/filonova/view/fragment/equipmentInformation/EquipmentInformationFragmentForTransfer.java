package com.creatifsoftware.filonova.view.fragment.equipmentInformation;

import android.os.Bundle;
import android.os.Handler;
import android.widget.RatingBar;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.creatifsoftware.filonova.R;
import com.creatifsoftware.filonova.di.Injectable;
import com.creatifsoftware.filonova.model.Equipment;
import com.creatifsoftware.filonova.model.TransferItem;
import com.creatifsoftware.filonova.model.base.ResponseResult;
import com.creatifsoftware.filonova.utils.BlobStorageManager;
import com.creatifsoftware.filonova.utils.EnumUtils;
import com.creatifsoftware.filonova.view.fragment.transfers.TransferSummaryForReturnFragment;
import com.creatifsoftware.filonova.view.fragment.transfers.TransferSummaryFragment;

import java.util.Locale;

import javax.inject.Inject;

/**
 * Created by kerembalaban on 16.02.2019 at 18:38.
 */
public class EquipmentInformationFragmentForTransfer extends EquipmentInformationFragment implements Injectable {
    @Inject
    ViewModelProvider.Factory viewModelFactory;

    /**
     * Creates project fragment for specific project ID
     */
    public static EquipmentInformationFragmentForTransfer forSelectedTransfer(TransferItem transferItem) {
        EquipmentInformationFragmentForTransfer fragment = new EquipmentInformationFragmentForTransfer();
        Bundle args = new Bundle();

        args.putSerializable(KEY_SELECTED_EQUIPMENT, transferItem.selectedEquipment);
        args.putSerializable(KEY_GROUP_CODE_INFORMATION, transferItem.groupCodeInformation);
        args.putSerializable(KEY_SELECTED_TRANSFER, transferItem);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getSelectedTransfer().transferType == EnumUtils.TransferType.DAMAGE.getIntValue() &&
                getSelectedTransfer().statusCode == EnumUtils.TransferStatusCode.TRANSFERRED.getIntValue()) {
            getStepView().go(3, true);
        } else {
            getStepView().go(2, true);
        }
        binding.carInformationTitle.setText(String.format(Locale.getDefault(), "%s - %s", getString(R.string.car_information_title), getSelectedTransfer().transferNumber));
    }

    @Override
    public void ratingBarChangeListener(RatingBar ratingBar, Equipment equipment) {
        super.ratingBarChangeListener(ratingBar, equipment);
        binding.carInformationLayout.fuelCheckbox.setChecked(true);
        //binding.carInformationLayout.currentFuelValue.setText(String.format(Locale.getDefault(),"%.0f",ratingBar.getRating()));
        //equipment.currentFuelValue = (int)binding.carInformationLayout.fuelRatingBar.rate.getRating();
    }

    @Override
    protected void navigate() {
        hasBlobStorageError = false;
        super.showLoading();
        (new Handler()).postDelayed(this::upload, 1000);
    }

    private void upload() {
        Thread thread = new Thread(() -> {
            try {
                String imageName = BlobStorageManager.instance.prepareEquipmentImageName(
                        selectedTransfer.selectedEquipment,
                        selectedTransfer.transferNumber,
                        selectedTransfer.statusCode == EnumUtils.TransferStatusCode.WAITING_FOR_DELIVERY.getIntValue() ? "delivery" : "rental",
                        "kilometer_fuel_image");

                BlobStorageManager.instance.UploadImage(BlobStorageManager.instance.getEquipmentsContainerName(), selectedTransfer.selectedEquipment.kilometerFuelImageFile, imageName);

            } catch (Exception e) {
                super.hideLoading();
                hasBlobStorageError = true;
                mActivity.showMessageDialog(e.getLocalizedMessage());
                e.printStackTrace();
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        super.hideLoading();

        if (!hasBlobStorageError) {
            selectedTransfer = getSelectedTransfer();
            selectedTransfer.selectedEquipment.currentKmValue = Integer.valueOf(binding.carInformationLayout.kilometerValue.getText().toString());
            selectedTransfer.selectedEquipment.currentFuelValue = (int) binding.carInformationLayout.fuelRatingBar.rate.getRating();

            if (selectedTransfer.statusCode == EnumUtils.TransferStatusCode.WAITING_FOR_DELIVERY.getIntValue()) {
                TransferSummaryFragment fragment = TransferSummaryFragment.withTransferItem(selectedTransfer);
                mActivity.show(fragment);
            } else {
                TransferSummaryForReturnFragment fragment = TransferSummaryForReturnFragment.withTransferItem(selectedTransfer);
                mActivity.show(fragment);
            }
        }
    }

    @Override
    protected ResponseResult checkBeforeNavigate() {
        if (!binding.carInformationLayout.kilometerCheckbox.isChecked()) {
            return new ResponseResult(false, getString(R.string.set_kilometer_error));
        } else if (!binding.carInformationLayout.fuelCheckbox.isChecked()) {
            return new ResponseResult(false, getString(R.string.fuel_value_error));
        } else if (!binding.carInformationLayout.kilometerFuelPhotoCheckbox.isChecked()) {
            return new ResponseResult(false, getString(R.string.kilometer_fuel_no_image_error));
        }

        return super.checkBeforeNavigate();
    }
}
