package com.creatifsoftware.rentgoservice.view.fragment.contractSummary;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.creatifsoftware.rentgoservice.R;
import com.creatifsoftware.rentgoservice.di.Injectable;
import com.creatifsoftware.rentgoservice.model.ContractItem;
import com.creatifsoftware.rentgoservice.model.DamageItem;
import com.creatifsoftware.rentgoservice.model.User;
import com.creatifsoftware.rentgoservice.utils.BlobStorageManager;

import java.util.Locale;

/**
 * Created by kerembalaban on 12.02.2019 at 22:23.
 */
public class ContractSummaryForRentalFragment extends ContractSummaryFragment implements Injectable {

    public static ContractSummaryForRentalFragment forSelectedContract(ContractItem selectedContract) {
        ContractSummaryForRentalFragment fragment = new ContractSummaryForRentalFragment();
        Bundle args = new Bundle();

        args.putSerializable(KEY_SELECTED_CONTRACT, selectedContract);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getStepView().go(4, true);
    }

    @Override
    public void navigate() {
        super.navigate();
        if (documentPhotoUploaded) {
            prepareConfirmationDialog();
        } else {
            confirmationButtonKey = OPEN_CAMERA_KEY;
            mActivity.showConfirmationDialog(getString(R.string.dialog_button_cancel), "Fotoğraf Çek", "İmzalı iade formunun fotoğrafı çekilmelidir");
        }
    }

    private void prepareConfirmationDialog() {
        confirmationButtonKey = UPDATE_CONTRACT_KEY;
        String message;
        String title;
        String selectedPlate;
        String amountoBePaid = "";
        String depositDifference = "";

        title = String.format(Locale.getDefault(), "%s numaralı sözleşme aşağıdaki bilgiler ile kapatılacaktır.\n", selectedContract.contractNumber);
        selectedPlate = String.format(Locale.getDefault(), "\nİade Alınacak Plaka : %s", selectedContract.selectedEquipment.plateNumber);

        if (getTotalPaymentAmount() > 0) {
            amountoBePaid = String.format(Locale.getDefault(), "\nÖdenecek Tutar : %.02f TL", getTotalPaymentAmount());
        }
        if (getTotalPaymentAmount() < 0) {
            amountoBePaid = String.format(Locale.getDefault(), "\nİade  Edilecek Tutar : %.02f TL", getTotalPaymentAmount());
        }

        message = title + selectedPlate + amountoBePaid;

        selectedContract.hasPaymentError = false;
        mActivity.showConfirmationDialog(getString(R.string.dialog_button_cancel), getString(R.string.dialog_button_yes), message);
    }

    @Override
    public void confirmationButtonClicked() {
        if (confirmationButtonKey.equals(OPEN_CAMERA_KEY)) {
            openCamera(RENTAL_DOCUMENT_PHOTO_REQUEST_CODE);
        } else if (confirmationButtonKey.equals(UPDATE_CONTRACT_KEY)) {
            updateContract();
        }
    }

    private void updateContract() {
        super.showLoading();
        viewModel.setContractItem(selectedContract);
        viewModel.setUser(new User().getUser(getContext()));
        viewModel.getUpdateContractForRentalObservable().observe(getViewLifecycleOwner(), response -> {
            super.hideLoading();
            if (response == null) {
                showMessageDialog(getString(R.string.unknown_error_message));
            } else if (response.responseResult.result) {
                isServiceSuccess = true;
                //uploadDamagePhotos();
                //uploadKilometerFuelImage();
                mActivity.showMessageDialog("Sözleşmeniz başarıyla güncellenmiştir");
            } else if (response.hasPaymentError) {
                selectedContract.hasPaymentError = true;
                mActivity.showConfirmationDialog("Yeni Kart", "Borç ile kapat", response.responseResult.exceptionDetail);
            } else {
                showMessageDialog(response.responseResult.exceptionDetail);
            }
        });
    }

    @Override
    public void uploadPhoto() {
        Thread thread = new Thread(() -> {
            try {
                String imageName = BlobStorageManager.instance.prepareEquipmentImageName(selectedContract.selectedEquipment, selectedContract.contractNumber, "rental", "rental_signed_document");
                BlobStorageManager.instance.UploadImage(BlobStorageManager.instance.getEquipmentsContainerName(), compressedImage, imageName);

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
            documentPhotoUploaded = true;
            prepareConfirmationDialog();
        }
    }

    private void uploadDamagePhotos() {
        Thread thread = new Thread(() -> {
            try {
                for (DamageItem damageItem : selectedContract.selectedEquipment.damageList) {
                    String imageName = BlobStorageManager.instance.prepareEquipmentImageName(selectedContract.selectedEquipment, selectedContract.contractNumber, "rental", damageItem.damageId);
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
                String imageName = BlobStorageManager.instance.prepareEquipmentImageName(selectedContract.selectedEquipment, selectedContract.contractNumber, "rental", "kilometer_fuel_image");
                BlobStorageManager.instance.UploadImage(BlobStorageManager.instance.getEquipmentsContainerName(), selectedContract.selectedEquipment.kilometerFuelImageFile, imageName);

            } catch (Exception e) {
                //mActivity.showMessageDialog(e.getLocalizedMessage());
                e.printStackTrace();
            }
        });

        thread.start();
    }
}
