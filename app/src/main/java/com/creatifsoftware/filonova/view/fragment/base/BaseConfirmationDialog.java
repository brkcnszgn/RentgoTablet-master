package com.creatifsoftware.filonova.view.fragment.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.creatifsoftware.filonova.R;
import com.creatifsoftware.filonova.databinding.FragmentBaseConfirmationDialogBinding;
import com.creatifsoftware.filonova.di.Injectable;
import com.creatifsoftware.filonova.interfaces.CommunicationInterface;
import com.creatifsoftware.filonova.view.activity.MainActivity;

/**
 * Created by kerembalaban on 7.03.2019 at 12:18.
 */

public class BaseConfirmationDialog extends Fragment implements Injectable {
    private static final String CANCEL_BUTTON_TITLE_KEY = "cancel_button_title_key";
    private static final String OK_BUTTON_TITLE_KEY = "ok_button_title_key";
    private static final String MESSAGE_KEY = "message_key";
    private static final String FROM_ACTIVITY = "from_activity";

    private FragmentBaseConfirmationDialogBinding binding;
    private MainActivity mActivity;
    private boolean fromActivity;

    public static BaseConfirmationDialog with(String cancelButtonTitle, String okButtonTitle, String message) {
        BaseConfirmationDialog fragment = new BaseConfirmationDialog();

        Bundle args = new Bundle();

        args.putString(CANCEL_BUTTON_TITLE_KEY, cancelButtonTitle);
        args.putString(OK_BUTTON_TITLE_KEY, okButtonTitle);
        args.putString(MESSAGE_KEY, message);
        args.putBoolean(FROM_ACTIVITY, false);

        fragment.setArguments(args);

        return fragment;
    }

    public static BaseConfirmationDialog forMainActivity(String cancelButtonTitle, String okButtonTitle, String message) {
        BaseConfirmationDialog fragment = new BaseConfirmationDialog();

        Bundle args = new Bundle();

        args.putString(CANCEL_BUTTON_TITLE_KEY, cancelButtonTitle);
        args.putString(OK_BUTTON_TITLE_KEY, okButtonTitle);
        args.putString(MESSAGE_KEY, message);
        args.putBoolean(FROM_ACTIVITY, true);

        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_base_confirmation_dialog, container, false);
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

        Bundle bundle = getArguments();
        if (bundle != null) {
            binding.cancelButton.setText(getArguments().getString(CANCEL_BUTTON_TITLE_KEY));
            binding.confirmButton.setText(getArguments().getString(OK_BUTTON_TITLE_KEY));
            binding.messageText.setText(getArguments().getString(MESSAGE_KEY));
            fromActivity = getArguments().getBoolean(FROM_ACTIVITY);
        }

        binding.cancelButton.setOnClickListener(view -> mActivity.onBackPressed());
        binding.closeButtonLayout.setOnClickListener(view -> mActivity.onBackPressed());
        binding.confirmButton.setOnClickListener(view -> {
            mActivity.onBackPressed();
            if (fromActivity) {
                myInterface.confirmButtonClickedForActivity();
            } else {
                myInterface.confirmButtonClicked();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
