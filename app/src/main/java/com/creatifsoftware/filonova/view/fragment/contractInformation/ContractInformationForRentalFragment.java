package com.creatifsoftware.filonova.view.fragment.contractInformation;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.creatifsoftware.filonova.BuildConfig;
import com.creatifsoftware.filonova.R;
import com.creatifsoftware.filonova.databinding.FragmentContractInformationForRentalBinding;
import com.creatifsoftware.filonova.di.Injectable;
import com.creatifsoftware.filonova.model.ContractItem;
import com.creatifsoftware.filonova.model.User;
import com.creatifsoftware.filonova.model.request.GetContractInformationRequest;
import com.creatifsoftware.filonova.utils.BlobStorageManager;
import com.creatifsoftware.filonova.utils.EnumUtils;
import com.creatifsoftware.filonova.view.fragment.base.BaseFragment;
import com.creatifsoftware.filonova.view.fragment.damageEntry.DamageEntryFragmentForRental;
import com.creatifsoftware.filonova.viewmodel.ContractInformationViewModel;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

/**
 * Created by kerembalaban on 16.02.2019 at 18:38.
 */
public class ContractInformationForRentalFragment extends BaseFragment implements Injectable {

    public FragmentContractInformationForRentalBinding binding;
    public ContractInformationViewModel viewModel;
    String date_time = "";
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private Date selectedDropoffDateTime;
    private boolean isServiceCalled = true;
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;

    public static ContractInformationForRentalFragment forSelectedContract(ContractItem selectedContract) {
        ContractInformationForRentalFragment fragment = new ContractInformationForRentalFragment();
        Bundle args = new Bundle();

        args.putSerializable(KEY_SELECTED_CONTRACT, selectedContract);
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_contract_information_for_rental, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getStepView().go(0, true);
        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(ContractInformationViewModel.class);
        mActivity.showProgressBar();

        //observeContractInformation(getSelectedContract().contractId);
        binding.contractInformationTitle.setText(String.format(Locale.getDefault(), "%s - %s (%s)", getString(R.string.contract_information), selectedContract.contractNumber, selectedContract.pnrNumber));
        binding.dropoffDateTime.setOnClickListener(view -> datePicker());
        bindViewModal();
    }

    private void datePicker() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(mActivity,
                (view, year, monthOfYear, dayOfMonth) -> {

                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    timePicker();
                }, mYear, mMonth, mDay);
        //datePickerDialog.getDatePicker().setMinDate(selectedContract.pickupTimestamp);
        String userId = new User().getUser(getContext()).userId;
        if (BuildConfig.IS_PROD && !userId.equals("cf4b9a11-75b3-e911-a851-000d3a2dd1b4")) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -1);
            datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis());
        }

        datePickerDialog.show();
    }

    private void timePicker() {
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(mActivity,
                (view, hourOfDay, minute) -> {
                    mHour = hourOfDay;
                    mMinute = minute;

                    selectedContract.isManuelProcess = true;
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, mYear);
                    calendar.set(Calendar.MONTH, mMonth);
                    calendar.set(Calendar.DAY_OF_MONTH, mDay);
                    calendar.set(Calendar.HOUR_OF_DAY, mHour);// for 6 hour
                    calendar.set(Calendar.MINUTE, mMinute);// for 0 min
                    calendar.set(Calendar.SECOND, 0);// for 0 sec
                    selectedContract.manuelDropoffTimeStamp = (calendar.getTimeInMillis() / 1000);
                    selectedContract.dropoffTimestamp = (calendar.getTimeInMillis() / 1000);
                    viewModel.setContractInformation(selectedContract);
                    binding.setContract(selectedContract);

                }, mHour, mMinute, true);
        timePickerDialog.show();
    }

    @Override
    public void backButtonClicked() {
        super.backButtonClicked();
        //mActivity.hideProgressBar();
    }

    private void observeContractInformation(String contractId) {
        if (viewModel.getContractInformationObservable().hasObservers()) {
            selectedContract = viewModel.getContractInformationObservable().getValue();
            bindViewModal();
            return;
        }
        super.showLoading();
        GetContractInformationRequest request = new GetContractInformationRequest();
        request.setContractId(contractId);
        request.setStatusCode(EnumUtils.ContractStatusCode.RENTAL.getIntValue());

        viewModel.setGetContractInformationRequest(request);
        viewModel.getContractInformationObservable().observe(this, contractItem -> {
            super.hideLoading();
            if (contractItem == null) {
                isServiceCalled = true;
                showMessageDialog(getString(R.string.unknown_error_message));
            } else if (contractItem.responseResult.result) {
                selectedContract = contractItem;
                bindViewModal();
            } else {
                isServiceCalled = true;
                showMessageDialog(contractItem.responseResult.exceptionDetail);
            }
        });
    }

    @Override
    public void messageDialogDoneButtonClicked() {
        if (isServiceCalled) {
            viewModel.getContractInformationObservable().removeObservers(this);
            mActivity.onBackPressed();
            //mActivity.getSupportFragmentManager().popBackStackImmediate();
        }
    }

    private void bindViewModal() {
        viewModel.setContractInformation(selectedContract);
        binding.setContract(selectedContract);

        downloadPhoto("license_front_image", true);
        downloadPhoto("license_rear_image", false);
//        if (selectedContract.customer.drivingLicenseFrontImageBitmap != null){
//            binding.licenseFrontFacePhoto.setImageBitmap(selectedContract.customer.drivingLicenseFrontImageBitmap);
//        }
//        else if (selectedContract.customer.drivingLicenseFrontImage != null){
//            Picasso.get().load(selectedContract.customer.drivingLicenseFrontImage).into(binding.licenseFrontFacePhoto);
//        }else{
//
//        }
//
//        if (selectedContract.customer.drivingLicenseRearImageBitmap != null){
//            binding.licenseBackFacePhoto.setImageBitmap(selectedContract.customer.drivingLicenseRearImageBitmap);
//        }
//        else if (selectedContract.customer.drivingLicenseRearImage != null){
//            Picasso.get().load(selectedContract.customer.drivingLicenseRearImage).into(binding.licenseBackFacePhoto);
//        }else{
//
//        }
    }

    private void downloadPhoto(String imageName, boolean isFront) {
        String refName = BlobStorageManager.instance.prepareCustomerDrivingLicenseImageName(selectedContract.customer.customerId, imageName);
        final ByteArrayOutputStream imageStream = new ByteArrayOutputStream();
        final Handler handler = new Handler();
        Thread th = new Thread(() -> {
            try {
                long imageLength = 0;
                BlobStorageManager.instance.GetImage(BlobStorageManager.instance.getCustomerContainerName(), refName, imageStream, imageLength);
                handler.post(() -> {
                    byte[] buffer = imageStream.toByteArray();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
                    if (isFront) {
                        binding.licenseFrontFacePhoto.setImageBitmap(bitmap);
                    } else {
                        binding.licenseBackFacePhoto.setImageBitmap(bitmap);
                    }
                    imageStream.reset();
                });
            } catch (Exception ex) {
                final String exceptionMessage = ex.getMessage();
                handler.post(imageStream::reset);
            }
        });
        th.start();
    }

    @Override
    public void navigate() {
        DamageEntryFragmentForRental damageEntryFragmentForRental = DamageEntryFragmentForRental.forSelectedContract(selectedContract);
        super.changeFragment(damageEntryFragmentForRental);
    }
}
