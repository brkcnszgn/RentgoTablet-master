package com.creatifsoftware.filonova.view.fragment.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.creatifsoftware.filonova.R;
import com.creatifsoftware.filonova.databinding.FragmentExtraPaymentDialogBinding;
import com.creatifsoftware.filonova.di.Injectable;
import com.creatifsoftware.filonova.interfaces.CommunicationInterface;
import com.creatifsoftware.filonova.model.AdditionalProduct;
import com.creatifsoftware.filonova.utils.CommonMethods;
import com.creatifsoftware.filonova.view.activity.MainActivity;
import com.creatifsoftware.filonova.view.adapter.ExtraServiceListAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BaseExtraPaymentDialog extends Fragment implements Injectable {
    private static final String KEY_OTHER_ADDITIONAL_PRODUCT_DATA = "other_additional_product_Data";
    public ExtraServiceListAdapter extraServiceListAdapter;
    private FragmentExtraPaymentDialogBinding binding;
    private MainActivity mActivity;
    private List<AdditionalProduct> otherCostAdditionalProductData;
    private List<AdditionalProduct> prepareExtraPaymentObject;


    public static BaseExtraPaymentDialog with(List<AdditionalProduct> product) {
        BaseExtraPaymentDialog fragment = new BaseExtraPaymentDialog();

        Bundle args = new Bundle();

        args.putSerializable(KEY_OTHER_ADDITIONAL_PRODUCT_DATA, (Serializable) product);
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

        initRcv();

        addButtonClickListeners();
        //addTextChangeListeners();
    }

    private void initRcv() {
        extraServiceListAdapter = new ExtraServiceListAdapter();
        binding.extraRcv.setAdapter(extraServiceListAdapter);
        binding.extraRcv.addItemDecoration(new DividerItemDecoration(binding.extraRcv.getContext(), DividerItemDecoration.VERTICAL));
        if (prepareExtraPaymentObject == null) {
            prepareExtraPaymentObject = CommonMethods.instance.getExtraServiceList(requireContext());
        }
        extraServiceListAdapter.setExtraServiceList(prepareExtraPaymentObject);
    }


    private void addButtonClickListeners() {
        binding.closeButtonLayout.setOnClickListener(view -> {
            this.hideKeyboard();
            mActivity.onBackPressed();
        });

        binding.addExtraPaymentButton.setOnClickListener(view -> {
            CommunicationInterface myInterface = (CommunicationInterface) getActivity();
            hideKeyboard();
      /*      if (binding.extraPaymentTitle.getText().toString().isEmpty()) {
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
            }*/
            mActivity.onBackPressed();

            prepareExtraPaymentObject = prepareExtraPaymentObject();
            myInterface.addCustomExtraPayment(prepareExtraPaymentObject);
        });
    }

    private List<AdditionalProduct> prepareExtraPaymentObject() {
        List<AdditionalProduct> additionalProductList = new ArrayList<>();
      /*  Bundle bundle = getArguments();
        if (bundle != null) {
            otherCostAdditionalProductData = (List<AdditionalProduct>) getArguments().getSerializable(KEY_OTHER_ADDITIONAL_PRODUCT_DATA);
        }*/

        for (AdditionalProduct product : extraServiceListAdapter.getItemList()) {

            product.value = 1;
                product.isChecked = true;
                product.actualTotalAmount = product.actualAmount;
                product.tobePaidAmount = product.actualAmount;
                additionalProductList.add(product);


        }


        return additionalProductList;
    }

  /*  private void addTextChangeListeners() {
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
    }*/

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
