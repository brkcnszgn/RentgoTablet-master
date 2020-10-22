package com.creatifsoftware.filonova.view.fragment.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.creatifsoftware.filonova.R;
import com.creatifsoftware.filonova.databinding.FragmentBaseLoadingDialogBinding;
import com.creatifsoftware.filonova.di.Injectable;

/**
 * Created by kerembalaban on 7.03.2019 at 12:18.
 */
public class BaseLoadingDialog extends Fragment implements Injectable {
    private FragmentBaseLoadingDialogBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_base_loading_dialog, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.avi.show();
    }

    @Override
    public void onStop() {
        if (binding != null) {
            binding.avi.hide();
        }

        super.onStop();
    }
}

