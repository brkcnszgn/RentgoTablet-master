package com.creatifsoftware.rentgoservice.view.fragment.contractSummary;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;

import com.creatifsoftware.rentgoservice.R;
import com.creatifsoftware.rentgoservice.databinding.FragmentContractSummaryBinding;
import com.creatifsoftware.rentgoservice.di.Injectable;
import com.creatifsoftware.rentgoservice.model.AdditionalProduct;
import com.creatifsoftware.rentgoservice.model.CreditCard;
import com.creatifsoftware.rentgoservice.model.PaymentInfo;
import com.creatifsoftware.rentgoservice.model.base.ResponseResult;
import com.creatifsoftware.rentgoservice.utils.EnumUtils;
import com.creatifsoftware.rentgoservice.utils.ImageUtil;
import com.creatifsoftware.rentgoservice.utils.SharedPrefUtils;
import com.creatifsoftware.rentgoservice.view.adapter.CreditCardListAdapter;
import com.creatifsoftware.rentgoservice.view.adapter.DamageListAdapter;
import com.creatifsoftware.rentgoservice.view.adapter.PaymentInfoListAdapter;
import com.creatifsoftware.rentgoservice.view.callback.CreditCardClickCallback;
import com.creatifsoftware.rentgoservice.view.fragment.base.BaseFragment;
import com.creatifsoftware.rentgoservice.viewmodel.ContractSummaryViewModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;

/**
 * Created by kerembalaban on 12.02.2019 at 22:23.
 */
public class ContractSummaryFragment extends BaseFragment implements Injectable {
    public static final String TAG = "ProjectListFragment";
    static final int DELIVERY_DOCUMENT_PHOTO_REQUEST_CODE = 200;
    static final int RENTAL_DOCUMENT_PHOTO_REQUEST_CODE = 300;
    static final String OPEN_CAMERA_KEY = "open_camera";
    static final String UPDATE_CONTRACT_KEY = "update_contract_key";
    public ContractSummaryViewModel viewModel;
    CreditCard selectedPaymentCard;
    CreditCard selectedDepositCard;
    boolean isServiceSuccess = false;
    boolean hasBlobStorageError = false;
    boolean documentPhotoUploaded = false;
    File compressedImage;
    String confirmationButtonKey;
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private CreditCardListAdapter paymentCardListAdapter;
    private final CreditCardClickCallback paymentCardClickCallback = new CreditCardClickCallback() {
        @Override
        public void onClick(CreditCard creditCard) {
            if (selectedContract.customer.paymentMethod == EnumUtils.PaymentMethod.CURRENT.getIntValue()) {
                mActivity.showMessageDialog("Cari sözleşmedir, kart seçilemez");
            } else if (selectedContract.customer.paymentMethod == EnumUtils.PaymentMethod.FULL_CREDIT.getIntValue()) {
                mActivity.showMessageDialog("Full credit sözleşmedir, ödeme alınmayacaktır");
            } else {
                for (CreditCard item : selectedContract.customer.cardList) {
                    item.isSelected = false;
                }
                creditCard.isSelected = !creditCard.isSelected;
                selectedPaymentCard = creditCard.isSelected ? creditCard : null;
                paymentCardListAdapter.notifyDataSetChanged();
            }

        }
    };
    private CreditCardListAdapter depositCardListAdapter;
    private FragmentContractSummaryBinding binding;

    @androidx.annotation.Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @androidx.annotation.Nullable ViewGroup container,
                             @androidx.annotation.Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_contract_summary, container, false);
        selectedContract = getSelectedContract();
        preparePaymentInfoRecyclerView();
        prepareDamagesRecyclerView();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this,
                viewModelFactory).get(ContractSummaryViewModel.class);

        binding.setContract(selectedContract);
        if (getTotalPaymentAmount() < 0) {
            binding.totalPaymentInfoTitle.setText(getString(R.string.total_refund_title));
        } else {
            binding.totalPaymentInfoTitle.setText(getString(R.string.total_payment_title));
        }
        if (selectedContract.customer.paymentMethod == EnumUtils.PaymentMethod.CURRENT.getIntValue()) {
            binding.totalPaymentInfoAmount.setText("Cari sözleşmedir, ödeme alınmayacaktır");
        } else if (selectedContract.customer.paymentMethod == EnumUtils.PaymentMethod.FULL_CREDIT.getIntValue()) {
            binding.totalPaymentInfoAmount.setText("Full credit sözleşmedir, ödeme alınmayacaktır");
        } else {
            binding.totalPaymentInfoAmount.setText(String.format(Locale.getDefault(), "%.02f TL", getTotalPaymentAmount()));
        }


        observeCustomerCreditCards();
    }

    private void observeCustomerCreditCards() {
        selectedContract.customer.cardList = new ArrayList<>();
        viewModel.setCustomerId(selectedContract.customer.customerId);
        viewModel.getCreditCardsObservable().observe(getViewLifecycleOwner(), creditCardsResponse -> {
            if (creditCardsResponse.responseResult.result) {
                if (creditCardsResponse.creditCardList.size() > 0) {
                    selectedContract.customer.cardList = creditCardsResponse.creditCardList;
                }
            }
            prepareCreditCardRecyclerView();
        });
    }

//    private void prepareCreditCardRecyclerView(){
//        paymentCardListAdapter = new CreditCardListAdapter(paymentCardClickCallback);
//        depositCardListAdapter = new CreditCardListAdapter(depositCardClickCallback);
//
//        paymentCardListAdapter.setCardList(selectedContract.customer.cardList);
//        binding.paymentCard.creditCardList.setAdapter(paymentCardListAdapter);
//        binding.paymentCard.setAddNewCard(true);
//        binding.paymentCard.setTitle(getString(R.string.payment_info));
//        binding.paymentCard.addCardLayout.setOnClickListener(view -> mActivity.showCreditCardFragment(false));
//
//        if (getDepositAmountDifference() > 0 && (selectedContract.updatedGroupCodeInformation != null && selectedContract.updatedGroupCodeInformation.isDoubleCard)){
//            binding.depositCard.cardLayout.setVisibility(View.VISIBLE);
//            depositCardListAdapter.setCardList(selectedContract.customer.cardList);
//            binding.depositCard.creditCardList.setAdapter(depositCardListAdapter);
//            binding.depositCard.setAddNewCard(true);
//            binding.depositCard.setTitle(getString(R.string.deposit_info));
//            binding.depositCard.addCardLayout.setOnClickListener(view -> mActivity.showCreditCardFragment(true));
//        }else{
//            binding.depositCard.cardLayout.setVisibility(View.INVISIBLE);
//        }
//    }

    private void prepareCreditCardRecyclerView() {
        paymentCardListAdapter = new CreditCardListAdapter(paymentCardClickCallback);
        paymentCardListAdapter.setCardList(selectedContract.customer.cardList);
        binding.paymentCard.creditCardList.setAdapter(paymentCardListAdapter);
        binding.paymentCard.setTitle(getString(R.string.payment_info));
        binding.paymentCard.setAddNewCard(true);
        binding.paymentCard.addCardLayout.setOnClickListener(view -> mActivity.showCreditCardFragment());
    }
//
//    private final CreditCardClickCallback depositCardClickCallback = new CreditCardClickCallback() {
//        @Override
//        public void onClick(CreditCard creditCard) {
//            if (creditCard.creditCardNumber == selectedPaymentCard.creditCardNumber){
//                showMessageDialog("Seçmiş olduğunuz araç çift kredi kartı istemektedir! \n\nÖdeme yapılan karttan farklı bir seçim yapılmalıdır yada yeni bir kart girilmelidir");
//                return;
//            }
//
//            for (CreditCard item : selectedContract.customer.cardList){
//                item.isSelected = false;
//            }
//            creditCard.isSelected = !creditCard.isSelected;
//            selectedDepositCard = creditCard.isSelected ? creditCard : null;
//            depositCardListAdapter.notifyDataSetChanged();
//        }
//    };

    @Override
    protected ResponseResult checkBeforeNavigate() {
        // if need payment, then check if card is selected
        if (selectedContract.customer.paymentMethod == EnumUtils.PaymentMethod.CURRENT.getIntValue() ||
                selectedContract.customer.paymentMethod == EnumUtils.PaymentMethod.FULL_CREDIT.getIntValue()) {
            return super.checkBeforeNavigate();
        } else if (getTotalPaymentAmount() > 0 && selectedPaymentCard == null) {
            return new ResponseResult(false, "Ödenecek tutar mevcut, lütfen kart seçimi yapın");
        }

//        if (getDepositAmountDifference() > 0 && (selectedContract.updatedGroupCodeInformation != null && selectedContract.updatedGroupCodeInformation.isDoubleCard) && selectedDepositCard == null){
//            return new ResponseResult(false,"Ödenecek teminat mevcut, lütfen yeni kart seçimi yapın");
//        }

        return super.checkBeforeNavigate();
    }

    double getTotalPaymentAmount() {
        double totalAmount = 0;
        for (PaymentInfo paymentInfo : getPaymentInfoList()) {
            totalAmount += paymentInfo.paymentInfoAmount * paymentInfo.value;
        }

        return totalAmount;
    }

    void prepareTakedocumentPhotoDialog() {

    }

    @Override
    public void addCreditCard(CreditCard creditCard) {
        for (CreditCard item : selectedContract.customer.cardList) {
            item.isSelected = false;
        }
        creditCard.isSelected = true;
        selectedPaymentCard = creditCard;
        selectedContract.customer.cardList.add(creditCard);
        paymentCardListAdapter.setCardList(selectedContract.customer.cardList);
    }

    @Override
    public void messageDialogDoneButtonClicked() {
        if (isServiceSuccess) {
            SharedPrefUtils.instance.saveObject(getContext(), "contract_number", selectedContract.contractNumber);
            popBackStackTillEntry();
        }
    }

    private void popBackStackTillEntry() {
        int entryIndex = 0;
        if (mActivity.getSupportFragmentManager().getBackStackEntryCount() <= entryIndex) {
            return;
        }
        FragmentManager.BackStackEntry entry = mActivity.getSupportFragmentManager().getBackStackEntryAt(
                entryIndex);
        mActivity.getSupportFragmentManager().popBackStackImmediate(entry.getId(),
                FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    private void preparePaymentInfoRecyclerView() {
        PaymentInfoListAdapter paymentInfoListAdapter = new PaymentInfoListAdapter();
        paymentInfoListAdapter.setPaymentInfoList(getPaymentInfoList());
        binding.paymentInfoList.setAdapter(paymentInfoListAdapter);
        binding.paymentInfoList.addItemDecoration(new DividerItemDecoration(binding.paymentInfoList.getContext(), DividerItemDecoration.VERTICAL));
    }

    private void prepareDamagesRecyclerView() {
        DamageListAdapter damageListAdapter = new DamageListAdapter(null, null, selectedContract.selectedEquipment, selectedContract.contractNumber, "delivery");
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 4);
        damageListAdapter.setDamageList(selectedContract.selectedEquipment.damageList);
        binding.damageList.setLayoutManager(layoutManager);
        binding.damageList.setAdapter(damageListAdapter);
    }

    private List<PaymentInfo> getPaymentInfoList() {
        List<PaymentInfo> list = new ArrayList<>();

        // if there is a added additional product, add to payment info list
        if (selectedContract.addedAdditionalProducts != null) {
            for (AdditionalProduct item : selectedContract.addedAdditionalProducts) {
                list.add(new PaymentInfo(item.productName, item.actualTotalAmount, item.value));
            }
        }


        // if there is a added additional services, add to payment info list
        if (selectedContract.addedAdditionalServices != null) {
            for (AdditionalProduct item : selectedContract.addedAdditionalServices) {
                list.add(new PaymentInfo(item.productName, item.actualTotalAmount, item.value));
            }
        }

        // if there is a extra payment, add to payment info list
        if (selectedContract.extraPaymentList != null) {
            for (AdditionalProduct item : selectedContract.extraPaymentList) {
                list.add(new PaymentInfo(item.productName, item.tobePaidAmount, item.value));
            }
        }

        // if there is price difference from car return by date
        if (selectedContract.carDifferenceAmount > 0) {
            list.add(new PaymentInfo("Geç Getirme Bedeli", selectedContract.carDifferenceAmount, 1));
        }

        if (selectedContract.carDifferenceAmount < 0) {
            list.add(new PaymentInfo("Erken Getirme Bedeli", selectedContract.carDifferenceAmount, 1));
        }


        //if there is upsell or downsell add item for info
        if (selectedContract.updatedGroupCodeInformation != null) {
            if (selectedContract.updatedGroupCodeInformation.changeType == EnumUtils.EquipmentChangeType.DOWNSELL.getIntValue()) {
                list.add(new PaymentInfo("Downsell bedeli", selectedContract.updatedGroupCodeInformation.amountToBePaid, 1));
            }
            if (selectedContract.updatedGroupCodeInformation.changeType == EnumUtils.EquipmentChangeType.UPSELL.getIntValue()) {
                list.add(new PaymentInfo("Upsell bedeli", selectedContract.updatedGroupCodeInformation.amountToBePaid, 1));
            }
        }

        return list;
    }

    public double getDepositAmountDifference() {
        if (selectedContract.updatedGroupCodeInformation != null &&
                (selectedContract.updatedGroupCodeInformation.changeType == EnumUtils.EquipmentChangeType.UPSELL.getIntValue() ||
                        selectedContract.updatedGroupCodeInformation.changeType == EnumUtils.EquipmentChangeType.DOWNSELL.getIntValue())) {
            //double newGroupDepositAmount = CommonMethods.instance.getSelectedGroupCodeInformation(getContext(),selectedContract.updatedGroupCodeInformation.groupCodeId).depositAmount;
            //double newGroupDepositAmount = CommonMethods.instance.getSelectedGroupCodeInformation(getContext(),selectedContract.updatedGroupCodeInformation.groupCodeId).depositAmount;
            return selectedContract.updatedGroupCodeInformation.depositAmount - selectedContract.depositAmount;
        }

        return 0;
    }

    public void openCamera(int requestCode) {
        if (mActivity.checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    requestCode);
        } else {
            Intent intent = ImageUtil.instance.dispatchTakePictureIntent(mActivity);
            startActivityForResult(intent, REQUEST_TAKE_PHOTO);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_TAKE_PHOTO) {
            compressedImage = ImageUtil.instance.getImageFile();
            try {
                compressedImage = new Compressor(mActivity.getApplicationContext()).setQuality(50).compressToFile(ImageUtil.instance.getImageFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
            hasBlobStorageError = false;
            super.showLoading();
            (new Handler()).postDelayed(this::uploadPhoto, 1000);
        }
    }

    public void uploadPhoto() {

    }
}
