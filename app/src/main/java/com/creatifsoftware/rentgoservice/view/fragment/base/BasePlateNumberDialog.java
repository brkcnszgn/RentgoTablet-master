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
import com.creatifsoftware.rentgoservice.databinding.FragmentBasePlateNumberDialogBinding;
import com.creatifsoftware.rentgoservice.di.Injectable;
import com.creatifsoftware.rentgoservice.interfaces.CommunicationInterface;
import com.creatifsoftware.rentgoservice.view.activity.MainActivity;

/**
 * Created by kerembalaban on 7.03.2019 at 12:18.
 */


public class BasePlateNumberDialog extends Fragment implements Injectable {
    private FragmentBasePlateNumberDialogBinding binding;
    private MainActivity mActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_base_plate_number_dialog, container, false);
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
        CommunicationInterface myInterface = (CommunicationInterface) getActivity();

        binding.plateNumber.setFocusable(true);
        binding.plateNumber.requestFocus();
        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(binding.plateNumber, InputMethodManager.SHOW_IMPLICIT);

        binding.closeButtonLayout.setOnClickListener(view -> {
            this.hideKeyboard();
            mActivity.onBackPressed();
        });

        binding.plateNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.errorText.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.searchContractButton.setOnClickListener(view -> {
            binding.errorText.setVisibility(View.GONE);
            if (binding.plateNumber.getText().toString().isEmpty()) {
                binding.errorText.setText(getString(R.string.plate_number_empty_error));
                binding.errorText.setVisibility(View.VISIBLE);
            } else {
                hideKeyboard();
                mActivity.onBackPressed();
                myInterface.searchContractByPlateNumber(binding.plateNumber.getText().toString());
            }
        });
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    private void hideKeyboard() {
        View view = mActivity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
