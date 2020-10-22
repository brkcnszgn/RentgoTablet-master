package com.creatifsoftware.filonova.view.fragment.base;

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

import com.creatifsoftware.filonova.R;
import com.creatifsoftware.filonova.databinding.FragmentBaseKilometerDialogBinding;
import com.creatifsoftware.filonova.di.Injectable;
import com.creatifsoftware.filonova.interfaces.CommunicationInterface;
import com.creatifsoftware.filonova.model.Equipment;
import com.creatifsoftware.filonova.view.activity.MainActivity;

/**
 * Created by kerembalaban on 7.03.2019 at 12:18.
 */


public class BaseKilometerDialog extends Fragment implements Injectable {
    private static Equipment equipment;
    private FragmentBaseKilometerDialogBinding binding;
    private MainActivity mActivity;

    public static BaseKilometerDialog withSelectedEquipment(Equipment aEquipment) {
        BaseKilometerDialog baseKilometerDialog = new BaseKilometerDialog();
        equipment = aEquipment;

        return baseKilometerDialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_base_kilometer_dialog, container, false);
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

        binding.kilometerEditText.setFocusable(true);
        binding.kilometerEditText.requestFocus();
        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(binding.kilometerEditText, InputMethodManager.SHOW_IMPLICIT);

        binding.closeButtonLayout.setOnClickListener(view -> {
            this.hideKeyboard();
            mActivity.onBackPressed();
        });

        binding.kilometerEditText.addTextChangedListener(new TextWatcher() {
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

        binding.addKilometerButton.setOnClickListener(view -> {
            binding.errorText.setVisibility(View.GONE);
            if (binding.kilometerEditText.getText().toString().isEmpty()) {
                binding.errorText.setText(getString(R.string.kilometer_empty_error));
                binding.errorText.setVisibility(View.VISIBLE);
            } else if (Integer.valueOf(binding.kilometerEditText.getText().toString()) < equipment.kmValue) {
                binding.errorText.setText(getString(R.string.kilometer_small_error));
                binding.errorText.setVisibility(View.VISIBLE);
            } else {
                hideKeyboard();
                mActivity.onBackPressed();
                myInterface.onKilometerSelected(binding.kilometerEditText.getText().toString());
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
