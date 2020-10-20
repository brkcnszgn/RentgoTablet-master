package com.creatifsoftware.rentgoservice.view.fragment.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.creatifsoftware.rentgoservice.R;
import com.creatifsoftware.rentgoservice.databinding.FragmentBaseLogoutDialogBinding;
import com.creatifsoftware.rentgoservice.di.Injectable;
import com.creatifsoftware.rentgoservice.utils.SharedPrefUtils;
import com.creatifsoftware.rentgoservice.view.activity.LoginActivity;
import com.creatifsoftware.rentgoservice.view.activity.MainActivity;

public class BaseLogoutDialog extends Fragment implements Injectable {

    private FragmentBaseLogoutDialogBinding binding;
    private MainActivity mActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_base_logout_dialog, container, false);
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

        binding.closeButtonLayout.setOnClickListener(view -> mActivity.onBackPressed());
        binding.cancelButton.setOnClickListener(view -> mActivity.onBackPressed());
        binding.logoutButton.setOnClickListener(view -> {
            SharedPrefUtils.instance.saveObject(mActivity.getApplicationContext(), "user", null);
            SharedPrefUtils.instance.saveObject(mActivity.getApplicationContext(), "username", null);
            SharedPrefUtils.instance.saveObject(mActivity.getApplicationContext(), "password", null);

//            Intent intent = mActivity.getApplicationContext().getPackageManager().
//                    getLaunchIntentForPackage(mActivity.getApplicationContext().getPackageName());
//            if (intent != null){
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//            }

            mActivity.finish();
            Intent loginIntent = new Intent(mActivity, LoginActivity.class);
            startActivity(loginIntent);
        });
    }
}
