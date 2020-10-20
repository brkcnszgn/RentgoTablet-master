package com.creatifsoftware.rentgoservice.view.fragment.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
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

import com.creatifsoftware.rentgoservice.R;
import com.creatifsoftware.rentgoservice.databinding.FragmentExtraPaymentDialogBinding;
import com.creatifsoftware.rentgoservice.di.Injectable;
import com.creatifsoftware.rentgoservice.interfaces.CommunicationInterface;
import com.creatifsoftware.rentgoservice.model.AdditionalProduct;
import com.creatifsoftware.rentgoservice.view.activity.MainActivity;

import java.util.Objects;

public class BaseExtraPaymentDialog extends Fragment implements Injectable {
    private static final String KEY_OTHER_ADDITIONAL_PRODUCT_DATA = "other_additional_product_Data";
    private FragmentExtraPaymentDialogBinding binding;
    private MainActivity mActivity;
    private AdditionalProduct otherCostAdditionalProductData;

    public static BaseExtraPaymentDialog with(AdditionalProduct product) {
        BaseExtraPaymentDialog fragment = new BaseExtraPaymentDialog();

        Bundle args = new Bundle();

        args.putSerializable(KEY_OTHER_ADDITIONAL_PRODUCT_DATA, product);
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_extra_payment_dialog, container, false);
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
        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        binding.extraPaymentTitle.requestFocus();

        addButtonClickListeners();
        addTextChangeListeners();
    }

    private void addButtonClickListeners() {
        binding.closeButtonLayout.setOnClickListener(view -> {
            this.hideKeyboard();
            mActivity.onBackPressed();
        });

        binding.addExtraPaymentButton.setOnClickListener(view -> {
            CommunicationInterface myInterface = (CommunicationInterface) getActivity();
            hideKeyboard();
            if (binding.extraPaymentTitle.getText().toString().isEmpty()) {
                binding.titleErrorText.setText(getString(R.string.extra_payment_title_empty_error));
                binding.titleErrorText.setVisibility(View.VISIBLE);
            } else if (binding.extraPaymentAmount.getText().toString().isEmpty()) {
                binding.amountErrorText.setText(getString(R.string.extra_payment_amount_empty_error));
                binding.amountErrorText.setVisibility(View.VISIBLE);
            } else if (binding.extraPaymentAmount.getText().toString().equals(".")) {
                binding.amountErrorText.setText(getString(R.string.extra_payment_amount_missing_error));
                binding.amountErrorText.setVisibility(View.VISIBLE);
            } else if (Double.valueOf(binding.extraPaymentAmount.getText().toString()) == 0) {
                binding.amountErrorText.setText(getString(R.string.extra_payment_amount_zero_error));
                binding.amountErrorText.setVisibility(View.VISIBLE);
            }
            mActivity.onBackPressed();
            AdditionalProduct item = prepareExtraPaymentObject();
            myInterface.addCustomExtraPayment(item);
        });
    }

    private AdditionalProduct prepareExtraPaymentObject() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            otherCostAdditionalProductData = (AdditionalProduct) getArguments().getSerializable(KEY_OTHER_ADDITIONAL_PRODUCT_DATA);
        }

        AdditionalProduct temp = new AdditionalProduct(Objects.requireNonNull(otherCostAdditionalProductData));

        temp.productName = binding.extraPaymentTitle.getText().toString();
        temp.value = 1;
        temp.isChecked = true;
        temp.actualTotalAmount = Double.valueOf(binding.extraPaymentAmount.getText().toString());
        temp.tobePaidAmount = Double.valueOf(binding.extraPaymentAmount.getText().toString());
        temp.actualAmount = Double.valueOf(binding.extraPaymentAmount.getText().toString());

        return temp;
    }

    private void addTextChangeListeners() {
        binding.extraPaymentTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.titleErrorText.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.extraPaymentAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.amountErrorText.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
