package com.creatifsoftware.filonova.view.fragment.damageEntry;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.creatifsoftware.filonova.BuildConfig;
import com.creatifsoftware.filonova.R;
import com.creatifsoftware.filonova.di.Injectable;
import com.creatifsoftware.filonova.model.DamageItem;
import com.creatifsoftware.filonova.model.TransferItem;
import com.creatifsoftware.filonova.utils.BlobStorageManager;
import com.creatifsoftware.filonova.utils.EnumUtils;
import com.creatifsoftware.filonova.view.adapter.DamageListAdapter;
import com.creatifsoftware.filonova.view.fragment.base.BaseFragment;
import com.creatifsoftware.filonova.view.fragment.equipmentInformation.EquipmentInformationFragmentForTransfer;
import com.creatifsoftware.filonova.view.fragment.transfers.RepairedDamageListFragment;

import java.util.Locale;
import java.util.UUID;

import javax.inject.Inject;

/**
 * Created by kerembalaban on 16.02.2019 at 18:38.
 */
public class DamageEntryFragmentForTransferReturn extends DamageEntryFragment implements Injectable {
    @Inject
    ViewModelProvider.Factory viewModelFactory;

    public static DamageEntryFragmentForTransferReturn forSelectedTransfer(TransferItem transferItem) {
        DamageEntryFragmentForTransferReturn fragment = new DamageEntryFragmentForTransferReturn();
        Bundle args = new Bundle();

        args.putSerializable(KEY_SELECTED_TRANSFER, transferItem);
        args.putSerializable(KEY_SELECTED_EQUIPMENT, transferItem.selectedEquipment);
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        damageListAdapter = new DamageListAdapter(damageItemClickCallback, damageItemDeleteButtonCallback, getSelectedTransfer().selectedEquipment, getSelectedTransfer().transferNumber, "delivery");
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_damage_entry, container, false);
        binding.damageList.setAdapter(damageListAdapter);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        selectedTransfer = getSelectedTransfer();
        getStepView().go(1, true);
        binding.damageEntryTitle.setText(String.format(Locale.getDefault(), "%s - %s", getString(R.string.damage_entry_title), selectedTransfer.transferNumber));
    }

    @Override
    public void showEquipmentPartSelection() {
        mActivity.showEquipmentPartSelectionFragment(getGroupIdBySelectedPath(), true);
    }

    @Override
    public void navigate() {
        BaseFragment nextFragment;
        if (selectedTransfer.transferType == EnumUtils.TransferType.DAMAGE.getIntValue() &&
                selectedTransfer.statusCode == EnumUtils.TransferStatusCode.TRANSFERRED.getIntValue()) {
            nextFragment = RepairedDamageListFragment.forSelectedTransfer(selectedTransfer);
        } else {
            nextFragment = EquipmentInformationFragmentForTransfer.forSelectedTransfer(selectedTransfer);
        }
        super.changeFragment(nextFragment);
    }

    @Override
    public void addDamageItemToDamageList(DamageItem damageItem) {
        hasBlobStorageError = false;
        //String blobStorageUrl = ApplicationUtils.instance.getLiveSwitchIsChecked(getContext()) ? ConnectionUtils.instance.getLiveBlobStorageUrl() : ConnectionUtils.instance.getDevBlobStorageUrl();

        String blobStorageUrl = BuildConfig.BLOB_API_URL;
        damageItem.damageId = UUID.randomUUID().toString();
        damageItem.damageInfo.isNewDamage = true;
        damageItem.blobStoragePath = blobStorageUrl +
                "equipments/" +
                selectedTransfer.selectedEquipment.plateNumber.toLowerCase() +
                "/" +
                selectedTransfer.transferNumber.toLowerCase() +
                "/return/" +
                damageItem.damageId.toLowerCase();

        Thread thread = new Thread(() -> {
            try {
                BlobStorageManager.instance.UploadImage(BlobStorageManager.instance.getEquipmentsContainerName(), damageItem.damagePhotoFile, getBlobImageName(damageItem));
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
            selectedTransfer.selectedEquipment.damageList.add(0, damageItem);
            damageListAdapter.notifyDataSetChanged();
            drawDamageItemsToModelByEquipmentSubPart(damageItem.equipmentPart.equipmentSubPartId);
        }
    }

    private String getBlobImageName(DamageItem damageItem) {
        return BlobStorageManager.instance.prepareEquipmentImageName(selectedTransfer.selectedEquipment,
                selectedTransfer.transferNumber,
                "return",
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
