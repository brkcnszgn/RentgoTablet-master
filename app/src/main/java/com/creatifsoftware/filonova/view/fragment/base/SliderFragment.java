package com.creatifsoftware.filonova.view.fragment.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.ViewPager;

import com.creatifsoftware.filonova.R;
import com.creatifsoftware.filonova.databinding.FragmentSliderBinding;
import com.creatifsoftware.filonova.view.adapter.ImageAdapter;

import java.util.List;

public class SliderFragment extends DialogFragment {
    private FragmentSliderBinding binding;
    private List<String> list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_slider, container, false);
        return binding.getRoot();
    }

    public SliderFragment(List<String> list) {
        this.list = list;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ViewPager viewPager = view.findViewById(R.id.viewPager);
        ImageAdapter adapter = new ImageAdapter(requireContext(), list);
        viewPager.setAdapter(adapter);
        binding.closeButtonLayout.setOnClickListener(v -> dismiss());
        binding.closeButton.setOnClickListener(v -> dismiss());
    }
}
