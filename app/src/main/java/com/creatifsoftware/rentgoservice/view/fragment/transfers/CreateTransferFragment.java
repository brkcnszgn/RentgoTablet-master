package com.creatifsoftware.rentgoservice.view.fragment.transfers;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.creatifsoftware.rentgoservice.R;
import com.creatifsoftware.rentgoservice.databinding.FragmentTransferCreateBinding;
import com.creatifsoftware.rentgoservice.di.Injectable;
import com.creatifsoftware.rentgoservice.model.Branch;
import com.creatifsoftware.rentgoservice.model.TransferItem;
import com.creatifsoftware.rentgoservice.model.User;
import com.creatifsoftware.rentgoservice.model.base.ResponseResult;
import com.creatifsoftware.rentgoservice.utils.CommonMethods;
import com.creatifsoftware.rentgoservice.utils.EnumUtils;
import com.creatifsoftware.rentgoservice.view.adapter.ExpandableBranchListAdapter;
import com.creatifsoftware.rentgoservice.view.fragment.base.BaseFragment;
import com.creatifsoftware.rentgoservice.view.fragment.dashboards.TransferDashboardFragment;
import com.creatifsoftware.rentgoservice.viewmodel.CreateTransferViewModel;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by kerembalaban on 24.04.2019 at 17:27.
 */
public class CreateTransferFragment extends BaseFragment implements Injectable {
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private FragmentTransferCreateBinding binding;
    private CreateTransferViewModel viewModel;
    private boolean isServiceResponseSuccess = false;
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;

    static CreateTransferFragment withTransferItem(TransferItem transferItem) {
        CreateTransferFragment fragment = new CreateTransferFragment();
        Bundle args = new Bundle();

        args.putSerializable(KEY_SELECTED_TRANSFER, transferItem);
        fragment.setArguments(args);

        return fragment;
    }

    @androidx.annotation.Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @androidx.annotation.Nullable ViewGroup container,
                             @androidx.annotation.Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_transfer_create, container, false);
        prepareExpandableBranchListAdapter();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this,
                viewModelFactory).get(CreateTransferViewModel.class);

        mActivity.showProgressBar();
        binding.setTransfer(selectedTransfer);

        if (selectedTransfer.transferType == EnumUtils.TransferType.BRANCH.getIntValue()) {
            selectedTransfer.dropoffBranch = null;
            binding.branchList.setVisibility(View.VISIBLE);
            binding.serviceNameLayout.setVisibility(View.GONE);
        } else if (selectedTransfer.transferType == EnumUtils.TransferType.DAMAGE.getIntValue() ||
                selectedTransfer.transferType == EnumUtils.TransferType.FAULT.getIntValue() ||
                selectedTransfer.transferType == EnumUtils.TransferType.SERVICE.getIntValue()) {

            binding.branchList.setVisibility(View.GONE);
            binding.serviceNameLayout.setVisibility(View.VISIBLE);
        } else {
            binding.branchList.setVisibility(View.GONE);
            binding.serviceNameLayout.setVisibility(View.GONE);
        }

        binding.transferType.setText(EnumUtils.TransferType.NONE.getName(selectedTransfer.transferType));
        binding.pickupDateLayout.setOnClickListener(view -> datePicker(view.getId()));
        binding.returnDateLayout.setOnClickListener(view -> datePicker(view.getId()));
        binding.serviceNameLayout.setOnClickListener(view -> mActivity.showInputDialog(getString(R.string.service_name_hint_text), 1, "Servis adı boş bırakılamaz"));
    }

    @Override
    public void backButtonClicked() {
        super.backButtonClicked();
        mActivity.hideProgressBar();
    }

    private void prepareExpandableBranchListAdapter() {
        ExpandableBranchListAdapter expandableBranchListAdapter = new ExpandableBranchListAdapter(getContext(), "Ek Bedeller");
        expandableBranchListAdapter.setDetailList(CommonMethods.instance.getBranchList(getContext()));
        binding.branchList.setAdapter(expandableBranchListAdapter);
        binding.branchList.setOnChildClickListener((expandableListView, view, i, i1, l) -> {
            List<Branch> tempBranchList = CommonMethods.instance.getBranchList(getContext());
            tempBranchList.get(i1).isSelected = true;
            selectedTransfer.dropoffBranch = tempBranchList.get(i1);
            expandableBranchListAdapter.setDetailList(tempBranchList);
            binding.branchList.collapseGroup(0);
            return false;
        });
    }

    private void datePicker(int layoutId) {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(mActivity,
                (view, year, monthOfYear, dayOfMonth) -> {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    timePicker(layoutId);
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void timePicker(int layoutId) {
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(mActivity,
                (view, hourOfDay, minute) -> {
                    mHour = hourOfDay;
                    mMinute = minute;

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, mYear);
                    calendar.set(Calendar.MONTH, mMonth);
                    calendar.set(Calendar.DAY_OF_MONTH, mDay);
                    calendar.set(Calendar.HOUR_OF_DAY, mHour);// for 6 hour
                    calendar.set(Calendar.MINUTE, mMinute);// for 0 min
                    calendar.set(Calendar.SECOND, 0);// for 0 sec
                    if (layoutId == binding.pickupDateLayout.getId()) {
                        selectedTransfer.estimatedPickupTimestamp = (calendar.getTimeInMillis() / 1000);
                        binding.transferStartDateTimeCheckbox.setChecked(true);
                    } else if (layoutId == binding.returnDateLayout.getId()) {
                        selectedTransfer.estimatedDropoffTimestamp = (calendar.getTimeInMillis() / 1000);
                        binding.transferEndDateTimeCheckbox.setChecked(true);
                    }

                    binding.setTransfer(selectedTransfer);
                }, mHour, mMinute, true);
        timePickerDialog.show();
    }

    @Override
    protected ResponseResult checkBeforeNavigate() {
        if (selectedTransfer.transferType == EnumUtils.TransferType.BRANCH.getIntValue() &&
                selectedTransfer.dropoffBranch == null) {
            return new ResponseResult(false, getString(R.string.transfer_branch_empty_error));
        }
        if ((selectedTransfer.transferType == EnumUtils.TransferType.DAMAGE.getIntValue() ||
                selectedTransfer.transferType == EnumUtils.TransferType.FAULT.getIntValue() ||
                selectedTransfer.transferType == EnumUtils.TransferType.SERVICE.getIntValue()) &&
                !binding.transferLocationCheckbox.isChecked()) {
            return new ResponseResult(false, getString(R.string.service_name_empty_error));
        }
        if (!binding.transferStartDateTimeCheckbox.isChecked()) {
            return new ResponseResult(false, getString(R.string.planned_begin_date_empty_message));
        }
        if (!binding.transferEndDateTimeCheckbox.isChecked()) {
            return new ResponseResult(false, getString(R.string.planned_end_date_empty_message));
        }

        return super.checkBeforeNavigate();
    }

    @Override
    public void navigate() {
        mActivity.showInputDialog("Transfer açıklamasını girin", 2, "Açıklama alanı boş bırakılamaz");

//        String message = String.format("%s plakalı araç için %s transferi oluşturulacaktır, emin misiniz?"
//                ,selectedTransfer.selectedEquipment.plateNumber,EnumUtils.TransferType.NONE.getName(selectedTransfer.transferType));
//
//        mActivity.showConfirmationDialog(getString(R.string.dialog_button_cancel),getString(R.string.dialog_button_yes),message);
    }

    @Override
    public void confirmationButtonClicked() {
        super.confirmationButtonClicked();
        createTransfer();
    }

    private void createTransfer() {
        super.showLoading();
        viewModel.setTransferItem(selectedTransfer);
        viewModel.setUser(new User().getUser(getContext()));
        viewModel.getCreateTransferObservable().observe(getViewLifecycleOwner(), response -> {
            super.hideLoading();
            if (response == null) {
                showMessageDialog(getString(R.string.unknown_error_message));
            } else if (response.responseResult.result) {
                isServiceResponseSuccess = true;
                showMessageDialog(getString(R.string.create_transfer_dialog_success_message));
            } else {
                showMessageDialog(response.responseResult.exceptionDetail);
            }
        });
    }

    @Override
    public void messageDialogDoneButtonClicked() {
        super.messageDialogDoneButtonClicked();
        if (isServiceResponseSuccess) {
            mActivity.hideProgressBar();
            TransferDashboardFragment fragment = new TransferDashboardFragment();
            mActivity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_layout, fragment, "").commit();
        }
    }

    @Override
    public void getInputValue(String input, Integer tag) {
        if (tag == 1) {
            binding.serviceName.setText(input);
            selectedTransfer.serviceName = input;
            binding.transferLocationCheckbox.setChecked(true);
        } else if (tag == 2) {
            selectedTransfer.description = input;
            String message = String.format("%s plakalı araç için %s transferi oluşturulacaktır, emin misiniz?"
                    , selectedTransfer.selectedEquipment.plateNumber, EnumUtils.TransferType.NONE.getName(selectedTransfer.transferType));
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    mActivity.showConfirmationDialog(getString(R.string.dialog_button_cancel), getString(R.string.dialog_button_yes), message);
                }
            }, 500);

        }
    }
}
