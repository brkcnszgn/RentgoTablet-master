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
import com.creatifsoftware.filonova.view.fragment.additionalphotos.AdditionaPhotoRentalFragment;
import com.creatifsoftware.filonova.view.fragment.equipmentInformation.EquipmentInformationFragmentForRental;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by kerembalaban on 16.02.2019 at 18:38.
 */
public class DamageEntryFragmentForRental extends DamageEntryFragment implements Injectable {
    public static DamageEntryFragmentForRental forSelectedContract(ContractItem selectedContract) {
        DamageEntryFragmentForRental fragment = new DamageEntryFragmentForRental();
        Bundle args = new Bundle();

        args.putSerializable(KEY_SELECTED_CONTRACT, selectedContract);
        args.putSerializable(KEY_SELECTED_EQUIPMENT, selectedContract.selectedEquipment);
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        damageListAdapter = new DamageListAdapter(damageItemClickCallback, damageItemDeleteButtonCallback, getSelectedContract().selectedEquipment, getSelectedContract().contractNumber, "rental");
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_damage_entry, container, false);
        binding.damageList.setAdapter(damageListAdapter);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        selectedContract = getSelectedContract();
        binding.damageEntryTitle.setText(String.format(Locale.getDefault(), "%s - %s (%s)", getString(R.string.damage_entry_title), selectedContract.contractNumber, selectedContract.pnrNumber));
        getStepView().go(1, true);
    }

    @Override
    public void showEquipmentPartSelection() {
        mActivity.showEquipmentPartSelectionFragment(getGroupIdBySelectedPath(), false);
    }

    @Override
    public void navigate() {
        if (CommonMethods.instance.getTakeAllPictures(requireContext())) {
            AdditionaPhotoRentalFragment additionaPhotoRentalFragment =
                    AdditionaPhotoRentalFragment.forSelectedContract(selectedContract);
            super.changeFragment(additionaPhotoRentalFragment);
        } else {
            EquipmentInformationFragmentForRental carInformationForRentalFragment = EquipmentInformationFragmentForRental.forSelectedContract(selectedContract);
            super.changeFragment(carInformationForRentalFragment);
        }
    }

    @Override
    public void addDamageItemToDamageList(DamageItem damageItem) {
        hasBlobStorageError = false;
//        String blobStorageUrl = ApplicationUtils.instance.getLiveSwitchIsChecked(getContext()) ?
//                                    ConnectionUtils.instance.getLiveBlobStorageUrl() :
//                                        ConnectionUtils.instance.getDevBlobStorageUrl();

        String blobStorageUrl = BuildConfig.BLOB_API_URL;

        damageItem.damageId = UUID.randomUUID().toString();
        damageItem.damageInfo.isNewDamage = true;
        damageItem.blobStoragePath = blobStorageUrl +
                "equipments/" +
                selectedContract.selectedEquipment.plateNumber.toLowerCase() +
                "/" +
                selectedContract.contractNumber.toLowerCase() +
                "/rental/" +
                damageItem.damageId.toLowerCase();
        int index=1;
        for (File doc : damageItem.damagePhotoFileDocument) {
            damageItem.blobStoragePathDocument.add(
                   UUID.randomUUID().toString().toLowerCase()
            );
            index++;
        }

        Thread thread = new Thread(() -> {
            try {
                BlobStorageManager.instance.UploadImage(BlobStorageManager.instance.getEquipmentsContainerName(), damageItem.damagePhotoFile, getBlobImageName(damageItem));
                int row=0;
                for (File doc:damageItem.damagePhotoFileDocument) {
                    BlobStorageManager.instance.UploadImage(BlobStorageManager.instance.getEquipmentsContainerName(), doc, getBlobImageName(damageItem,row));
                    row++;
                }

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
            Picasso.get().load(damageItem.damagePhotoFileDocument.get(0)).into(binding.imgTest);
        }
    }

    private String getBlobImageName(DamageItem damageItem) {
        return BlobStorageManager.instance.prepareEquipmentImageName(selectedContract.selectedEquipment,
                selectedContract.contractNumber,
                "rental",
                damageItem.damageId);
    }

    private String getBlobImageName(DamageItem damageItem,int index) {
        return BlobStorageManager.instance.prepareEquipmentImageName(selectedContract.selectedEquipment,
                selectedContract.contractNumber,
                "rental",
                damageItem.blobStoragePathDocument.get(index)
                );
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
