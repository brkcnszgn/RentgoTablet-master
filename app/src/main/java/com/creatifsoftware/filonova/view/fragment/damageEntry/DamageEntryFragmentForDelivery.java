package com.creatifsoftware.filonova.view.fragment.damageEntry;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.creatifsoftware.filonova.BuildConfig;
import com.creatifsoftware.filonova.R;
import com.creatifsoftware.filonova.di.Injectable;
import com.creatifsoftware.filonova.model.ContractItem;
import com.creatifsoftware.filonova.model.DamageItem;
import com.creatifsoftware.filonova.utils.BlobStorageManager;
import com.creatifsoftware.filonova.utils.CommonMethods;
import com.creatifsoftware.filonova.view.adapter.DamageListAdapter;
import com.creatifsoftware.filonova.view.fragment.additionalphotos.AdditionaPhotoFragment;
import com.creatifsoftware.filonova.view.fragment.equipmentInformation.EquipmentInformationFragmentForDelivery;

import java.util.Locale;
import java.util.UUID;

/**
 * Created by kerembalaban on 16.02.2019 at 18:38.
 */
public class DamageEntryFragmentForDelivery extends DamageEntryFragment implements Injectable {
    public static DamageEntryFragmentForDelivery forSelectedContract(ContractItem selectedContract) {
        DamageEntryFragmentForDelivery fragment = new DamageEntryFragmentForDelivery();
        Bundle args = new Bundle();

        args.putSerializable(KEY_SELECTED_CONTRACT, selectedContract);
        args.putSerializable(KEY_SELECTED_EQUIPMENT, selectedContract.selectedEquipment);
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        damageListAdapter = new DamageListAdapter(damageItemClickCallback, damageItemDeleteButtonCallback, getSelectedContract().selectedEquipment, getSelectedContract().contractNumber, "delivery");
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_damage_entry, container, false);
        binding.damageList.setAdapter(damageListAdapter);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        selectedContract = getSelectedContract();
        binding.damageEntryTitle.setText(String.format(Locale.getDefault(), "%s - %s (%s)", getString(R.string.damage_entry_title), selectedContract.contractNumber, selectedContract.pnrNumber));
        getStepView().go(2, true);
    }

    @Override
    public void showEquipmentPartSelection() {
        mActivity.showEquipmentPartSelectionFragment(getGroupIdBySelectedPath(), true);
    }

    @Override
    protected void navigate() {

        if (CommonMethods.instance.getTakeAllPictures(requireContext())) {
            AdditionaPhotoFragment additionaPhotoFragment =
                    AdditionaPhotoFragment.forSelectedContract(selectedContract);
            super.changeFragment(additionaPhotoFragment);
        } else {
            EquipmentInformationFragmentForDelivery equipmentInformationFragment = EquipmentInformationFragmentForDelivery.forSelectedContract(selectedContract);
            super.changeFragment(equipmentInformationFragment);
        }

    }

    @Override
    public void addDamageItemToDamageList(DamageItem damageItem) {
        hasBlobStorageError = false;
        //String blobStorageUrl = ApplicationUtils.instance.getLiveSwitchIsChecked(getContext()) ? ConnectionUtils.instance.getLiveBlobStorageUrl() : ConnectionUtils.instance.getDevBlobStorageUrl();
        String blobStorageUrl = BuildConfig.BLOB_API_URL;
        damageItem.damageId = UUID.randomUUID().toString();
        String test = UUID.randomUUID().toString();
        damageItem.damageInfo.isNewDamage = true;
        damageItem.blobStoragePath = blobStorageUrl +
                "equipments/" +
                selectedContract.selectedEquipment.plateNumber.toLowerCase() +
                "/" +
                selectedContract.contractNumber.toLowerCase() +
                "/delivery/" +
                damageItem.damageId.toLowerCase();
        damageItem.blobStoragePathDocument = blobStorageUrl +
                "equipments/" +
                selectedContract.selectedEquipment.plateNumber.toLowerCase() +
                "/" +
                selectedContract.contractNumber.toLowerCase() +
                "/delivery/" +
                test.toLowerCase();

        //upload image
        Thread thread = new Thread(() -> {
            try {
                BlobStorageManager.instance.UploadImage(BlobStorageManager.instance.getEquipmentsContainerName(), damageItem.damagePhotoFile, getBlobImageName(damageItem));
                BlobStorageManager.instance.UploadImage(BlobStorageManager.instance.getEquipmentsContainerName(), damageItem.damagePhotoFileDocument, getBlobImageName(damageItem));
            } catch (Exception e) {
                hasBlobStorageError = true;
                super.showMessageDialog(e.getLocalizedMessage());
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
            selectedContract.selectedEquipment.damageList.add(0, damageItem);
            damageListAdapter.notifyDataSetChanged();
            drawDamageItemsToModelByEquipmentSubPart(damageItem.equipmentPart.equipmentSubPartId);
        }
    }

    private String getBlobImageName(DamageItem damageItem) {
        return BlobStorageManager.instance.prepareEquipmentImageName(selectedContract.selectedEquipment,
                selectedContract.contractNumber,
                "delivery",
                damageItem.damageId);
    }

    @Override
    public void deleteBlobImage(DamageItem damageItem) {
        //upload image
        new Thread(() -> {
            try {
                BlobStorageManager.instance.deleteImage(BlobStorageManager.instance.getEquipmentsContainerName(), getBlobImageName(damageItem));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
