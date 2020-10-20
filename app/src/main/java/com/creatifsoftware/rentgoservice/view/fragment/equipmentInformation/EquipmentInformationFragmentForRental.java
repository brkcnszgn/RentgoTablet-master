package com.creatifsoftware.rentgoservice.view.fragment.equipmentInformation;

import android.os.Bundle;
import android.os.Handler;
import android.widget.RatingBar;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.creatifsoftware.rentgoservice.R;
import com.creatifsoftware.rentgoservice.di.Injectable;
import com.creatifsoftware.rentgoservice.model.ContractItem;
import com.creatifsoftware.rentgoservice.model.Equipment;
import com.creatifsoftware.rentgoservice.model.base.ResponseResult;
import com.creatifsoftware.rentgoservice.utils.BlobStorageManager;
import com.creatifsoftware.rentgoservice.view.fragment.FinePriceFragment;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import javax.inject.Inject;

/**
 * Created by kerembalaban on 16.02.2019 at 18:38.
 */
public class EquipmentInformationFragmentForRental extends EquipmentInformationFragment implements Injectable {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    /**
     * Creates project fragment for specific project ID
     */
    public static EquipmentInformationFragmentForRental forSelectedContract(ContractItem selectedContract) {
        EquipmentInformationFragmentForRental fragment = new EquipmentInformationFragmentForRental();
        Bundle args = new Bundle();

        args.putSerializable(KEY_SELECTED_CONTRACT, selectedContract);
        args.putSerializable(KEY_SELECTED_EQUIPMENT, selectedContract.selectedEquipment);
        args.putSerializable(KEY_GROUP_CODE_INFORMATION, selectedContract.groupCodeInformation);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.carInformationTitle.setText(String.format(Locale.getDefault(), "%s - %s (%s)", getString(R.string.car_information_title), selectedContract.contractNumber, selectedContract.pnrNumber));
        getStepView().go(2, true);
        if (selectedContract.selectedEquipment.kilometerFuelImageFile != null) {
            Picasso.get().load(selectedContract.selectedEquipment.kilometerFuelImageFile).into(binding.carInformationLayout.kilometerFuelImage);
        }
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
                String imageName = BlobStorageManager.instance.prepareEquipmentImageName(selectedContract.selectedEquipment, selectedContract.contractNumber, "rental", "kilometer_fuel_image");
                BlobStorageManager.instance.UploadImage(BlobStorageManager.instance.getEquipmentsContainerName(), selectedContract.selectedEquipment.kilometerFuelImageFile, imageName);

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
            selectedContract = getSelectedContract();
            selectedContract.selectedEquipment.currentKmValue = Integer.valueOf(binding.carInformationLayout.kilometerValue.getText().toString());
            selectedContract.selectedEquipment.currentFuelValue = (int) binding.carInformationLayout.fuelRatingBar.rate.getRating();
            FinePriceFragment finePriceFragment = FinePriceFragment.forSelectedContract(selectedContract);
            super.changeFragment(finePriceFragment);
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
