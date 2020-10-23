package com.creatifsoftware.filonova.view.fragment.base;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.fragment.app.Fragment;

import com.creatifsoftware.filonova.BuildConfig;
import com.creatifsoftware.filonova.R;
import com.creatifsoftware.filonova.model.ContractItem;
import com.creatifsoftware.filonova.model.CreditCard;
import com.creatifsoftware.filonova.model.TransferItem;
import com.creatifsoftware.filonova.model.base.ResponseResult;
import com.creatifsoftware.filonova.utils.ClickHelper;
import com.creatifsoftware.filonova.view.activity.MainActivity;
import com.shuhart.stepview.StepView;

import id.zelory.compressor.Compressor;

/**
 * Created by kerembalaban on 18.02.2019 at 02:43.
 */
public class BaseFragment extends Fragment {
    public static final int REQUEST_TAKE_PHOTO = 100;
    public static final String KEY_SELECTED_CONTRACT = "selected_contract";
    public static final String KEY_IS_NEW_REQUEST = "is_new_request";
    public static final String KEY_SELECTED_TRANSFER = "selected_transfer";
    public static final String KEY_SELECTED_EQUIPMENT = "selected_equipment";
    public static final String KILOMETER_FUEL_CAPTURE_IMAGE = "kilometer_fuel_image";
    public static final String DAMAGE_IMAGE = "damage_image";
    public static final String FRONT_LICENSE_IMAGE = "front_license_image";
    public static final String REAR_LICENSE_IMAGE = "rear_license_image";
    private static final int MY_CAMERA_PERMISSION_CODE = 102;
    private static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".fileprovider";
    public static ContractItem selectedContract;
    public static TransferItem selectedTransfer;
    public MainActivity mActivity;
    public int webServiceCallCount;
    public String imageFilePath;
    private Compressor compressor;
    private ProgressDialog progressDialog;
    private BaseFilonovaLoadingDialogFragment baseFilonovaLoadingDialogFragment;

    @BindingAdapter({"android:src"})
    public static void setImageViewResource(ImageView imageView, String base64String) {
        if (base64String != null) {
            //base64String = base64String.replace("data:image/jpeg;base64,","");
            String[] splittedArray = base64String.split(";base64");
            if (splittedArray.length == 2) {
                base64String = splittedArray[1];
            } else {
                base64String = base64String.replace("data:image/jpeg;base64,", "");
                base64String = base64String.replace("data:image/png;base64,", "");
                base64String = base64String.replace("data:image/bmp;base64,", "");
            }
            byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imageView.setImageBitmap(decodedByte);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Button nextButton = ((MainActivity) mActivity).findViewById(R.id.next_button);
        //To Prevent multiple clicks
        nextButton.setOnClickListener(new ClickHelper() {
            @Override
            public void onSingleClick(View v) {
                nextButtonClicked();
            }
        });

        Button backButton = ((MainActivity) mActivity).findViewById(R.id.back_button);
        backButton.setOnClickListener(view -> backButtonClicked());

        selectedContract = getSelectedContract();
        selectedTransfer = getSelectedTransfer();
        webServiceCallCount = 0;
        //initCompressor();
    }

    public StepView getStepView() {
        return ((MainActivity) mActivity).findViewById(R.id.step_view);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.mActivity = (MainActivity) context;
        }
    }

//    public void removeImageFile() {
//        if (imageFilePath != null) {
//            File file = getImageFile(imageFilePath);
//            imageFilePath = null;
//        }
//    }
//
//    private void initCompressor() {
//        compressor = new Compressor(mActivity).setQuality(75).setCompressFormat(Bitmap.CompressFormat.JPEG);
//        //compressor = new Compressor.Builder(this).setQuality(60).setCompressFormat(Bitmap.CompressFormat.JPEG).build();
//    }

    private void nextButtonClicked() {
        ResponseResult responseResult = checkBeforeNavigate();
        if (!responseResult.result) {
            showMessageDialog(responseResult.exceptionDetail);
        } else {
            navigate();
        }
    }

    public boolean checkJsonResponse(ResponseResult responseResult) {
        if (responseResult == null) {
            return false;
        } else if (!responseResult.result) {
            showMessageDialog(responseResult.exceptionDetail);
            return false;
        } else {
            return true;
        }
    }

    protected void navigate() {
    }

    public void backButtonClicked() {
        mActivity.onBackPressed();
    }

    public ContractItem getSelectedContract() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            ContractItem contractItem = ((ContractItem) bundle.getSerializable(KEY_SELECTED_CONTRACT));
            if (contractItem != null) {
                contractItem.groupCodeInformation.groupCodeImage = "";
                if (contractItem.updatedGroupCodeInformation != null) {
                    contractItem.updatedGroupCodeInformation.groupCodeImage = "";
                }
            }

            return contractItem;
        }

        return new ContractItem();
    }

    protected TransferItem getSelectedTransfer() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            TransferItem transferItem = ((TransferItem) bundle.getSerializable(KEY_SELECTED_TRANSFER));
            if (transferItem != null && transferItem.groupCodeInformation != null) {
                transferItem.groupCodeInformation.groupCodeImage = "";
            }

            return transferItem;
            //return ((TransferItem)bundle.getSerializable(KEY_SELECTED_TRANSFER));
        }
        return new TransferItem();
    }

    protected ResponseResult checkBeforeNavigate() {
        return new ResponseResult(true, "");
    }

    protected void showMessageDialog(String message) {
        mActivity.showMessageDialog(message);

    }

    protected void showLoading() {
       /* progressDialog = new ProgressDialog(mActivity);
        progressDialog.setMessage("Lütfen Bekleyin...");
        //progressDialog.setTitle("ProgressDialog");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);*/

        //mActivity.showLoading();

        baseFilonovaLoadingDialogFragment = new BaseFilonovaLoadingDialogFragment();
        baseFilonovaLoadingDialogFragment.setCancelable(false);
        baseFilonovaLoadingDialogFragment.show(mActivity.getSupportFragmentManager(), "BaseLoading");
    }

    protected void hideLoading() {
        // progressDialog.dismiss();

        //  mActivity.hideLoading();
        baseFilonovaLoadingDialogFragment.dismiss();
    }

    protected void changeFragment(Fragment fragment) {
        mActivity.show(fragment);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                permissionGranted();
            } else {
                Toast.makeText(getContext(), "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void hideKeyboard() {
        mActivity.hideSoftInputKeyboard();
    }

    public void permissionGranted() {

    }

    protected void openCameraIntent(int requestCode) {
        if (mActivity.checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    MY_CAMERA_PERMISSION_CODE);
        } else {
            //Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivityForResult(cameraIntent, requestCode);
        }
    }

    public void kilometerSelected(String kilometer) {

    }

    public void getInputValue(String input, Integer tag) {

    }

    public void messageDialogDoneButtonClicked() {

    }

    public void confirmationButtonClicked() {

    }

    public void addCreditCard(CreditCard creditCard) {

    }

    public void addDepositCreditCard(CreditCard creditCard) {

    }
}
