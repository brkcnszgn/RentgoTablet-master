package com.creatifsoftware.filonova.view.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.creatifsoftware.filonova.BuildConfig;
import com.creatifsoftware.filonova.R;
import com.creatifsoftware.filonova.databinding.ActivityLoginBinding;
import com.creatifsoftware.filonova.model.User;
import com.creatifsoftware.filonova.model.request.UserRequest;
import com.creatifsoftware.filonova.utils.ApplicationUtils;
import com.creatifsoftware.filonova.utils.SharedPrefUtils;
import com.creatifsoftware.filonova.view.fragment.base.BaseErrorDialog;
import com.creatifsoftware.filonova.viewmodel.LoginViewModel;

import java.util.Locale;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by kerembalaban on 21.02.2019 at 00:46.
 */
public class LoginActivity extends AppCompatActivity implements HasSupportFragmentInjector {
    private static final String KEY_MASTER_DATA_RESPONSE = "master_data_response";
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private ProgressDialog progressDialog;
    private ActivityLoginBinding binding;
    private LoginViewModel viewModel;

    public static void triggerRebirth(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        if (context instanceof Activity) {
            ((Activity) context).finish();
        }

        Runtime.getRuntime().exit(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        viewModel = ViewModelProviders.of(this,
                viewModelFactory).get(LoginViewModel.class);

        //observeMasterData();
        prepareComponents();
    }

    private void prepareComponents() {
        Animation translate = AnimationUtils.loadAnimation(this, R.anim.translate);
        Animation translate_left = AnimationUtils.loadAnimation(this, R.anim.translate_left);
        Animation translate_right = AnimationUtils.loadAnimation(this, R.anim.translate_right);
        binding.versionNumber.setText(String.format(Locale.getDefault(), "Version: %s", ApplicationUtils.instance.getApplicationVersion(getApplicationContext())));
        binding.inputLayout.startAnimation(translate);
        binding.imgLeft.startAnimation(translate_left);
        binding.imgRight.startAnimation(translate_right);

        binding.loginButton.setOnClickListener(view -> {
            hideKeyboard(view);
            if (binding.username.getText().toString().isEmpty() || binding.password.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), getString(R.string.username_password_empty_error), Toast.LENGTH_SHORT).show();
            } else {
                observeLogin();
            }
        });

        //get user isLoggedI
        binding.username.setText(SharedPrefUtils.instance.getSavedObjectFromPreference(getApplicationContext(), "username", String.class));
        binding.password.setText(SharedPrefUtils.instance.getSavedObjectFromPreference(getApplicationContext(), "password", String.class));

        if (!binding.username.getText().toString().isEmpty() && !binding.password.getText().toString().isEmpty()) {
            observeLogin();
        }
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void observeMasterData(User userResponse) {
//        MasterDataResponse masterDataResponse = SharedPrefUtils.instance.getSavedObjectFromPreference(getApplicationContext(),KEY_MASTER_DATA_RESPONSE, MasterDataResponse.class);
//        if (masterDataResponse != null){
//            return;
//        }

        viewModel.getMasterDataObservable().observe(this, response -> {
            hideLoading();
            if (response == null) {
                showMessageDialog(getString(R.string.unknown_error_message));
            } else if (response.responseResult.result) {
                SharedPrefUtils.instance.saveObject(getApplicationContext(), KEY_MASTER_DATA_RESPONSE, response);
                showMainActivity(userResponse);
            } else {
                showMessageDialog(response.responseResult.exceptionDetail);
            }
        });
    }

    private void observeLogin() {
        showLoading();

//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        final DocumentReference docRef = db.collection("mobile_versions").document("tablet_service");
//        docRef.get().addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                DocumentSnapshot document = task.getResult();
//                if (document.exists()) {
//                    String dbVersion = "";
//                    String currentVersion = ApplicationUtils.instance.getApplicationVersion(this);
//                    Map<String,Object> snapshotData = document.getData();
//                    if (snapshotData != null){
//                        dbVersion = (String) snapshotData.get("version_number");
//                        if (Double.valueOf(dbVersion) > Double.valueOf(currentVersion)){
//                            hideLoading();
//                            showMessageDialog("Yeni versiyon mevcuttur, sistem yöneticiniz ile iletişime geçin");
//                        }else{
//
//                        }
//                    }
//                }
//            }
//        });

        UserRequest request = new UserRequest();
        request.setUserName(binding.username.getText().toString());
        request.setPassword(binding.password.getText().toString());
        request.setVersion(BuildConfig.VERSION_NAME);

        viewModel.getLoginObervable(request).observe(this, userResponse -> {

            //binding.loginButton.setProgress(0);
            if (userResponse == null) {
                hideLoading();
                showMessageDialog(getString(R.string.unknown_error_message));
            } else if (userResponse.responseResult.result) {
                observeMasterData(userResponse);

            } else {
                hideLoading();
                showMessageDialog(userResponse.responseResult.exceptionDetail);
            }
        });

    }

    public void showMessageDialog(String message) {
        BaseErrorDialog baseErrorDialog = BaseErrorDialog.withMessageContent(message);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_layout, baseErrorDialog, null)
                .addToBackStack(null)
                .commit();
    }

    void showLoading() {
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Lütfen Bekleyin...");
        //progressDialog.setTitle("ProgressDialog");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);

//        BaseLoadingDialog loadingDialog = new BaseLoadingDialog();
//        getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.content_layout,loadingDialog, null)
//                .addToBackStack(null)
//                .commit();
    }

    void hideLoading() {
        progressDialog.dismiss();
        //onBackPressed();
    }

    private void showMainActivity(User user) {
        SharedPrefUtils.instance.saveObject(getApplicationContext(), "username", binding.username.getText().toString());
        SharedPrefUtils.instance.saveObject(getApplicationContext(), "password", binding.password.getText().toString());
        SharedPrefUtils.instance.saveObject(getApplicationContext(), "user", user);
        Intent myIntent = new Intent(this, MainActivity.class);
        this.startActivity(myIntent);
        finish();
    }

    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }
}
