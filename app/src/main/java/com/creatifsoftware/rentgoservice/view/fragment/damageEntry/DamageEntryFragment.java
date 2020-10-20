package com.creatifsoftware.rentgoservice.view.fragment.damageEntry;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.creatifsoftware.rentgoservice.R;
import com.creatifsoftware.rentgoservice.databinding.FragmentDamageEntryBinding;
import com.creatifsoftware.rentgoservice.di.Injectable;
import com.creatifsoftware.rentgoservice.model.DamageItem;
import com.creatifsoftware.rentgoservice.model.Equipment;
import com.creatifsoftware.rentgoservice.model.EquipmentPart;
import com.creatifsoftware.rentgoservice.utils.CommonMethods;
import com.creatifsoftware.rentgoservice.view.adapter.DamageListAdapter;
import com.creatifsoftware.rentgoservice.view.callback.DamageItemClickCallback;
import com.creatifsoftware.rentgoservice.view.callback.DamageItemDeleteButtonCallback;
import com.creatifsoftware.rentgoservice.view.fragment.base.BaseFragment;
import com.creatifsoftware.rentgoservice.viewmodel.DamageEntryViewModel;
import com.richpath.RichPath;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by kerembalaban on 16.02.2019 at 18:38.
 */
public class DamageEntryFragment extends BaseFragment implements Injectable {
    final DamageItemClickCallback damageItemClickCallback = item -> {
        if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
            mActivity.showImagePreview(item);
        }
    };
    public boolean hasBlobStorageError = false;
    FragmentDamageEntryBinding binding;
    DamageListAdapter damageListAdapter;
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private Equipment selectedEquipment;
    final DamageItemDeleteButtonCallback damageItemDeleteButtonCallback = item -> {
        if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
            selectedEquipment.damageList.remove(item);
            drawAllDamageItemsToModel();
            damageListAdapter.notifyDataSetChanged();
            deleteBlobImage(item);
        }
    };
    private String selectedPathName;
    private boolean isServiceCalled = false;
    private DamageEntryViewModel viewModel;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(DamageEntryViewModel.class);

        Bundle bundle = getArguments();
        if (bundle != null) {
            selectedEquipment = (Equipment) bundle.getSerializable(KEY_SELECTED_EQUIPMENT);
        }

        observeViewModel(viewModel);
        binding.damageEntryForm.setOnPathClickListener(richPath -> {
            selectedPathName = richPath.getName();
            showEquipmentPartSelection();
        });

        binding.damageEntrySegment.setOnPositionChangedListener(position -> {
            ScaleAnimation fade_in = new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            fade_in.setDuration(500);
            fade_in.setFillAfter(true);
            binding.damageEntryForm.startAnimation(fade_in);
            binding.damageEntryWireframe.startAnimation(fade_in);

            if (position == 0) {
                binding.damageEntryForm.setVectorDrawable(R.drawable.ic_front_vector);
                binding.damageEntryWireframe.setImageResource(R.drawable.ic_front_wireframe);
            } else {
                binding.damageEntryForm.setVectorDrawable(R.drawable.ic_rear_vector);
                binding.damageEntryWireframe.setImageResource(R.drawable.ic_rear_wireframe);
            }
            drawAllDamageItemsToModel(position);
        });

        binding.addOtherDamageLayout.setOnClickListener(view -> {
            selectedPathName = "27000001";
            showEquipmentPartSelection();
        });
        //observeMasterData(viewModel);
    }

    ArrayList<EquipmentPart> getGroupIdBySelectedPath() {
        String mainPartId = "";
        ArrayList<EquipmentPart> subPartList = new ArrayList<>();

        if (CommonMethods.instance.getEquipmentPartList(mActivity) == null) {
            showMessageDialog("Araç parçalarının sistemden alınması bekleniyor, tekrar deneyin");
        }

        for (EquipmentPart item : CommonMethods.instance.getEquipmentPartList(mActivity)) {
            if (item.equipmentSubPartId.equals(selectedPathName)) {
                mainPartId = item.equipmentMainPartId;
            }
        }

        for (EquipmentPart item : CommonMethods.instance.getEquipmentPartList(mActivity)) {
            if (item.equipmentMainPartId.equals(mainPartId)) {
                subPartList.add(item);
            }
        }

        return subPartList;
    }

    @Override
    public void backButtonClicked() {
        super.backButtonClicked();
        for (DamageItem item : selectedEquipment.damageList) {
            if (item.damageInfo.isNewDamage) {
                deleteBlobImage(item);
            }
        }
    }

    public void showEquipmentPartSelection() {

    }

    private void observeViewModel(final DamageEntryViewModel viewModel) {
        if (viewModel.getDamageListByEquipment().hasObservers()) {
            damageListAdapter.setDamageList(selectedEquipment.damageList);
            return;
        }

        super.showLoading();
        viewModel.setEquipmentId(selectedEquipment.equipmentId);
        viewModel.getDamageListByEquipment().observe(this, damageListResponse -> {
            hideLoading();
            if (damageListResponse == null) {
                isServiceCalled = true;
                showMessageDialog(getString(R.string.unknown_error_message));
            } else if (damageListResponse.responseResult.result) {
                selectedEquipment.damageList = damageListResponse.damageList;
                drawAllDamageItemsToModel();
                damageListAdapter.setDamageList(damageListResponse.damageList);
            } else {
                isServiceCalled = true;
                showMessageDialog(getString(R.string.unknown_error_message));
            }
        });
    }

    private void drawAllDamageItemsToModel() {
        for (DamageItem item : selectedEquipment.damageList) {
            RichPath richPath = binding.damageEntryForm.findRichPathByName(item.equipmentPart.equipmentSubPartId);
            if (richPath != null) {
                richPath.setFillColor(R.color.colorMainBlue);
            }
        }
    }

    private void drawAllDamageItemsToModel(int position) {
        for (DamageItem item : selectedEquipment.damageList) {
            RichPath richPath = binding.damageEntryForm.findRichPathByName(item.equipmentPart.equipmentSubPartId);
            if (richPath != null) {
                richPath.setFillColor(R.color.colorMainBlue);
            }
        }

        if (selectedEquipment.damageList.size() == 0) {
            if (position == 0) {
                binding.damageEntryForm.setVectorDrawable(R.drawable.ic_front_vector);
                binding.damageEntryWireframe.setImageResource(R.drawable.ic_front_wireframe);
            } else {
                binding.damageEntryForm.setVectorDrawable(R.drawable.ic_rear_vector);
                binding.damageEntryWireframe.setImageResource(R.drawable.ic_rear_wireframe);
            }
        }
    }

    void drawDamageItemsToModelByEquipmentSubPart(String equipmentSubPartId) {
        RichPath selectedDamageRichPath = binding.damageEntryForm.findRichPathByName(equipmentSubPartId);
        if (selectedDamageRichPath != null) {
            selectedDamageRichPath.setFillColor(R.color.colorMainRed);
        }
    }

    @Override
    public void messageDialogDoneButtonClicked() {
        if (isServiceCalled) {
            super.hideLoading();
            viewModel.getDamageListByEquipment().removeObservers(this);
            mActivity.onBackPressed();
        }
    }

    public void addDamageItemToDamageList(DamageItem damageItem) {

    }

    public void deleteBlobImage(DamageItem damageItem) {

    }
}
