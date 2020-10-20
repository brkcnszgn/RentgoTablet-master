package com.creatifsoftware.rentgoservice.view.fragment.equipmentInformation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import com.creatifsoftware.rentgoservice.R;
import com.creatifsoftware.rentgoservice.databinding.FragmentEquipmentInformationBinding;
import com.creatifsoftware.rentgoservice.di.Injectable;
import com.creatifsoftware.rentgoservice.model.Equipment;
import com.creatifsoftware.rentgoservice.model.EquipmentInventory;
import com.creatifsoftware.rentgoservice.model.GroupCodeInformation;
import com.creatifsoftware.rentgoservice.utils.BlobStorageManager;
import com.creatifsoftware.rentgoservice.utils.ImageUtil;
import com.creatifsoftware.rentgoservice.view.adapter.EquipmentInventoryListAdapter_v2;
import com.creatifsoftware.rentgoservice.view.callback.EquipmentInventoryAvailableCallback;
import com.creatifsoftware.rentgoservice.view.callback.EquipmentInventoryClickCallback;
import com.creatifsoftware.rentgoservice.view.callback.EquipmentInventoryMissingCallback;
import com.creatifsoftware.rentgoservice.view.fragment.base.BaseFragment;
import com.creatifsoftware.rentgoservice.viewmodel.DeliveryCarInformationViewModel;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import javax.inject.Inject;

import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;

/**
 * Created by kerembalaban on 16.02.2019 at 18:38.
 */
public class EquipmentInformationFragment extends BaseFragment implements Injectable {
    static final String KEY_SELECTED_EQUIPMENT = "selected_equipment";
    static final String KEY_GROUP_CODE_INFORMATION = "group_code_information";
    private static final int MY_CAMERA_PERMISSION_CODE = 102;
    private static final int REQUEST_KILOMETER_FUEL_CAPTURE_IMAGE = 103;
    private final EquipmentInventoryAvailableCallback equipmentInventoryAvailableCallback = new EquipmentInventoryAvailableCallback() {
        @Override
        public void onClick(EquipmentInventory inventory) {
            if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                inventory.isExist = true;
                inventory.isSelectedOnDelivery = "X";
                //equipmentInventoryListAdapter.notifyDataSetChanged();
            }
        }
    };
    private final EquipmentInventoryMissingCallback equipmentInventoryMissingCallback = new EquipmentInventoryMissingCallback() {
        @Override
        public void onClick(EquipmentInventory inventory) {
            if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                inventory.isExist = false;
                inventory.isSelectedOnDelivery = "X";
                //equipmentInventoryListAdapter.notifyDataSetChanged();
            }
        }
    };
    @SuppressLint("StaticFieldLeak")
    public FragmentEquipmentInformationBinding binding;
    boolean hasBlobStorageError = false;
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private DeliveryCarInformationViewModel viewModel;
    //private EquipmentInventoryListAdapter equipmentInventoryListAdapter;
    private EquipmentInventoryListAdapter_v2 equipmentInventoryListAdapter;
    private final EquipmentInventoryClickCallback equipmentInventoryClickCallback = new EquipmentInventoryClickCallback() {
        @Override
        public void onItemClick(EquipmentInventory inventory) {
            if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                inventory.isExist = !inventory.isExist;
                equipmentInventoryListAdapter.notifyDataSetChanged();
            }
        }
    };
    private Equipment selectedEquipment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_equipment_information, container, false);
        prepareRecyclerView();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(DeliveryCarInformationViewModel.class);
        //selectedContract = getSelectedContract();
        prepareComponents();
        observeEquipmentInventoryList();
    }

    private void prepareComponents() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            GroupCodeInformation groupCodeInformation = (GroupCodeInformation) bundle.getSerializable(KEY_GROUP_CODE_INFORMATION);
            selectedEquipment = (Equipment) bundle.getSerializable(KEY_SELECTED_EQUIPMENT);

            binding.setGroupCodeInformation(groupCodeInformation);
            binding.setSelectedEquipment(selectedEquipment);
            binding.carInformationLayout.fuelRatingBar.rate.setIsIndicator(false);
            binding.carInformationLayout.fuelRatingBar.rate.setOnRatingBarChangeListener
                    ((ratingBar, v, b) -> ratingBarChangeListener(ratingBar, selectedEquipment));

            binding.carInformationLayout.kilometerLayout.setOnClickListener(view ->
                    mActivity.showKilometerDialog(selectedEquipment));

            binding.carInformationLayout.kilometerFuelPhotoLayout.setOnClickListener(view -> openCamera());

            if (selectedEquipment.currentKmValue > 0) {
                binding.carInformationLayout.kilometerCheckbox.setChecked(true);
            }
            binding.carInformationLayout.currentFuelValue.setText(String.format(Locale.getDefault(), "%.0f", binding.carInformationLayout.fuelRatingBar.rate.getRating()));
        }

    }

    private void openCamera() {
        if (mActivity.checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    MY_CAMERA_PERMISSION_CODE);
        } else {
            Intent intent = ImageUtil.instance.dispatchTakePictureIntent(mActivity);
            startActivityForResult(intent, REQUEST_TAKE_PHOTO);
        }
    }

    public void ratingBarChangeListener(RatingBar ratingBar, Equipment equipment) {
        //binding.carInformationLayout.fuelCheckbox.setChecked(binding.carInformationLayout.fuelRatingBar.rate.getRating() == 8);
        binding.carInformationLayout.currentFuelValue.setText(String.format(Locale.getDefault(), "%.0f", ratingBar.getRating()));
        //equipment.currentFuelValue = (int)binding.carInformationLayout.fuelRatingBar.rate.getRating();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_TAKE_PHOTO) {
            File compressedImage = ImageUtil.instance.getImageFile();
            try {
                compressedImage = new Compressor(mActivity.getApplicationContext()).setQuality(50).compressToFile(ImageUtil.instance.getImageFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
            //File imageFile = ImageUtil.instance.getImageFile();
            Picasso.get().load(compressedImage).into(binding.carInformationLayout.kilometerFuelImage);
            binding.carInformationLayout.kilometerFuelPhotoCheckbox.setChecked(true);
            selectedEquipment.kilometerFuelImageFile = compressedImage;
        }
    }

    boolean uploadKilometerFuelImage(String type) {
        hasBlobStorageError = false;
        super.showLoading();
        Thread thread = new Thread(() -> {
            try {
                String imageName = BlobStorageManager.instance.prepareEquipmentImageName(selectedEquipment, selectedContract.contractNumber, type, "kilometer_fuel_image");
                BlobStorageManager.instance.UploadImage(BlobStorageManager.instance.getEquipmentsContainerName(), selectedEquipment.kilometerFuelImageFile, imageName);

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
        return !hasBlobStorageError;
    }

    private void observeEquipmentInventoryList() {
        if (selectedEquipment.inventoryList != null) {
            //prepareToggleButtonsForInventories();
            equipmentInventoryListAdapter.setEquipmentInventoryList(selectedEquipment.inventoryList);
            return;
        }
        showLoading();
        viewModel.setEquipmentId(selectedEquipment.equipmentId);
        viewModel.getEquipmentInventoryListObservable().observe(this, response -> {
            super.hideLoading();
            if (response == null) {
                showMessageDialog(getString(R.string.unknown_error_message));
            } else if (!response.responseResult.result) {
                showMessageDialog(response.responseResult.exceptionDetail);
            } else {
                selectedEquipment.inventoryList = response.equipmentInventoryData;
                //prepareToggleButtonsForInventories();
                equipmentInventoryListAdapter.setEquipmentInventoryList(selectedEquipment.inventoryList);
            }
        });
    }

    private void prepareRecyclerView() {
        //equipmentInventoryListAdapter = new EquipmentInventoryListAdapter(equipmentInventoryClickCallback);
        equipmentInventoryListAdapter = new EquipmentInventoryListAdapter_v2(equipmentInventoryAvailableCallback, equipmentInventoryMissingCallback);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        binding.equipmentInventoryList.setLayoutManager(layoutManager);
        binding.equipmentInventoryList.setAdapter(equipmentInventoryListAdapter);
        //binding.equipmentInventoryList.addItemDecoration(new DividerItemDecoration(binding.equipmentInventoryList.getContext(), DividerItemDecoration.HORIZONTAL));
    }

    @Override
    public void kilometerSelected(String kilometer) {
        binding.carInformationLayout.kilometerCheckbox.setChecked(true);
        binding.carInformationLayout.kilometerValue.setText(kilometer);
    }
}


