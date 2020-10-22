package com.creatifsoftware.filonova.view.fragment.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.creatifsoftware.filonova.R;
import com.creatifsoftware.filonova.databinding.FragmentBasePreviewPhotoBinding;
import com.creatifsoftware.filonova.di.Injectable;
import com.creatifsoftware.filonova.model.DamageItem;
import com.creatifsoftware.filonova.view.activity.MainActivity;
import com.squareup.picasso.Picasso;

/**
 * Created by kerembalaban on 7.03.2019 at 12:18.
 */
public class BasePreviewImageFragment extends Fragment implements Injectable {
    private static final String KEY_IMAGE_BITMAP = "image_bitmap";
    private static final String KEY_DAMAGE_ITEM = "damage_item";
    private Activity activity;

    private FragmentBasePreviewPhotoBinding binding;

    public static BasePreviewImageFragment withImageBitmap(Bitmap bitmap) {
        BasePreviewImageFragment fragment = new BasePreviewImageFragment();
        Bundle args = new Bundle();

        args.putParcelable(KEY_IMAGE_BITMAP, bitmap);
        fragment.setArguments(args);

        return fragment;
    }

    public static BasePreviewImageFragment withDamageItem(DamageItem item) {
        BasePreviewImageFragment fragment = new BasePreviewImageFragment();
        Bundle args = new Bundle();

        args.putSerializable(KEY_DAMAGE_ITEM, item);
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_base_preview_photo, container, false);
        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            this.activity = (MainActivity) context;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();

//        Bitmap bitmap = null;
//        if (bundle != null){
//            bitmap = getArguments().getParcelable(KEY_IMAGE_BITMAP);
//        }
        DamageItem damageItem = new DamageItem();
        if (getArguments() != null) {
            damageItem = (DamageItem) getArguments().getSerializable(KEY_DAMAGE_ITEM);
        }
        if (damageItem != null) {
            if (!damageItem.blobStoragePath.isEmpty()) {
                Picasso.get().load(damageItem.blobStoragePath).into(binding.imageToPreview);
            }
            if (damageItem.damagePhotoFile != null) {
                Picasso.get().load(damageItem.damagePhotoFile).into(binding.imageToPreview);
            }
        }

        //binding.imageToPreview.setImageBitmap(bitmap);
        binding.closeButtonLayout.setOnClickListener(view -> activity.onBackPressed());
    }
}
