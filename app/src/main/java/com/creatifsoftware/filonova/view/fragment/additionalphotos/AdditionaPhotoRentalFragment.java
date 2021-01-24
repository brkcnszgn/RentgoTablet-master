package com.creatifsoftware.filonova.view.fragment.additionalphotos;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.creatifsoftware.filonova.R;
import com.creatifsoftware.filonova.databinding.FragmentAdditionalRentalPhotosBinding;
import com.creatifsoftware.filonova.di.Injectable;
import com.creatifsoftware.filonova.model.ContractItem;
import com.creatifsoftware.filonova.model.base.ResponseResult;
import com.creatifsoftware.filonova.model.request.GetContractInformationRequest;
import com.creatifsoftware.filonova.utils.BlobStorageManager;
import com.creatifsoftware.filonova.utils.EnumUtils;
import com.creatifsoftware.filonova.utils.ImageUtil;
import com.creatifsoftware.filonova.view.callback.AdditionalPhotoImageClickCallback;
import com.creatifsoftware.filonova.view.fragment.base.BaseFragment;
import com.creatifsoftware.filonova.view.fragment.equipmentInformation.EquipmentInformationFragmentForDelivery;
import com.creatifsoftware.filonova.view.fragment.equipmentInformation.EquipmentInformationFragmentForRental;
import com.creatifsoftware.filonova.viewmodel.AdditionalPhotoViewModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

import javax.inject.Inject;

import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;

/**
 * Created by kerembalaban on 16.02.2019 at 18:38.
 */
public class AdditionaPhotoRentalFragment extends BaseFragment implements Injectable {
    private static final int RIGHT_CAPTURE_IMAGE = 100;
    private static final int LEFT_CAPTURE_IMAGE = 101;
    private static final int FRONT_CAPTURE_IMAGE = 102;
    private static final int BACK_CAPTURE_IMAGE = 103;
    private static final int SEAT_BACK_CAPTURE_IMAGE = 104;
    private static final int SEAT_FRONT_CAPTURE_IMAGE = 105;
    private static final int MY_CAMERA_PERMISSION_CODE = 102;
    private final AdditionalPhotoImageClickCallback additionalPhotoImageClickCallback = this::openCameraIntent;
    public FragmentAdditionalRentalPhotosBinding binding;
    public AdditionalPhotoViewModel viewModel;
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private boolean hasServiceError = false;
    private boolean hasBlobStorageError = false;
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;

    public static AdditionaPhotoRentalFragment forSelectedContract(ContractItem selectedContract) {
        AdditionaPhotoRentalFragment fragment = new AdditionaPhotoRentalFragment();
        Bundle args = new Bundle();

        args.putSerializable(KEY_SELECTED_CONTRACT, selectedContract);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_additional_rental_photos, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getStepView().go(2, true);

        mActivity.showProgressBar();
        viewModel = ViewModelProviders.of(mActivity, viewModelFactory)
                .get(AdditionalPhotoViewModel.class);
        selectedContract = getSelectedContract();
        binding.contractInformationTitle.setText(String.format(Locale.getDefault(), "%s - %s (%s)", getString(R.string.additional_photos_title), selectedContract.contractNumber, selectedContract.pnrNumber));
        bindViewModal();

    }


    @Override
    public void backButtonClicked() {
        selectedContract = null;
        viewModel.getContractInformationObservable().removeObservers(getViewLifecycleOwner());
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
        request.setStatusCode(EnumUtils.ContractStatusCode.WAITING_FOR_DELIVERY.getIntValue());

        viewModel.setGetContractInformationRequest(request);
        viewModel.getContractInformationObservable().observe(this, contractItem -> {
            if (contractItem == null) {
                super.hideLoading();
                hasServiceError = true;
                showMessageDialog(getString(R.string.unknown_error_message));
            } else if (!contractItem.responseResult.result) {
                super.hideLoading();
                hasServiceError = true;
                showMessageDialog(contractItem.responseResult.exceptionDetail);
            } else if (contractItem.contractId.equals(selectedContract.contractId)) {
                super.hideLoading();
                selectedContract = contractItem;
                selectedContract.depositAmount = selectedContract.groupCodeInformation.depositAmount;
                bindViewModal();
            }
        });
    }

    @Override
    public void messageDialogDoneButtonClicked() {
        if (hasServiceError) {
            viewModel.getContractInformationObservable().removeObservers(this);
            mActivity.onBackPressed();
            //mActivity.getSupportFragmentManager().popBackStackImmediate();
        }
    }

    private void bindViewModal() {
        viewModel.setContractInformation(selectedContract);

        binding.setCallback(additionalPhotoImageClickCallback);


        if (selectedContract.isEquipmentChanged) {
            super.showLoading();
            downloadPhoto("extra_right_image", RIGHT_CAPTURE_IMAGE);
            downloadPhoto("extra_left_image", LEFT_CAPTURE_IMAGE);
            downloadPhoto("extra_front_image", FRONT_CAPTURE_IMAGE);
            downloadPhoto("extra_rear_image", BACK_CAPTURE_IMAGE);
            downloadPhoto("extra_seat_rear_image", SEAT_BACK_CAPTURE_IMAGE);
            downloadPhoto("extra_seat_front_image", SEAT_FRONT_CAPTURE_IMAGE);
        } else {
            if (selectedContract.customer.extra_right_image != null) {
                binding.rightFacePhoto.setImageBitmap(ImageUtil.instance.convertFiletoBitmap(selectedContract.customer.extra_right_image));
                binding.rightPhotoCheckbox.setChecked(true);
            }
            if (selectedContract.customer.extra_left_image != null) {
                binding.leftFacePhoto.setImageBitmap(ImageUtil.instance.convertFiletoBitmap(selectedContract.customer.extra_left_image));
                binding.leftPhotoCheckbox.setChecked(true);
            }
            if (selectedContract.customer.extra_front_image != null) {
                binding.frontFacePhoto.setImageBitmap(ImageUtil.instance.convertFiletoBitmap(selectedContract.customer.extra_front_image));
                binding.frontFacePhotoCheckbox.setChecked(true);
            }
            if (selectedContract.customer.extra_rear_image != null) {
                binding.backFacePhoto.setImageBitmap(ImageUtil.instance.convertFiletoBitmap(selectedContract.customer.extra_rear_image));
                binding.backFacePhotoCheckbox.setChecked(true);
            }
            if (selectedContract.customer.extra_seat_rear_image != null) {
                binding.seatBackFacePhoto.setImageBitmap(ImageUtil.instance.convertFiletoBitmap(selectedContract.customer.extra_seat_rear_image));
                binding.seatBackFacePhotoCheckbox.setChecked(true);
            }
            if (selectedContract.customer.extra_seat_front_image != null) {
                binding.seatFrontFacePhoto.setImageBitmap(ImageUtil.instance.convertFiletoBitmap(selectedContract.customer.extra_seat_front_image));
                binding.seatFrontFacePhotoCheckbox.setChecked(true);
            }
        }
    }

    private void downloadPhoto(String imageName, int type) {
        String refName =
                BlobStorageManager.instance.prepareAdditionalRentalImageName(selectedContract.selectedEquipment, selectedContract.contractNumber, imageName);
        final ByteArrayOutputStream imageStream = new ByteArrayOutputStream();
        final Handler handler = new Handler();
        Thread th = new Thread(() -> {
            try {
                long imageLength = 0;
                BlobStorageManager.instance.GetImage(BlobStorageManager.instance.getEquipmentsContainerName(), refName,
                        imageStream, imageLength);
                handler.post(() -> {
                    byte[] buffer = imageStream.toByteArray();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
                    switch (type) {
                        case RIGHT_CAPTURE_IMAGE:
                            binding.rightFacePhoto.setImageBitmap(bitmap);
                            binding.rightPhotoCheckbox.setChecked(true);
                            break;
                        case LEFT_CAPTURE_IMAGE:
                            binding.leftFacePhoto.setImageBitmap(bitmap);
                            binding.leftPhotoCheckbox.setChecked(true);
                            break;
                        case FRONT_CAPTURE_IMAGE:
                            binding.frontFacePhoto.setImageBitmap(bitmap);
                            binding.frontFacePhotoCheckbox.setChecked(true);
                            break;
                        case BACK_CAPTURE_IMAGE:
                            binding.backFacePhoto.setImageBitmap(bitmap);
                            binding.backFacePhotoCheckbox.setChecked(true);
                            break;
                        case SEAT_BACK_CAPTURE_IMAGE:
                            binding.seatBackFacePhoto.setImageBitmap(bitmap);
                            binding.seatBackFacePhotoCheckbox.setChecked(true);
                            break;
                        case SEAT_FRONT_CAPTURE_IMAGE:
                            binding.seatFrontFacePhoto.setImageBitmap(bitmap);
                            binding.seatFrontFacePhotoCheckbox.setChecked(true);
                            break;
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
    protected void navigate() {
        super.navigate();
        if (selectedContract.isEquipmentChanged) {
            showEquipmentListFragment();
        } else {
            uploadDrivingLicensePhotos();
        }

    }

    private void uploadDrivingLicensePhotos() {
        hasBlobStorageError = false;
        super.showLoading();
        (new Handler()).postDelayed(this::upload, 1000);
    }

    private void upload() {
        Thread thread = new Thread(() -> {
            try {

                String extra_right_image = BlobStorageManager.instance.prepareAdditionalRentalImageName(selectedContract.selectedEquipment, selectedContract.contractNumber,
                        "extra_right_image");
                String extra_left_image = BlobStorageManager.instance.prepareAdditionalRentalImageName(selectedContract.selectedEquipment, selectedContract.contractNumber,
                        "extra_left_image");
                String extra_front_image = BlobStorageManager.instance.prepareAdditionalRentalImageName(selectedContract.selectedEquipment, selectedContract.contractNumber,
                        "extra_front_image");
                String extra_rear_image = BlobStorageManager.instance.prepareAdditionalRentalImageName(selectedContract.selectedEquipment, selectedContract.contractNumber,
                        "extra_rear_image");
                String extra_seat_rear_image = BlobStorageManager.instance.prepareAdditionalRentalImageName(selectedContract.selectedEquipment, selectedContract.contractNumber,
                        "extra_seat_rear_image");
                String extra_seat_front_image = BlobStorageManager.instance.prepareAdditionalRentalImageName(selectedContract.selectedEquipment, selectedContract.contractNumber,
                        "extra_seat_front_image");

                BlobStorageManager.instance.UploadImage(BlobStorageManager.instance.getEquipmentsContainerName(),
                        selectedContract.customer.extra_right_image, extra_right_image);
                BlobStorageManager.instance.UploadImage(BlobStorageManager.instance.getEquipmentsContainerName(),
                        selectedContract.customer.extra_left_image, extra_left_image);
                BlobStorageManager.instance.UploadImage(BlobStorageManager.instance.getEquipmentsContainerName(),
                        selectedContract.customer.extra_front_image, extra_front_image);
                BlobStorageManager.instance.UploadImage(BlobStorageManager.instance.getEquipmentsContainerName(),
                        selectedContract.customer.extra_rear_image, extra_rear_image);
                BlobStorageManager.instance.UploadImage(BlobStorageManager.instance.getEquipmentsContainerName(),
                        selectedContract.customer.extra_seat_rear_image, extra_seat_rear_image);
                BlobStorageManager.instance.UploadImage(BlobStorageManager.instance.getEquipmentsContainerName(),
                        selectedContract.customer.extra_seat_front_image, extra_seat_front_image);


            } catch (Exception e) {
                hasBlobStorageError = true;
                showMessageDialog(String.format(Locale.getDefault(), "Fotoğraf atma hatası\n\n%s", e.getLocalizedMessage()));
                e.printStackTrace();
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        super.hideLoading();
        if (!hasBlobStorageError)
            showEquipmentListFragment();
    }

    private void showEquipmentListFragment() {
        EquipmentInformationFragmentForRental carInformationForRentalFragment = EquipmentInformationFragmentForRental.forSelectedContract(selectedContract);
        super.changeFragment(carInformationForRentalFragment);
    }

    @Override
    protected ResponseResult checkBeforeNavigate() {
        if (!binding.rightPhotoCheckbox.isChecked()) {
            return new ResponseResult(false, getString(R.string.right_error));
        } else if (!binding.leftPhotoCheckbox.isChecked()) {
            return new ResponseResult(false, getString(R.string.left_front_photo_error));
        } else if (!binding.frontFacePhotoCheckbox.isChecked()) {
            return new ResponseResult(false, getString(R.string.front_back_photo_error));
        } else if (!binding.backFacePhotoCheckbox.isChecked()) {
            return new ResponseResult(false, getString(R.string.back_photo_error));
        } else if (!binding.seatFrontFacePhotoCheckbox.isChecked()) {
            return new ResponseResult(false, getString(R.string.seat_front_photo_error));
        } else if (!binding.seatBackFacePhotoCheckbox.isChecked()) {
            return new ResponseResult(false, getString(R.string.seat_back_photo_error));
        }

        return super.checkBeforeNavigate();
    }

    @Override
    public void permissionGranted() {
        super.permissionGranted();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCameraIntent(getView());
            } else {
                Toast.makeText(getContext(), "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void openCameraIntent(View view) {
        if (mActivity.checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    MY_CAMERA_PERMISSION_CODE);
        } else if (mActivity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    876);
        } else if (mActivity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    7675);
        } else {
            //Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            //Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");

            Intent intent = ImageUtil.instance.dispatchTakePictureIntent(mActivity);
            int reqId = 0;
            switch (view.getId()) {

                case R.id.rigt_image_layout:
                    reqId = RIGHT_CAPTURE_IMAGE;
                    break;
                case R.id.left_image_layout:
                    reqId = LEFT_CAPTURE_IMAGE;
                    break;
                case R.id.front_image_layout:
                    reqId = FRONT_CAPTURE_IMAGE;
                    break;
                case R.id.back_image_layout:
                    reqId = BACK_CAPTURE_IMAGE;
                    break;
                case R.id.seat_back_image_layout:
                    reqId = SEAT_BACK_CAPTURE_IMAGE;
                    break;
                case R.id.seat_front_image_layout:
                    reqId = SEAT_FRONT_CAPTURE_IMAGE;
                    break;

            }
            startActivityForResult(intent, reqId);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            File compressedImage = ImageUtil.instance.getImageFile();
            try {
                compressedImage = new Compressor(mActivity.getApplicationContext()).setQuality(50).compressToFile(ImageUtil.instance.getImageFile());
            } catch (IOException e) {
                e.printStackTrace();
            }

            switch (requestCode) {
                case RIGHT_CAPTURE_IMAGE:
                    selectedContract.customer.extra_right_image = compressedImage;
                    binding.rightFacePhoto.setImageBitmap(ImageUtil.instance.convertFiletoBitmap(selectedContract.customer.extra_right_image));
                    binding.rightPhotoCheckbox.setChecked(true);
                    ImageUtil.instance.removeImageFile();
                    break;
                case LEFT_CAPTURE_IMAGE:
                    selectedContract.customer.extra_left_image = compressedImage;
                    binding.leftFacePhoto.setImageBitmap(ImageUtil.instance.convertFiletoBitmap(selectedContract.customer.extra_left_image));
                    binding.leftPhotoCheckbox.setChecked(true);
                    ImageUtil.instance.removeImageFile();
                    break;
                case FRONT_CAPTURE_IMAGE:
                    selectedContract.customer.extra_front_image = compressedImage;
                    binding.frontFacePhoto.setImageBitmap(ImageUtil.instance.convertFiletoBitmap(selectedContract.customer.extra_front_image));
                    binding.frontFacePhotoCheckbox.setChecked(true);
                    ImageUtil.instance.removeImageFile();
                    break;
                case BACK_CAPTURE_IMAGE:
                    selectedContract.customer.extra_rear_image = compressedImage;
                    binding.backFacePhoto.setImageBitmap(ImageUtil.instance.convertFiletoBitmap(selectedContract.customer.extra_rear_image));
                    binding.backFacePhotoCheckbox.setChecked(true);
                    ImageUtil.instance.removeImageFile();
                    break;
                case SEAT_BACK_CAPTURE_IMAGE:
                    selectedContract.customer.extra_seat_rear_image = compressedImage;
                    binding.seatBackFacePhoto.setImageBitmap(ImageUtil.instance.convertFiletoBitmap(selectedContract.customer.extra_seat_rear_image));
                    binding.seatBackFacePhotoCheckbox.setChecked(true);
                    ImageUtil.instance.removeImageFile();
                    break;
                case SEAT_FRONT_CAPTURE_IMAGE:
                    selectedContract.customer.extra_seat_front_image = compressedImage;
                    binding.seatFrontFacePhoto.setImageBitmap(ImageUtil.instance.convertFiletoBitmap(selectedContract.customer.extra_seat_front_image));
                    binding.seatFrontFacePhotoCheckbox.setChecked(true);
                    ImageUtil.instance.removeImageFile();
                    break;

            }

        }
    }
}
