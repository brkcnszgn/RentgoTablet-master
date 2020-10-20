package com.creatifsoftware.rentgoservice.view.fragment.equipmentInformation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.creatifsoftware.rentgoservice.R;
import com.creatifsoftware.rentgoservice.databinding.FragmentEquipmentInformationForEquipmentDashboardBinding;
import com.creatifsoftware.rentgoservice.di.Injectable;
import com.creatifsoftware.rentgoservice.model.Equipment;
import com.creatifsoftware.rentgoservice.model.TransferItem;
import com.creatifsoftware.rentgoservice.model.base.ResponseResult;

/**
 * Created by kerembalaban on 16.02.2019 at 18:38.
 */
public class EquipmentInformationFragmentForEquipmentDashboard extends EquipmentInformationFragment implements Injectable {
    public static final String KEY_SELECTED_TRANSFER_ITEM = "selected_transfer_item";
    public FragmentEquipmentInformationForEquipmentDashboardBinding binding;
    public Equipment selectedEquipment;

    /**
     * Creates project fragment for specific project ID
     */
    public static EquipmentInformationFragmentForEquipmentDashboard forSelectedTransferItem(TransferItem transferItem) {
        EquipmentInformationFragmentForEquipmentDashboard fragment = new EquipmentInformationFragmentForEquipmentDashboard();
        Bundle args = new Bundle();

        args.putSerializable(KEY_SELECTED_EQUIPMENT, transferItem.selectedEquipment);
        args.putSerializable(KEY_GROUP_CODE_INFORMATION, transferItem.groupCodeInformation);
        args.putSerializable(KEY_SELECTED_TRANSFER_ITEM, transferItem);
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_equipment_information_for_equipment_dashboard, container, false);
        return binding.getRoot();
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
        selectedEquipment.currentKmValue = Integer.valueOf(binding.carInformationLayout.kilometerValue.getText().toString());
        selectedEquipment.currentFuelValue = (int) binding.carInformationLayout.fuelRatingBar.rate.getRating();
        //FinePriceFragment finePriceFragment = FinePriceFragment.forSelectedContract(selectedContract);
        //super.changeFragment(finePriceFragment);
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
