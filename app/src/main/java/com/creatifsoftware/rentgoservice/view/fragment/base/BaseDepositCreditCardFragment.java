package com.creatifsoftware.rentgoservice.view.fragment.base;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.creatifsoftware.rentgoservice.R;
import com.creatifsoftware.rentgoservice.di.Injectable;
import com.creatifsoftware.rentgoservice.interfaces.CommunicationInterface;
import com.creatifsoftware.rentgoservice.model.ContractItem;
import com.creatifsoftware.rentgoservice.model.CreditCard;
import com.creatifsoftware.rentgoservice.utils.EnumUtils;
import com.creatifsoftware.rentgoservice.utils.RegexUtils;
import com.creatifsoftware.rentgoservice.view.adapter.ExpandableCardListAdapter;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by kerembalaban on 7.03.2019 at 12:18.
 */


public class BaseDepositCreditCardFragment extends BaseCreditCardFragment implements Injectable {
    public ContractItem contractItem;
    public double depositAmount;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        prepareExpandableCardListAdapter();
    }

    private void prepareExpandableCardListAdapter() {
        ArrayList<CreditCard> tempList = new ArrayList<>();
        if (contractItem.creditCards.size() > 1) {
            tempList = contractItem.customer.cardList;
        } else {
            CreditCard paymentCard = contractItem.creditCards.get(0);
            for (CreditCard item : contractItem.customer.cardList) {
                if (!item.creditCardNumber.equals(paymentCard.creditCardNumber)) {
                    item.isSelected = false;
                    tempList.add(item);
                }
            }
        }

        if (tempList.size() > 0) {
            expandableCardListAdapter = new ExpandableCardListAdapter(getContext(), "Teminat Kartı", depositAmount);
            expandableCardListAdapter.setDetailList(tempList);
            binding.creditCardList.setAdapter(expandableCardListAdapter);
            binding.creditCardList.setVisibility(View.VISIBLE);
            binding.creditCardList.setOnChildClickListener((expandableListView, view, i, i1, l) -> {
                CreditCard card = expandableCardListAdapter.getDetailList().get(i1);
                CommunicationInterface myInterface = (CommunicationInterface) getActivity();
                hideKeyboard();
                mActivity.onBackPressed();
                myInterface.addDepositCard(card);
                return false;
            });
        }
    }

    @Override
    boolean checkInputValidation() {
        boolean validationSuccess = true;
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        if (binding.cardNumber.getText().toString().isEmpty()) {

            binding.cardNumberError.setText(R.string.field_empty_error);
            binding.cardNumberError.setVisibility(View.VISIBLE);
            validationSuccess = false;
        } else if ((binding.cardNumber.getText().toString().matches(RegexUtils.instance.visaCardRegex()) ||
                binding.cardNumber.getText().toString().matches(RegexUtils.instance.masterCardCardRegex())) &&
                binding.cardNumber.getText().toString().length() != 16) {

            binding.cardNumberError.setText(R.string.visa_master_card_number_length_error);
            binding.cardNumberError.setVisibility(View.VISIBLE);
            validationSuccess = false;
        } else if (binding.cardNumber.getText().toString().matches(RegexUtils.instance.amexCardCardRegex()) &&
                binding.cardNumber.getText().toString().length() != 15) {

            binding.cardNumberError.setText(R.string.amex_card_number_length_error);
            binding.cardNumberError.setVisibility(View.VISIBLE);
            validationSuccess = false;
        } else if (expandableCardListAdapter != null) {
            String firstSixDigit = getCreditCardFirstSixCharacter(binding.cardNumber.getText().toString());
            String lastFourDigit = getCreditCardLastFourCharacter(binding.cardNumber.getText().toString());

            for (CreditCard creditCard : expandableCardListAdapter.getDetailList()) {
                String paymentCardFirstSixDigit = getCreditCardFirstSixCharacter(creditCard.creditCardNumber);
                String paymentCardLastFourDigit = getCreditCardLastFourCharacter(creditCard.creditCardNumber);

                if (firstSixDigit.equals(paymentCardFirstSixDigit) &&
                        lastFourDigit.equals(paymentCardLastFourDigit)) {
                    binding.cardNumberError.setText(R.string.credit_card_is_on_list_error);
                    binding.cardNumberError.setVisibility(View.VISIBLE);
                    validationSuccess = false;
                }
            }
        }
        if (binding.cardHolderName.getText().toString().isEmpty()) {
            binding.cardHolderNameError.setText(R.string.field_empty_error);
            binding.cardHolderNameError.setVisibility(View.VISIBLE);
            validationSuccess = false;
        } else if (!binding.cardHolderName.getText().toString().toLowerCase().equals(contractItem.customer.fullName.toLowerCase())) {
            binding.cardHolderNameError.setText(R.string.card_holder_name_error);
            binding.cardHolderNameError.setVisibility(View.VISIBLE);
            validationSuccess = false;
        }
        if (binding.validMonth.getText().toString().isEmpty()) {
            binding.validMonthError.setText(R.string.field_empty_error);
            binding.validMonthError.setVisibility(View.VISIBLE);
            validationSuccess = false;
        }
        if (binding.validYear.getText().toString().isEmpty()) {
            binding.validYearError.setText(R.string.field_empty_error);
            binding.validYearError.setVisibility(View.VISIBLE);
            validationSuccess = false;
        }
        if (binding.cvv.getText().toString().isEmpty()) {
            binding.cvvError.setText(R.string.field_empty_error);
            binding.cvvError.setVisibility(View.VISIBLE);
            validationSuccess = false;
        } else if (Integer.valueOf(binding.validMonth.getText().toString()) > 12 ||
                Integer.valueOf(binding.validMonth.getText().toString()) == 0 ||
                binding.validMonth.getText().toString().length() != 2) {
            binding.validMonthError.setText(R.string.valid_card_month_error);
            binding.validMonthError.setVisibility(View.VISIBLE);
            validationSuccess = false;
        } else if (Integer.valueOf(binding.validYear.getText().toString()) < currentYear ||
                binding.validYear.getText().toString().length() != 4) {
            binding.validYearError.setText(R.string.valid_card_year_error);
            binding.validYearError.setVisibility(View.VISIBLE);
            validationSuccess = false;
        } else if (binding.cvv.getText().toString().length() != 3 &&
                (binding.cvv.getText().toString().matches(RegexUtils.instance.visaCardRegex()) ||
                        binding.cvv.getText().toString().matches(RegexUtils.instance.masterCardCardRegex()))) {
            binding.cvvError.setText(R.string.visa_master_card_cvv_error);
            binding.cvvError.setVisibility(View.VISIBLE);
            validationSuccess = false;
        } else if (binding.cvv.getText().toString().length() != 4 &&
                (binding.cvv.getText().toString().matches(RegexUtils.instance.amexCardCardRegex()))) {
            binding.cvvError.setText(R.string.amex_card_cvv_error);
            binding.cvvError.setVisibility(View.VISIBLE);
            validationSuccess = false;
        }

        return validationSuccess;
    }

    @Override
    void addClickListener() {
        binding.addCard.setOnClickListener(view -> {
            hideErrorTextViews();
            if (checkInputValidation()) {
                CommunicationInterface myInterface = (CommunicationInterface) getActivity();
                hideKeyboard();
                mActivity.onBackPressed();
                myInterface.addDepositCard(prepareCreditCard());
            }
        });

        binding.creditCardList.setOnItemClickListener((adapterView, view, i, l) -> {

        });
    }

    private CreditCard prepareCreditCard() {
        CreditCard creditCard = new CreditCard();
        creditCard.cardHolderName = binding.cardHolderName.getText().toString();
        creditCard.creditCardNumber = binding.cardNumber.getText().toString().replaceAll(" ", "");
        creditCard.expireMonth = Integer.valueOf(binding.validMonth.getText().toString());
        creditCard.expireYear = Integer.valueOf(binding.validYear.getText().toString());
        creditCard.cvc = binding.cvv.getText().toString();
        creditCard.cardType = EnumUtils.TransactionType.DEPOSIT.getIntValue();

        return creditCard;
    }
}
