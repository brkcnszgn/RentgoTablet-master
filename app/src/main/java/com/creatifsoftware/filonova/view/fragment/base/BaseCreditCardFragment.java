package com.creatifsoftware.filonova.view.fragment.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.creatifsoftware.filonova.R;
import com.creatifsoftware.filonova.databinding.FragmentBaseCreditCardBinding;
import com.creatifsoftware.filonova.di.Injectable;
import com.creatifsoftware.filonova.interfaces.CommunicationInterface;
import com.creatifsoftware.filonova.model.CreditCard;
import com.creatifsoftware.filonova.utils.EnumUtils;
import com.creatifsoftware.filonova.utils.RegexUtils;
import com.creatifsoftware.filonova.view.activity.MainActivity;
import com.creatifsoftware.filonova.view.adapter.ExpandableCardListAdapter;

import java.util.Calendar;

/**
 * Created by kerembalaban on 7.03.2019 at 12:18.
 */


public class BaseCreditCardFragment extends Fragment implements Injectable {
    ExpandableCardListAdapter expandableCardListAdapter;
    FragmentBaseCreditCardBinding binding;
    MainActivity mActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_base_credit_card, container, false);
        //prepareExpandableCardListAdapter();
        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.mActivity = (MainActivity) context;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        binding.cardNumber.setFocusable(true);
        binding.cardNumber.requestFocus();
        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(binding.cardNumber, InputMethodManager.SHOW_IMPLICIT);

        binding.closeButtonLayout.setOnClickListener(view -> {
            this.hideKeyboard();
            mActivity.onBackPressed();
        });

        addTextWatcher();
        addClickListener();
    }

    void addClickListener() {
        binding.addCard.setOnClickListener(view -> {
            hideErrorTextViews();
            if (checkInputValidation()) {
                CommunicationInterface myInterface = (CommunicationInterface) getActivity();
                hideKeyboard();
                mActivity.onBackPressed();
                myInterface.addCreditCard(prepareCreditCard());
            }
        });

        binding.creditCardList.setOnItemClickListener((adapterView, view, i, l) -> {

        });
    }

    boolean checkInputValidation() {
        boolean validationSuccess = true;
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        if (binding.cardNumber.getText().toString().isEmpty()) {
            binding.cardNumberError.setText("Boş bırakılamaz");
            binding.cardNumberError.setVisibility(View.VISIBLE);
            validationSuccess = false;
        }
//        else{
//            String firstSixDigit = binding.cardNumber.getText().toString().substring(0,5);
//            String lastFourDigit = binding.cardNumber.getText().toString().substring(11,15);
//
//            String paymentCardFirstSixDigit = paymentCard.creditCardNumber.substring(0,5);
//            String paymentCardLastFourDigit = paymentCard.creditCardNumber.toString().substring(11,15);
//
//            if (firstSixDigit.equals(paymentCardFirstSixDigit) &&
//                lastFourDigit.equals(paymentCardLastFourDigit)){
//                binding.cardHolderNameError.setText("Seçilen araba çift kredi");
//                binding.cardHolderNameError.setVisibility(View.VISIBLE);
//                validationSuccess = false;
//            }
//        }
        if (binding.cardHolderName.getText().toString().isEmpty()) {
            binding.cardHolderNameError.setText("Boş bırakılamaz");
            binding.cardHolderNameError.setVisibility(View.VISIBLE);
            validationSuccess = false;
        }
        if (binding.validMonth.getText().toString().isEmpty()) {
            binding.validMonthError.setText("Boş bırakılamaz");
            binding.validMonthError.setVisibility(View.VISIBLE);
            validationSuccess = false;
        }
        if (binding.validYear.getText().toString().isEmpty()) {
            binding.validYearError.setText("Boş bırakılamaz");
            binding.validYearError.setVisibility(View.VISIBLE);
            validationSuccess = false;
        }
        if (binding.cvv.getText().toString().isEmpty()) {
            binding.cvvError.setText("Boş bırakılamaz");
            binding.cvvError.setVisibility(View.VISIBLE);
            validationSuccess = false;
        } else if (Integer.valueOf(binding.validMonth.getText().toString()) > 12 ||
                Integer.valueOf(binding.validMonth.getText().toString()) == 0 ||
                binding.validMonth.getText().toString().length() != 2) {
            binding.validMonthError.setText("Geçerli ay girin");
            binding.validMonthError.setVisibility(View.VISIBLE);
            validationSuccess = false;
        } else if (Integer.valueOf(binding.validYear.getText().toString()) < currentYear ||
                binding.validYear.getText().toString().length() != 4) {
            binding.validYearError.setText("Geçerli yıl girin");
            binding.validYearError.setVisibility(View.VISIBLE);
            validationSuccess = false;
        } else if (binding.cvv.getText().toString().length() != 3) {
            binding.cvvError.setText("Geçerli güvenlik kodu girin");
            binding.cvvError.setVisibility(View.VISIBLE);
            validationSuccess = false;
        }

        return validationSuccess;
    }

    private CreditCard prepareCreditCard() {
        CreditCard creditCard = new CreditCard();
        creditCard.cardHolderName = binding.cardHolderName.getText().toString();
        creditCard.creditCardNumber = binding.cardNumber.getText().toString().replaceAll(" ", "");
        creditCard.expireMonth = Integer.valueOf(binding.validMonth.getText().toString());
        creditCard.expireYear = Integer.valueOf(binding.validYear.getText().toString());
        creditCard.cvc = binding.cvv.getText().toString();
        creditCard.cardType = EnumUtils.TransactionType.SALE.getIntValue();

        return creditCard;
    }

    private void addTextWatcher() {
        binding.cardNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                hideErrorTextViews();
                int maxLength = 16;
                if (charSequence.toString().matches(RegexUtils.instance.amexCardCardRegex())) {
                    maxLength = 15;
                }
                binding.cardNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.cardHolderName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                hideErrorTextViews();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.validMonth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                hideErrorTextViews();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.validYear.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                hideErrorTextViews();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.cvv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                hideErrorTextViews();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    void hideErrorTextViews() {
        binding.cardNumberError.setVisibility(View.GONE);
        binding.cardHolderNameError.setVisibility(View.GONE);
        binding.validMonthError.setVisibility(View.GONE);
        binding.validYearError.setVisibility(View.GONE);
        binding.cvvError.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    void hideKeyboard() {
        View view = mActivity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    String getCreditCardFirstSixCharacter(String cardNumber) {
        return cardNumber.substring(0, 6);
    }

    String getCreditCardLastFourCharacter(String cardNumber) {
        return cardNumber.matches(RegexUtils.instance.amexCardCardRegex()) ? cardNumber.substring(11, 15) : cardNumber.substring(12, 16);
    }
}
