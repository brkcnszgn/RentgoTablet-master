package com.creatifsoftware.filonova.view.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.creatifsoftware.filonova.R;
import com.creatifsoftware.filonova.databinding.FragmentEquipmentListBinding;
import com.creatifsoftware.filonova.di.Injectable;
import com.creatifsoftware.filonova.model.AdditionalProduct;
import com.creatifsoftware.filonova.model.AvailabilityGroupCodeInformation;
import com.creatifsoftware.filonova.model.ContractItem;
import com.creatifsoftware.filonova.model.Equipment;
import com.creatifsoftware.filonova.model.GroupCodeInformation;
import com.creatifsoftware.filonova.model.User;
import com.creatifsoftware.filonova.model.base.ResponseResult;
import com.creatifsoftware.filonova.model.response.EquipmentListResponse;
import com.creatifsoftware.filonova.utils.EnumUtils;
import com.creatifsoftware.filonova.utils.LocaleUtils;
import com.creatifsoftware.filonova.utils.RegexUtils;
import com.creatifsoftware.filonova.view.adapter.AvailabilityListAdapter;
import com.creatifsoftware.filonova.view.adapter.EquipmentListAdapter;
import com.creatifsoftware.filonova.view.callback.AvailabilityItemClickCallback;
import com.creatifsoftware.filonova.view.callback.EquipmentClickCallback;
import com.creatifsoftware.filonova.view.fragment.base.BaseFragment;
import com.creatifsoftware.filonova.view.fragment.damageEntry.DamageEntryFragmentForDelivery;
import com.creatifsoftware.filonova.viewmodel.AdditionalProductsViewModel;
import com.creatifsoftware.filonova.viewmodel.EquipmentListViewModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

/**
 * Created by kerembalaban on 12.02.2019 at 22:23.
 */
public class EquipmentListFragment extends BaseFragment implements Injectable {
    private static ContractItem selectedContract;
    private final List<Equipment> equipmentList = new ArrayList<>();
    private final AvailabilityItemClickCallback availabilityItemClickCallback = item -> {
        if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
            // show modal
            mActivity.showCarGroupChangeFragment(selectedContract.groupCodeInformation, item, selectedContract);
        }
    };
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private EquipmentListAdapter equipmentListAdapter;
    private AvailabilityListAdapter availabilityListAdapter;
    private FragmentEquipmentListBinding binding;
    private List<AvailabilityGroupCodeInformation> availabilityGroupCodeInformations;
    private EquipmentListViewModel viewModel;
    private String trackingNumber;
    private int count = 0;
    private GroupCodeInformation contractGroupCodeInformation;
    private Equipment selectedEquipment;
    private EquipmentListResponse equipmentListResponse;
    private final EquipmentClickCallback equipmentClickCallback = item -> {
        if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
            selectedEquipment = item;
//            if (item.statusReason != EnumUtils.EquipmentStatusCode.AVAILABLE.getIntValue()){
//                mActivity.showConfirmationDialog("Tamam","Kullanılabilir Yap",String.format(Locale.getDefault(),"%s plakalı araç teslim edilmeye uygun değildir.\n" + "Durum: %s",item.plateNumber,EnumUtils.EquipmentStatusCode.NONE.getName(item.statusReason)));
//                return;
//            }

            if (item.statusReason != EnumUtils.EquipmentStatusCode.AVAILABLE.getIntValue()) {
                showMessageDialog(String.format(Locale.getDefault(), "Araç teslim edilmeye uygun değildir.\n" + "Durum: %s", EnumUtils.EquipmentStatusCode.NONE.getName(item.statusReason)));
                return;
            }

            selectEquipment(item);
        }
    };

    /**
     * Creates project fragment for specific project ID
     */
    public static EquipmentListFragment forSelectedContract(ContractItem selectedContract) {
        selectedContract.selectedEquipment = null;
        EquipmentListFragment fragment = new EquipmentListFragment();
        Bundle args = new Bundle();

        args.putSerializable(KEY_SELECTED_CONTRACT, selectedContract);
        fragment.setArguments(args);

        return fragment;
    }

    @androidx.annotation.Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @androidx.annotation.Nullable ViewGroup container,
                             @androidx.annotation.Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_equipment_list, container, false);
        prepareEquipmentListRecyclerView();
        prepareAvailabilityListRecyclerView();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@androidx.annotation.Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        if (this.contractGroupCodeInformation == null) {
            this.contractGroupCodeInformation = new GroupCodeInformation(getSelectedContract().groupCodeInformation);
        }

        //mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
        viewModel = ViewModelProviders.of(this,
                viewModelFactory).get(EquipmentListViewModel.class);

        binding.carSelectionTitle.setText(String.format(Locale.getDefault(), "%s - %s (%s)", getString(R.string.equipment_list_title), getSelectedContract().contractNumber, getSelectedContract().pnrNumber));

        getStepView().go(1, true);
        setTextChangeListener();
        observeData();
    }

    @Override
    public void onDestroy() {
        selectedContract.groupCodeInformation = new GroupCodeInformation(this.contractGroupCodeInformation);
        //selectedContract.groupCodeInformation = this.contractGroupCodeInformation;

        super.onDestroy();
    }

    @Override
    public void backButtonClicked() {
        super.backButtonClicked();
    }

    private void observeData() {
        count = 0;
        observeEquipmentListViewModel();
        observeCalculateAvailability();
//        if (!selectedContract.isEquipmentChanged){
//            observeCalculateAvailability();
//        }else{
//            count++;
//        }
    }

    private void setTextChangeListener() {
        binding.plateNumberSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                equipmentListAdapter.setEquipmentList(filter(charSequence.toString()));
                //equipmentListAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void groupCodeChanged(AvailabilityGroupCodeInformation groupCodeInformation) {
        selectedContract.selectedEquipment = null;
        selectedContract.updatedGroupCodeInformation = groupCodeInformation;
        selectedContract.updatedGroupCodeInformation.trackingNumber = trackingNumber;
        selectedContract.groupCodeInformation = groupCodeInformation;

        observeEquipmentListViewModel();
    }

    private void observeEquipmentListViewModel() {
        selectedContract = getSelectedContract();
        if (viewModel.getEquipmentListObservable().hasObservers() && isGroupCodeChanged()) {
            bindEquipmentListViewModal();
            return;
        }
        super.showLoading();
        viewModel.setGroupCodeId(selectedContract.groupCodeInformation.groupCodeId);
        viewModel.setUser(new User().getUser(getContext()));
        viewModel.getEquipmentListObservable().observe(this, response -> {
            count++;
            hideLoading();
            if (response == null) {
                showMessageDialog(getString(R.string.unknown_error_message));
            } else if (response.responseResult.result) {
                //selectedContract.groupCodeInformation.equipmentList = new ArrayList<>();
                //selectedContract.groupCodeInformation.equipmentList = response.equipmentList;
                equipmentListResponse = response;
                bindEquipmentListViewModal();
            } else {
                showMessageDialog(response.responseResult.exceptionDetail);
            }
        });
    }

    private boolean isGroupCodeChanged() {
        return selectedContract.updatedGroupCodeInformation == null;
    }

    private void bindEquipmentListViewModal() {
        binding.searchLayout.setVisibility(View.VISIBLE);
        binding.equipmentListLayout.setVisibility(View.VISIBLE);
        //equipmentListAdapter.setEquipmentList(selectedContract.groupCodeInformation.equipmentList);
        equipmentListAdapter.setEquipmentList(equipmentListResponse.equipmentList);
        binding.setGroupCodeInformation(selectedContract.groupCodeInformation);
        binding.setViewModel(viewModel);
    }

    private void bindAvailabilityListViewModal() {
        if (availabilityGroupCodeInformations != null) {
            binding.availabilityList.setVisibility(View.VISIBLE);
            availabilityGroupCodeInformations.removeIf(item -> (item.groupCodeId.equals(selectedContract.groupCodeInformation.groupCodeId)));
            availabilityListAdapter.setItemList(availabilityGroupCodeInformations);
            binding.setViewModel(viewModel);
        }
    }

    private void observeCalculateAvailability() {
        if (viewModel.getCalculateAvailabilityObservable().hasObservers()) {
            bindAvailabilityListViewModal();
            return;
        }
        viewModel.setContractItem(selectedContract);
        viewModel.setUser(new User().getUser(getContext()));
        viewModel.getCalculateAvailabilityObservable().observe(this, response -> {
            count++;
            hideLoading();
            if (response == null) {
                mActivity.showMessageDialog(getString(R.string.unknown_error_message));
            } else if (response.responseResult.result) {
                availabilityGroupCodeInformations = new ArrayList<>();
                availabilityGroupCodeInformations = response.availabilityData;
                trackingNumber = response.trackingNumber;
                bindAvailabilityListViewModal();
            } else {
                mActivity.showMessageDialog(response.responseResult.exceptionDetail);
            }
        });
    }

    @Override
    protected void hideLoading() {
        if (count == 2) {
            count = 0;
            super.hideLoading();
        }
    }

    private void selectEquipment(Equipment item) {
        for (Equipment equipment : equipmentListResponse.equipmentList) {
            equipment.isSelected = false;
        }
        item.isSelected = !item.isSelected;
        if (item.isSelected) {
            selectedContract.selectedEquipment = item;
        }

        equipmentListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void navigate() {
        super.navigate();
        observeAdditionalProducts();
//        if (selectedContract.updatedGroupCodeInformation == null){
//            observeAdditionalProducts();
//        }else{
//            DamageEntryFragment damageEntryFragment = DamageEntryFragment.forSelectedContract(selectedContract);
//            super.changeFragment(damageEntryFragment);
//        }
    }

    private void observeAdditionalProducts() {
        AdditionalProductsViewModel additionalProductsViewModel = ViewModelProviders.of(this,
                viewModelFactory).get(AdditionalProductsViewModel.class);

        showLoading();
        additionalProductsViewModel.setContractItem(selectedContract);
        if (selectedContract.updatedGroupCodeInformation == null) {
            additionalProductsViewModel.setGroupCodeId(selectedContract.groupCodeInformation.pricingGroupCodeId);
        } else if (selectedContract.updatedGroupCodeInformation.changeType == EnumUtils.EquipmentChangeType.UPSELL.getIntValue() ||
                selectedContract.updatedGroupCodeInformation.changeType == EnumUtils.EquipmentChangeType.DOWNSELL.getIntValue()) {
            additionalProductsViewModel.setGroupCodeId(selectedContract.updatedGroupCodeInformation.groupCodeId);
        } else {
            additionalProductsViewModel.setGroupCodeId(contractGroupCodeInformation.pricingGroupCodeId);
        }

//        if (selectedContract.updatedGroupCodeInformation == null){
//            additionalProductsViewModel.setGroupCodeId(selectedContract.groupCodeInformation.groupCodeId);
//        }else if (selectedContract.updatedGroupCodeInformation.changeType == EnumUtils.EquipmentChangeType.UPGRADE.getIntValue() ||
//                selectedContract.updatedGroupCodeInformation.changeType == EnumUtils.EquipmentChangeType.DOWNGRADE.getIntValue()){
//            additionalProductsViewModel.setGroupCodeId(contractGroupCodeInformation.groupCodeId);
//        }else{
//            additionalProductsViewModel.setGroupCodeId(selectedContract.updatedGroupCodeInformation.groupCodeId);
//        }

        additionalProductsViewModel.getAdditionalProductListObservable().observe(this, response -> {
            super.hideLoading();
            if (response == null) {
                showMessageDialog(getString(R.string.unknown_error_message));
            } else if (!response.responseResult.result) {
                showMessageDialog(response.responseResult.exceptionDetail);
            } else {
                selectedContract.addedAdditionalProducts = new ArrayList<>();
                selectedContract.addedAdditionalServices = new ArrayList<>();
                selectedContract.additionalProductRules = new ArrayList<>();

                for (AdditionalProduct item : response.additionalProductData) {
                    //item.isFromReservation = item.isChecked;
                    selectedContract.initialAdditionalProductList.add(new AdditionalProduct(item));
                }

                for (AdditionalProduct item : response.additionalProductData) {
                    if (item.tobePaidAmount > 0 && item.value == 0) {
                        item.value = 1;
                        selectedContract.addedAdditionalServices.add(new AdditionalProduct(item));
                    }
                }

                selectedContract.additionalProductList = response.additionalProductData;
                selectedContract.additionalProductRules = response.additionalProductRuleData;

                //getBlobImages();
                DamageEntryFragmentForDelivery damageEntryFragment = DamageEntryFragmentForDelivery.forSelectedContract(selectedContract);
                changeFragment(damageEntryFragment);
            }
        });
    }

//    public void getBlobImages(){
//        new Thread(() -> {
//            try  {
//                String[] blobsName = BlobStorageManager.instance.ListImages(selectedContract.selectedEquipment,selectedContract.contractNumber,"delivery");
//                for (String name : blobsName){
//                    deleteBlobImage(name);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).start();
//    }
//
//    private String getBlobImageName(String blobName){
//        return BlobStorageManager.instance.prepareEquipmentImageName(selectedContract.selectedEquipment,
//                selectedContract.contractNumber,
//                "delivery",
//                blobName);
//    }
//
//    public void deleteBlobImage(String blobName){
//        //upload image
//        new Thread(() -> {
//            try  {
//                BlobStorageManager.instance.deleteImage(BlobStorageManager.instance.getEquipmentsContainerName(),getBlobImageName(blobName));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).start();
//    }

    @Override
    protected ResponseResult checkBeforeNavigate() {
        if (selectedContract.selectedEquipment == null) {
            return new ResponseResult(false, getString(R.string.no_selected_equipment_error));
        }

        return super.checkBeforeNavigate();
    }

    private void prepareEquipmentListRecyclerView() {
        equipmentListAdapter = new EquipmentListAdapter(equipmentClickCallback, getSelectedContract());
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.equipmentList.setLayoutManager(layoutManager);
        binding.equipmentList.setAdapter(equipmentListAdapter);
    }

    private void prepareAvailabilityListRecyclerView() {
        availabilityListAdapter = new AvailabilityListAdapter(availabilityItemClickCallback);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.availabilityList.setLayoutManager(layoutManager);
        binding.availabilityList.setAdapter(availabilityListAdapter);
    }

    public List<Equipment> filter(String query) {
        List<Equipment> tempList = equipmentListResponse.equipmentList;
        if (TextUtils.isEmpty(query)) {
            return tempList;
        }

        query = query.toLowerCase(LocaleUtils.TR);
        Pattern pattern = Pattern.compile(RegexUtils.instance.createTurkishRegexFor(query));
        HashSet<Equipment> filtered = new LinkedHashSet<>();
        for (Equipment item : tempList) {
            Matcher plateNumberMatcher = pattern.matcher(item.plateNumber.toLowerCase(LocaleUtils.TR));
            Matcher brandNameMatcher = pattern.matcher(item.brandName.toLowerCase(LocaleUtils.TR));
            Matcher modelNameMatcher = pattern.matcher(item.modelName.toLowerCase(LocaleUtils.TR));
            if (plateNumberMatcher.find() || brandNameMatcher.find() || modelNameMatcher.find()) {
                filtered.add(item);
            }
        }

        return new ArrayList<>(filtered);
    }

    @Override
    public void confirmationButtonClicked() {
        super.confirmationButtonClicked();
        showLoading();
        viewModel.getUpdateEquipmentStatusObservable(selectedEquipment, 1).observe(this, response -> {
            super.hideLoading();
            if (response.responseResult.result) {
                selectedEquipment.statusReason = 1;
                selectEquipment(selectedEquipment);
            } else {
                Toast.makeText(mActivity, response.responseResult.exceptionDetail, Toast.LENGTH_LONG).show();
            }
        });
    }
}
