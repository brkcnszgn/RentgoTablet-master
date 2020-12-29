package com.creatifsoftware.filonova.view.fragment.contractSummary;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.creatifsoftware.filonova.R;
import com.creatifsoftware.filonova.di.Injectable;
import com.creatifsoftware.filonova.model.ContractItem;
import com.creatifsoftware.filonova.model.CreditCard;
import com.creatifsoftware.filonova.model.DamageItem;
import com.creatifsoftware.filonova.model.User;
import com.creatifsoftware.filonova.utils.BlobStorageManager;
import com.creatifsoftware.filonova.utils.CommonMethods;
import com.creatifsoftware.filonova.utils.EnumUtils;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by kerembalaban on 12.02.2019 at 22:23.
 */
public class ContractSummaryForDeliveryFragment extends ContractSummaryFragment implements Injectable {

    public static ContractSummaryForDeliveryFragment forSelectedContract(ContractItem selectedContract) {
        ContractSummaryForDeliveryFragment fragment = new ContractSummaryForDeliveryFragment();
        Bundle args = new Bundle();

        args.putSerializable(KEY_SELECTED_CONTRACT, selectedContract);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getStepView().go(7, true);
    }

    @Override
    public void navigate() {
        super.navigate();
        boolean showDepositCardFragment;
        showDepositCardFragment = selectedContract.customer.paymentMethod != EnumUtils.PaymentMethod.CURRENT.getIntValue() &&
                selectedContract.customer.paymentMethod != EnumUtils.PaymentMethod.FULL_CREDIT.getIntValue();

        if (getDepositAmountDifference() > 0 &&
                showDepositCardFragment &&
                (selectedContract.updatedGroupCodeInformation != null &&
                        CommonMethods.instance.getSelectedGroupCodeInformation(selectedContract.updatedGroupCodeInformation.groupCodeId).isDoubleCard)) {
            mActivity.showDepositCreditCardFragment(selectedContract, getDepositAmountDifference());
        } else {
            if (documentPhotoUploaded) {
                prepareConfirmationDialog();
            } else {
                confirmationButtonKey = OPEN_CAMERA_KEY;
                mActivity.showConfirmationDialog(getString(R.string.dialog_button_cancel), "Fotoğraf Çek", "İmzalı teslim formunun fotoğrafı çekilmelidir");
            }
        }
    }

    @Override
    public void addCreditCard(CreditCard creditCard) {
        super.addCreditCard(creditCard);
    }

    @Override
    public void addDepositCreditCard(CreditCard creditCard) {
        selectedDepositCard = creditCard;
        prepareConfirmationDialog();
    }

    private void prepareConfirmationDialog() {
        confirmationButtonKey = UPDATE_CONTRACT_KEY;
        String message;
        String title;
        String selectedPlate;
        String amountoBePaid = "";
        String depositDifference = "";

        title = String.format(Locale.getDefault(), "%s numaralı sözleşme aşağıdaki bilgiler ile güncellenecektir.\n", selectedContract.contractNumber);
        selectedPlate = String.format(Locale.getDefault(), "\nTeslim Edilecek Plaka : %s", selectedContract.selectedEquipment.plateNumber);

        if (getTotalPaymentAmount() > 0) {
            if (selectedContract.customer.paymentMethod == EnumUtils.PaymentMethod.CURRENT.getIntValue()) {
                amountoBePaid = "Cari sözleşmedir, ödeme alınmayacaktır";
            } else if (selectedContract.customer.paymentMethod == EnumUtils.PaymentMethod.FULL_CREDIT.getIntValue()) {
                amountoBePaid = "Full credit sözleşmedir, ödeme alınmayacaktır";
            } else {
                amountoBePaid = String.format(Locale.getDefault(), "\nÖdenecek Tutar : %.02f TL", getTotalPaymentAmount());
            }
        }
        if (getTotalPaymentAmount() < 0) {
            if (selectedContract.customer.paymentMethod == EnumUtils.PaymentMethod.CURRENT.getIntValue()) {
                amountoBePaid = "Cari sözleşmedir, ödeme alınmayacaktır";
            } else if (selectedContract.customer.paymentMethod == EnumUtils.PaymentMethod.FULL_CREDIT.getIntValue()) {
                amountoBePaid = "Full credit sözleşmedir, ödeme alınmayacaktır";
            } else {
                amountoBePaid = String.format(Locale.getDefault(), "\nİade  Edilecek Tutar : %.02f TL", getTotalPaymentAmount());
            }
        }
        if (getDepositAmountDifference() > 0) {
            if (selectedContract.customer.paymentMethod == EnumUtils.PaymentMethod.CURRENT.getIntValue()) {
                depositDifference = "Cari sözleşmedir, ödeme alınmayacaktır";
            } else if (selectedContract.customer.paymentMethod == EnumUtils.PaymentMethod.FULL_CREDIT.getIntValue()) {
                depositDifference = "Full credit sözleşmedir, ödeme alınmayacaktır";
            } else {
                depositDifference = String.format(Locale.getDefault(), "\nÖdenecek Teminat Tutarı : %.02f TL", getDepositAmountDifference());
            }
        }
        if (getDepositAmountDifference() < 0) {
            if (selectedContract.customer.paymentMethod == EnumUtils.PaymentMethod.CURRENT.getIntValue()) {
                depositDifference = "Cari sözleşmedir, ödeme alınmayacaktır";
            } else if (selectedContract.customer.paymentMethod == EnumUtils.PaymentMethod.FULL_CREDIT.getIntValue()) {
                depositDifference = "Full credit sözleşmedir, ödeme alınmayacaktır";
            } else {
                depositDifference = String.format(Locale.getDefault(), "\nİade  Edilecek Teminat Tutarı : %.02f TL", getDepositAmountDifference());
            }
        }

        message = title + selectedPlate + amountoBePaid + depositDifference;
        mActivity.showConfirmationDialog(getString(R.string.dialog_button_cancel), getString(R.string.dialog_button_yes), message);
    }

    @Override
    public void confirmationButtonClicked() {
        if (confirmationButtonKey.equals(OPEN_CAMERA_KEY)) {
            openCamera(DELIVERY_DOCUMENT_PHOTO_REQUEST_CODE);
        } else if (confirmationButtonKey.equals(UPDATE_CONTRACT_KEY)) {
            updateContract();
        }
    }

    private void updateContract() {
        super.showLoading();
        viewModel.setContractItem(selectedContract);
        viewModel.setUser(new User().getUser(getContext()));
        ArrayList<CreditCard> creditCards = new ArrayList<>();
        if (selectedPaymentCard != null) {
            selectedPaymentCard.cardType = EnumUtils.TransactionType.SALE.getIntValue();
            creditCards.add(selectedPaymentCard);
        }
        if (selectedDepositCard != null) {
            selectedDepositCard.cardType = EnumUtils.TransactionType.DEPOSIT.getIntValue();
            creditCards.add(selectedDepositCard);
        }
        viewModel.getUpdateContractForDeliveryObservable(creditCards).observe(getViewLifecycleOwner(), baseResponse -> {
            super.hideLoading();
            if (baseResponse == null) {
                showMessageDialog(getString(R.string.unknown_error_message));
            } else if (baseResponse.responseResult.result) {
                isServiceSuccess = true;
                //uploadDamagePhotos();
                //uploadKilometerFuelImage();
                mActivity.showMessageDialog(getString(R.string.delivery_contract_dialog_success_message));
            } else {
                showMessageDialog(baseResponse.responseResult.exceptionDetail);
            }
        });
    }

    @Override
    public void uploadPhoto() {
        Thread thread = new Thread(() -> {
            try {
                String imageName = BlobStorageManager.instance.prepareEquipmentImageName(selectedContract.selectedEquipment, selectedContract.contractNumber, "delivery", "delivery_signed_document");
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
                    String imageName = BlobStorageManager.instance.prepareEquipmentImageName(selectedContract.selectedEquipment, selectedContract.contractNumber, "delivery", damageItem.damageId);
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
                String imageName = BlobStorageManager.instance.prepareEquipmentImageName(selectedContract.selectedEquipment, selectedContract.contractNumber, "delivery", "kilometer_fuel_image");
                BlobStorageManager.instance.UploadImage(BlobStorageManager.instance.getEquipmentsContainerName(), selectedContract.selectedEquipment.kilometerFuelImageFile, imageName);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        thread.start();
    }
}
