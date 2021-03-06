package com.creatifsoftware.rentgoservice.view.fragment.base;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.creatifsoftware.rentgoservice.R;
import com.creatifsoftware.rentgoservice.databinding.FragmentBaseDamagePartSelectionBinding;
import com.creatifsoftware.rentgoservice.di.Injectable;
import com.creatifsoftware.rentgoservice.interfaces.CommunicationInterface;
import com.creatifsoftware.rentgoservice.model.DamageDocument;
import com.creatifsoftware.rentgoservice.model.DamageInfo;
import com.creatifsoftware.rentgoservice.model.DamageItem;
import com.creatifsoftware.rentgoservice.model.DamageSize;
import com.creatifsoftware.rentgoservice.model.DamageType;
import com.creatifsoftware.rentgoservice.model.EquipmentPart;
import com.creatifsoftware.rentgoservice.model.User;
import com.creatifsoftware.rentgoservice.utils.CommonMethods;
import com.creatifsoftware.rentgoservice.utils.EnumUtils;
import com.creatifsoftware.rentgoservice.utils.ImageUtil;
import com.creatifsoftware.rentgoservice.view.activity.MainActivity;
import com.creatifsoftware.rentgoservice.view.adapter.DamageDocumentListAdapter;
import com.creatifsoftware.rentgoservice.view.adapter.DamageSizeListAdapter;
import com.creatifsoftware.rentgoservice.view.adapter.DamageTypeListAdapter;
import com.creatifsoftware.rentgoservice.view.adapter.EquipmentPartListAdapter;
import com.creatifsoftware.rentgoservice.view.callback.DamageDocumentClickCallback;
import com.creatifsoftware.rentgoservice.view.callback.DamageSizeClickCallback;
import com.creatifsoftware.rentgoservice.view.callback.DamageTypeClickCallback;
import com.creatifsoftware.rentgoservice.view.callback.EquipmentPartClickCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;

/**
 * Created by kerembalaban on 7.03.2019 at 12:18.
 */

public class BaseEquipmentPartSelectionDialog extends Fragment implements Injectable {
    private static final int REQUEST_TAKE_PHOTO = 100;
    private static final int MY_CAMERA_PERMISSION_CODE = 101;
    private static final String KEY_SHOULD_HIDE_DAMAGE_DOCUMENT = "hide_damage_document";
    private static final String KEY_PART_LIST = "key_part_list";
    private static final String KEY_DOCUMENT_ID = "document_id";

    private FragmentBaseDamagePartSelectionBinding binding;
    private MainActivity mActivity;

    private EquipmentPartListAdapter equipmentPartListAdapter;
    private DamageTypeListAdapter damageTypeListAdapter;
    private DamageSizeListAdapter damageSizeListAdapter;
    private DamageDocumentListAdapter damageDocumentListAdapter;

    private DamageSize selectedDamageSize;
    private DamageType selectedDamageType;
    private DamageDocument selectedDamageDocument;
    private EquipmentPart selectedEquipmentPart;

    private List<EquipmentPart> equipmentPartList = new ArrayList<>();
    private final EquipmentPartClickCallback equipmentPartClickCallback = item -> {
        if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
            //first deselect all item
            for (EquipmentPart equipmentPart : equipmentPartList) {
                equipmentPart.isSelected = false;
            }

            //then select clicked item
            item.isSelected = true;
            selectedEquipmentPart = item;
            enableAddDamageButton();
            equipmentPartListAdapter.notifyDataSetChanged();
        }
    };
    private List<DamageSize> damageSizeList = new ArrayList<>();
    private final DamageSizeClickCallback damageSizeClickCallback = item -> {
        if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
            //first deselect all item
            for (DamageSize damageSize : damageSizeList) {
                damageSize.isSelected = false;
            }

            //then select clicked item
            item.isSelected = true;
            selectedDamageSize = item;
            enableAddDamageButton();
            damageSizeListAdapter.notifyDataSetChanged();
        }
    };
    private List<DamageType> damageTypeList = new ArrayList<>();
    private final DamageTypeClickCallback damageTypeClickCallback = item -> {
        if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
            //first deselect all item
            for (DamageType damageType : damageTypeList) {
                damageType.isSelected = false;
            }

            //then select clicked item
            item.isSelected = true;
            selectedDamageType = item;
            enableAddDamageButton();
            damageTypeListAdapter.notifyDataSetChanged();
        }
    };
    private List<DamageDocument> damageDocumentList = new ArrayList<>();
    private final DamageDocumentClickCallback damageDocumentClickCallback = item -> {
        if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
            //first deselect all item
            for (DamageDocument damageDocument : damageDocumentList) {
                damageDocument.isSelected = false;
            }

            //then select clicked item
            item.isSelected = true;
            selectedDamageDocument = item;
            enableAddDamageButton();
            damageDocumentListAdapter.notifyDataSetChanged();
        }
    };

    public static BaseEquipmentPartSelectionDialog with(ArrayList<EquipmentPart> partList, boolean shouldHideDamageDocumentSelection) {
        BaseEquipmentPartSelectionDialog fragment = new BaseEquipmentPartSelectionDialog();
        Bundle args = new Bundle();

        args.putBoolean(KEY_SHOULD_HIDE_DAMAGE_DOCUMENT, shouldHideDamageDocumentSelection);
        args.putSerializable(KEY_PART_LIST, partList);

        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_base_damage_part_selection, container, false);
        //prepareEquipmentPartRecyclerView();
        prepareDamageTypeRecyclerView();
        prepareDamageSizeRecyclerView();
        prepareDamageDocumentRecyclerView();
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
            boolean shouldHideDamageDocumentLayout = getArguments().getBoolean(KEY_SHOULD_HIDE_DAMAGE_DOCUMENT);
            equipmentPartList = (ArrayList<EquipmentPart>) getArguments().getSerializable(KEY_PART_LIST);
            prepareEquipmentPartRecyclerView();

            if (shouldHideDamageDocumentLayout) {
                binding.damageDocumentTitle.setVisibility(View.GONE);
                binding.damageDocumentList.setVisibility(View.GONE);
                for (DamageDocument damageDocument : CommonMethods.instance.getDamageDocumentList(getContext())) {
                    if (damageDocument.damageDocumentType == EnumUtils.DamageDocumentType.NONE.getIntValue()) {
                        selectedDamageDocument = damageDocument;
                        break;
                    }
                }
            }
        }

        binding.closeButtonLayout.setOnClickListener(view -> mActivity.onBackPressed());
        binding.cancelButton.setOnClickListener(view -> mActivity.onBackPressed());
        binding.confirmButton.setOnClickListener(view -> {
            //damageItem = prepareDamageObject();
            openCameraIntent();
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCameraIntent();
            } else {
                Toast.makeText(getContext(), "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void openCameraIntent() {
        if (mActivity.checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    MY_CAMERA_PERMISSION_CODE);
        } else {
            Intent intent = ImageUtil.instance.dispatchTakePictureIntent(mActivity);
            startActivityForResult(intent, REQUEST_TAKE_PHOTO);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUEST_TAKE_PHOTO) {
            CommunicationInterface myInterface = (CommunicationInterface) getActivity();
            if (myInterface != null) {
                mActivity.getSupportFragmentManager().popBackStackImmediate();
                DamageItem damageItem = prepareDamageObject();
                myInterface.addDamageItem(damageItem);
            }
        }
    }

    private DamageItem prepareDamageObject() {
        DamageItem damageItem = new DamageItem();
        damageItem.damageInfo = new DamageInfo();

        damageItem.damageDocument = selectedDamageDocument;
        damageItem.damageType = selectedDamageType;
        damageItem.damageSize = selectedDamageSize;
        damageItem.equipmentPart = selectedEquipmentPart;
        damageItem.damageInfo.damageBranch = new User().getUser(mActivity).userBranch;
        //damageItem.damagePhotoFile = ImageUtil.instance.getImageFile();
        try {
            damageItem.damagePhotoFile = new Compressor(mActivity.getApplicationContext()).setQuality(50).compressToFile(ImageUtil.instance.getImageFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        ImageUtil.instance.removeImageFile();

        return damageItem;
    }

    private void prepareEquipmentPartRecyclerView() {
        //equipmentPartList = CommonMethods.instance.getEquipmentPartList(getContext());
        equipmentPartListAdapter = new EquipmentPartListAdapter(equipmentPartClickCallback);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        equipmentPartListAdapter.setItemList(equipmentPartList);
        binding.damagePartList.setLayoutManager(layoutManager);
        binding.damagePartList.setAdapter(equipmentPartListAdapter);
    }

    private void prepareDamageTypeRecyclerView() {
        damageTypeList = CommonMethods.instance.getDamageTypeList(getContext());
        damageTypeListAdapter = new DamageTypeListAdapter(damageTypeClickCallback);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        damageTypeListAdapter.setItemList(damageTypeList);
        binding.damageTypeList.setLayoutManager(layoutManager);
        binding.damageTypeList.setAdapter(damageTypeListAdapter);
    }

    private void prepareDamageSizeRecyclerView() {
        damageSizeList = CommonMethods.instance.getDamageSizeList(getContext());
        damageSizeListAdapter = new DamageSizeListAdapter(damageSizeClickCallback);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        damageSizeListAdapter.setItemList(damageSizeList);
        binding.damageSizeList.setLayoutManager(layoutManager);
        binding.damageSizeList.setAdapter(damageSizeListAdapter);
    }

    private void prepareDamageDocumentRecyclerView() {
        damageDocumentList = CommonMethods.instance.getDamageDocumentList(getContext());
        damageDocumentListAdapter = new DamageDocumentListAdapter(damageDocumentClickCallback);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        damageDocumentListAdapter.setItemList(damageDocumentList);
        binding.damageDocumentList.setLayoutManager(layoutManager);
        binding.damageDocumentList.setAdapter(damageDocumentListAdapter);
    }

    private void enableAddDamageButton() {
        if (selectedEquipmentPart != null &&
                selectedDamageType != null &&
                selectedDamageSize != null &&
                selectedDamageDocument != null) {

            binding.confirmButton.setAlpha(1);
            binding.confirmButton.setEnabled(true);
        }
    }
}
