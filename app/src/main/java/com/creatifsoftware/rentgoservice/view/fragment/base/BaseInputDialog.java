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
import com.creatifsoftware.rentgoservice.databinding.FragmentBaseInputDialogBinding;
import com.creatifsoftware.rentgoservice.di.Injectable;
import com.creatifsoftware.rentgoservice.interfaces.CommunicationInterface;
import com.creatifsoftware.rentgoservice.model.Equipment;
import com.creatifsoftware.rentgoservice.view.activity.MainActivity;

/**
 * Created by kerembalaban on 7.03.2019 at 12:18.
 */


public class BaseInputDialog extends Fragment implements Injectable {
    private static final String KEY_EDIT_TEXT_HINT = "edit_text_hint";
    private static final String KEY_ERROR_MESSAGE = "error_message";
    private static final String KEY_TAG = "tag";
    private static Equipment equipment;
    private FragmentBaseInputDialogBinding binding;
    private MainActivity mActivity;
    private Integer tag;

    public static BaseInputDialog withEditTextHintAndTag(String hint, Integer tag, String errorMessage) {
        BaseInputDialog baseInputDialog = new BaseInputDialog();

        Bundle args = new Bundle();

        args.putString(KEY_EDIT_TEXT_HINT, hint);
        args.putString(KEY_ERROR_MESSAGE, errorMessage);
        args.putInt(KEY_TAG, tag);
        baseInputDialog.setArguments(args);

        return baseInputDialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_base_input_dialog, container, false);
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

        Bundle bundle = getArguments();
        if (bundle != null) {
            binding.inputEditText.setHint(getArguments().getString(KEY_EDIT_TEXT_HINT));
            binding.errorText.setText(getArguments().getString(KEY_ERROR_MESSAGE));
            tag = getArguments().getInt(KEY_TAG);
        }

        CommunicationInterface myInterface = (CommunicationInterface) getActivity();

        binding.inputEditText.setFocusable(true);
        binding.inputEditText.requestFocus();
        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(binding.inputEditText, InputMethodManager.SHOW_IMPLICIT);

        binding.closeButtonLayout.setOnClickListener(view -> {
            this.hideKeyboard();
            mActivity.onBackPressed();
        });

        binding.inputEditText.addTextChangedListener(new TextWatcher() {
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
            if (binding.inputEditText.getText().toString().isEmpty()) {
                //binding.errorText.setText(getString(R.string.kilometer_empty_error));
                binding.errorText.setVisibility(View.VISIBLE);
            } else {
                hideKeyboard();
                mActivity.onBackPressed();
                myInterface.getInputValue(binding.inputEditText.getText().toString(), tag);
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
