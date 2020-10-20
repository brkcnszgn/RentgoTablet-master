package com.creatifsoftware.rentgoservice.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.creatifsoftware.rentgoservice.R;
import com.creatifsoftware.rentgoservice.databinding.FragmentReservationInformationBinding;
import com.creatifsoftware.rentgoservice.di.Injectable;
import com.creatifsoftware.rentgoservice.model.CreditCard;
import com.creatifsoftware.rentgoservice.model.PaymentInfo;
import com.creatifsoftware.rentgoservice.model.ReservationItem;
import com.creatifsoftware.rentgoservice.model.User;
import com.creatifsoftware.rentgoservice.model.base.ResponseResult;
import com.creatifsoftware.rentgoservice.utils.CommonMethods;
import com.creatifsoftware.rentgoservice.utils.EnumUtils;
import com.creatifsoftware.rentgoservice.view.adapter.CreditCardListAdapter;
import com.creatifsoftware.rentgoservice.view.adapter.PaymentInfoListAdapter;
import com.creatifsoftware.rentgoservice.view.callback.CreditCardClickCallback;
import com.creatifsoftware.rentgoservice.view.fragment.base.BaseFragment;
import com.creatifsoftware.rentgoservice.view.fragment.dashboards.ContractDashboardFragment;
import com.creatifsoftware.rentgoservice.viewmodel.ReservationInformationViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

/**
 * Created by kerembalaban on 2019-05-23 at 16:00.
 */
public class ReservationInformationFragment extends BaseFragment implements Injectable {

    private static final String KEY_SELECTED_RESERVATION = "selected_reservation";
    boolean isServiceSuccess = false;
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private CreditCardListAdapter paymentCardListAdapter;
    private CreditCardListAdapter depositCardListAdapter;
    private FragmentReservationInformationBinding binding;
    private ReservationInformationViewModel viewModel;
    private ReservationItem selectedReservation;
    private final CreditCardClickCallback paymentCardClickCallback = new CreditCardClickCallback() {
        @Override
        public void onClick(CreditCard creditCard) {
            for (CreditCard item : selectedReservation.customer.cardList) {
                item.isSelected = false;
            }
            creditCard.isSelected = !creditCard.isSelected;
            paymentCardListAdapter.notifyDataSetChanged();
        }
    };
    private final CreditCardClickCallback depositCardClickCallback = new CreditCardClickCallback() {
        @Override
        public void onClick(CreditCard creditCard) {
            for (CreditCard item : selectedReservation.customer.cardList) {
                item.isSelected = false;
            }
            creditCard.isSelected = !creditCard.isSelected;
            depositCardListAdapter.notifyDataSetChanged();
        }
    };

    public static ReservationInformationFragment forSelectedReservation(ReservationItem selectedReservation) {
        ReservationInformationFragment fragment = new ReservationInformationFragment();
        Bundle args = new Bundle();

        args.putSerializable(KEY_SELECTED_RESERVATION, selectedReservation);
        fragment.setArguments(args);

        return fragment;
    }

    @androidx.annotation.Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @androidx.annotation.Nullable ViewGroup container,
                             @androidx.annotation.Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reservation_information, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mActivity.showProgressBar();
        viewModel = ViewModelProviders.of(mActivity, viewModelFactory)
                .get(ReservationInformationViewModel.class);

        Bundle bundle = getArguments();
        if (bundle != null) {
            selectedReservation = ((ReservationItem) bundle.getSerializable(KEY_SELECTED_RESERVATION));
        }
        binding.setReservationItem(selectedReservation);

        // kurumsal ve cariyse kart bilgilerini çekme
        if (selectedReservation.customer.paymentMethod == EnumUtils.PaymentMethod.CURRENT.getIntValue() ||
                selectedReservation.customer.paymentMethod == EnumUtils.PaymentMethod.FULL_CREDIT.getIntValue()) {
        } else {
            observeCustomerCreditCards();
        }

        preparePaymentInfoRecyclerView();
    }

    @Override
    protected ResponseResult checkBeforeNavigate() {
        // kurumsal ve cariyse kartları kontrol etme
        if (selectedReservation.customer.paymentMethod == EnumUtils.PaymentMethod.CURRENT.getIntValue() ||
                selectedReservation.customer.paymentMethod == EnumUtils.PaymentMethod.FULL_CREDIT.getIntValue()) {

        } else if (selectedReservation.customer.cardList != null) {
            CreditCard selectedCard = new CreditCard();
            for (CreditCard item : selectedReservation.customer.cardList) {
                if (item.isSelected) {
                    selectedCard = item;
                    break;
                }
            }

            // if need payment, then check if card is selected
            if (getTotalPaymentAmount() > 0 && selectedCard.creditCardNumber == null) {
                return new ResponseResult(false, "Ödenecek tutar mevcut, lütfen kart seçimi yapın");
            }

            if (CommonMethods.instance.getSelectedGroupCodeInformation(selectedReservation.groupCodeInformation.groupCodeId).isDoubleCard && !checkDoubleCreditCard()) {
                return new ResponseResult(false, "Seçilen araç grubu çift kredi istemektedir. Seçilen teminat kartı, rezervasyonda kullanılan karttan farklı olmalıdır.");
            }
        }

        return super.checkBeforeNavigate();
    }

    private boolean checkDoubleCreditCard() {
        return true;
    }

    @Override
    protected void navigate() {
        super.navigate();
        String message;
        if (getTotalPaymentAmount() > 0) {
            message = String.format(Locale.getDefault(), "%s numaralı rezervasyonunuz, %.02f TL teminat alınarak sözleşmeye dönüştürülecektir, emin misiniz?"
                    , selectedReservation.reservationNumber, getTotalPaymentAmount());
        } else {
            message = String.format("%s numaralı rezervasyonunuz, sözleşmeye dönüştürülecektir, emin misiniz?"
                    , selectedReservation.reservationNumber);
        }

        mActivity.showConfirmationDialog(getString(R.string.dialog_button_cancel), getString(R.string.dialog_button_yes), message);
    }

    @Override
    public void confirmationButtonClicked() {
        createContract();
    }

    private void createContract() {
        super.showLoading();
        viewModel.setReservationItem(selectedReservation);
        viewModel.setUser(new User().getUser(getContext()));
        viewModel.getCreateQuickContractObservable().observe(getViewLifecycleOwner(), baseResponse -> {
            super.hideLoading();
            if (baseResponse == null) {
                showMessageDialog(getString(R.string.unknown_error_message));
            } else if (baseResponse.responseResult.result) {
                isServiceSuccess = true;
                mActivity.showMessageDialog(getString(R.string.quick_contract_dialog_success_message));
            } else {
                showMessageDialog(baseResponse.responseResult.exceptionDetail);
            }
        });
    }

    @Override
    public void messageDialogDoneButtonClicked() {
        if (isServiceSuccess) {
            //mActivity.hideProgressBar();
            ContractDashboardFragment fragment = new ContractDashboardFragment();
            mActivity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_layout, fragment, "").commit();
        }
    }

    private void observeCustomerCreditCards() {
        viewModel.setCustomerId(selectedReservation.customer.customerId);
        viewModel.getCreditCardsObservable().observe(getViewLifecycleOwner(), creditCardsResponse -> {
            if (creditCardsResponse.responseResult.result) {
                if (creditCardsResponse.creditCardList.size() > 0) {
                    selectedReservation.customer.cardList = creditCardsResponse.creditCardList;
                    prepareCreditCardRecyclerView();
                }
            }
        });
    }

    private void prepareCreditCardRecyclerView() {
        paymentCardListAdapter = new CreditCardListAdapter(paymentCardClickCallback);
        depositCardListAdapter = new CreditCardListAdapter(depositCardClickCallback);

        paymentCardListAdapter.setCardList(selectedReservation.customer.cardList);
        binding.paymentCard.creditCardList.setAdapter(paymentCardListAdapter);
        binding.paymentCard.setAddNewCard(false);
        binding.paymentCard.setTitle(getString(R.string.payment_info));

        depositCardListAdapter.setCardList(selectedReservation.customer.cardList);
        binding.depositCard.creditCardList.setAdapter(depositCardListAdapter);
        binding.depositCard.setAddNewCard(true);
        binding.depositCard.setTitle(getString(R.string.deposit_info));
        binding.depositCard.addCardLayout.setOnClickListener(view -> mActivity.showCreditCardFragment());
    }

    private void preparePaymentInfoRecyclerView() {
        PaymentInfoListAdapter paymentInfoListAdapter = new PaymentInfoListAdapter();
        paymentInfoListAdapter.setPaymentInfoList(getPaymentInfoList());
        binding.paymentInfoList.setAdapter(paymentInfoListAdapter);
        binding.paymentInfoList.addItemDecoration(new DividerItemDecoration(binding.paymentInfoList.getContext(), DividerItemDecoration.VERTICAL));
        binding.totalPaymentInfoAmount.setText(String.format(Locale.getDefault(), "%.02f TL", getTotalPaymentAmount()));
    }

    private List<PaymentInfo> getPaymentInfoList() {
        List<PaymentInfo> list = new ArrayList<>();

        list.add(new PaymentInfo("Ödenecek Rezervasyon Tutarı", selectedReservation.reservationTotalAmount, 1));
        list.add(new PaymentInfo("Ödenecek Teminat Tutarı", selectedReservation.depositAmount, 1));

        return list;
    }

    private double getTotalPaymentAmount() {
        double totalAmount = 0;
        for (PaymentInfo paymentInfo : getPaymentInfoList()) {
            totalAmount += paymentInfo.paymentInfoAmount * paymentInfo.value;
        }

        return totalAmount;
    }

    @Override
    public void addCreditCard(CreditCard creditCard) {
        for (CreditCard item : selectedReservation.customer.cardList) {
            item.isSelected = false;
        }
        creditCard.isSelected = true;
        selectedReservation.customer.cardList.add(creditCard);
        depositCardListAdapter.setCardList(selectedReservation.customer.cardList);
    }
}
