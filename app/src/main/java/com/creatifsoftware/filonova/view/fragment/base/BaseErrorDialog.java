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
import com.creatifsoftware.filonova.databinding.FragmentBaseErrorDialogBinding;
import com.creatifsoftware.filonova.di.Injectable;
import com.creatifsoftware.filonova.interfaces.CommunicationInterface;
import com.creatifsoftware.filonova.view.activity.LoginActivity;
import com.creatifsoftware.filonova.view.activity.MainActivity;

/**
 * Created by kerembalaban on 7.03.2019 at 12:18.
 */
public class BaseErrorDialog extends Fragment implements Injectable {
    private static final String KEY_MESSAGE_CONTENT = "message_content";
    private Activity activity;

    private FragmentBaseErrorDialogBinding binding;

    public static BaseErrorDialog withMessageContent(String content) {
        BaseErrorDialog fragment = new BaseErrorDialog();
        Bundle args = new Bundle();

        args.putString(KEY_MESSAGE_CONTENT, content);
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_base_error_dialog, container, false);
        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            this.activity = (MainActivity) context;
        }
        if (context instanceof LoginActivity) {
            this.activity = (LoginActivity) context;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        String message = "";
        if (bundle != null) {
            message = getArguments().getString(KEY_MESSAGE_CONTENT);
        }

        binding.messageText.setText(message);
        binding.closeButtonLayout.setOnClickListener(view -> dismissErrorDialog());
        binding.okButton.setOnClickListener(view -> dismissErrorDialog());
    }

    private void dismissErrorDialog() {
        if (activity instanceof LoginActivity) {
            activity.onBackPressed();
        } else {
            CommunicationInterface myInterface = (CommunicationInterface) getActivity();
            activity.onBackPressed();
            myInterface.messageDialogDoneButtonClicked();
        }
    }
}
