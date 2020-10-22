package com.creatifsoftware.filonova.view.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.creatifsoftware.filonova.BuildConfig;
import com.creatifsoftware.filonova.R;
import com.creatifsoftware.filonova.databinding.ActivityMainBinding;
import com.creatifsoftware.filonova.di.Injectable;
import com.creatifsoftware.filonova.interfaces.CommunicationInterface;
import com.creatifsoftware.filonova.model.AdditionalProduct;
import com.creatifsoftware.filonova.model.AvailabilityGroupCodeInformation;
import com.creatifsoftware.filonova.model.ContractItem;
import com.creatifsoftware.filonova.model.CreditCard;
import com.creatifsoftware.filonova.model.DamageItem;
import com.creatifsoftware.filonova.model.Equipment;
import com.creatifsoftware.filonova.model.EquipmentPart;
import com.creatifsoftware.filonova.model.GroupCodeInformation;
import com.creatifsoftware.filonova.utils.ApplicationUtils;
import com.creatifsoftware.filonova.utils.SharedPrefUtils;
import com.creatifsoftware.filonova.view.fragment.EquipmentListFragment;
import com.creatifsoftware.filonova.view.fragment.FinePriceFragment;
import com.creatifsoftware.filonova.view.fragment.base.BaseConfirmationDialog;
import com.creatifsoftware.filonova.view.fragment.base.BaseCreditCardFragment;
import com.creatifsoftware.filonova.view.fragment.base.BaseDepositCreditCardFragment;
import com.creatifsoftware.filonova.view.fragment.base.BaseEquipmentPartSelectionDialog;
import com.creatifsoftware.filonova.view.fragment.base.BaseErrorDialog;
import com.creatifsoftware.filonova.view.fragment.base.BaseExtraPaymentDialog;
import com.creatifsoftware.filonova.view.fragment.base.BaseFragment;
import com.creatifsoftware.filonova.view.fragment.base.BaseGroupCodeChangeFragment;
import com.creatifsoftware.filonova.view.fragment.base.BaseInputDialog;
import com.creatifsoftware.filonova.view.fragment.base.BaseKilometerDialog;
import com.creatifsoftware.filonova.view.fragment.base.BaseLoadingDialog;
import com.creatifsoftware.filonova.view.fragment.base.BaseLogoutDialog;
import com.creatifsoftware.filonova.view.fragment.base.BasePlateNumberDialog;
import com.creatifsoftware.filonova.view.fragment.base.BasePreviewImageFragment;
import com.creatifsoftware.filonova.view.fragment.damageEntry.DamageEntryFragment;
import com.creatifsoftware.filonova.view.fragment.dashboards.ContractDashboardFragment;
import com.creatifsoftware.filonova.view.fragment.dashboards.EquipmentDashboardFragment;
import com.creatifsoftware.filonova.view.fragment.dashboards.TransferDashboardFragment;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class MainActivity extends AppCompatActivity implements HasSupportFragmentInjector, Injectable, CommunicationInterface {

    @Inject
    DispatchingAndroidInjector<androidx.fragment.app.Fragment> dispatchingAndroidInjector;

    private ActivityMainBinding binding;
    private int menuPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setNavbarButtonClickListener();
        //initFirebaseDB();
        showContractMenu();
    }

    private void initFirebaseDB() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference docRef = db.collection("mobile_versions").document("tablet_service");
        docRef.addSnapshotListener((documentSnapshot, e) -> {
            if (e != null) {
                return;
            }

            if (documentSnapshot != null && documentSnapshot.exists()) {
                String currentVersion = ApplicationUtils.instance.getApplicationVersion(this);
                if (!BuildConfig.IS_PROD) {
                    currentVersion = currentVersion.replace("Test - ", "");
                }

                String dbVersion = "";
                String versionDetail = "";
                ArrayList<String> versionDetailList = new ArrayList<>();

                Map<String, Object> snapshotData = documentSnapshot.getData();
                if (snapshotData != null) {
                    dbVersion = (String) snapshotData.get("version_number");
                    versionDetailList = (ArrayList) snapshotData.get("version_detail");
                }

                for (String item : versionDetailList) {
                    if (versionDetail.isEmpty()) {
                        versionDetail = String.format(Locale.getDefault(), "- %s", item);
                    } else {
                        versionDetail = String.format(Locale.getDefault(), "%s\n- %s", versionDetail, item);
                    }
                }

                if (Double.valueOf(dbVersion) > Double.valueOf(currentVersion)) {
                    SharedPrefUtils.instance.saveObject(this, "current_version_number", dbVersion);
                    SharedPrefUtils.instance.saveObject(this, "version_detail", versionDetail);
                }
            }
        });
    }

    @Override
    public void onKilometerSelected(String kilometer) {
        BaseFragment fragment = getCurrentFragment();
        fragment.kilometerSelected(kilometer);
    }

    @Override
    public void getInputValue(String input, Integer tag) {
        BaseFragment fragment = getCurrentFragment();
        fragment.getInputValue(input, tag);
    }

    @Override
    public void addCreditCard(CreditCard creditCard) {
        BaseFragment fragment = getCurrentFragment();
        fragment.addCreditCard(creditCard);
    }

    @Override
    public void addDepositCard(CreditCard creditCard) {
        BaseFragment fragment = getCurrentFragment();
        fragment.addDepositCreditCard(creditCard);
    }

    @Override
    public void addDamageItem(DamageItem damageItem) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment.isVisible()) {
                ((DamageEntryFragment) fragment).addDamageItemToDamageList(damageItem);
                break;
            }
        }
    }

    @Override
    public void addCustomExtraPayment(AdditionalProduct item) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment.isVisible()) {
                ((FinePriceFragment) fragment).extraPaymentAdded(item);
                break;
            }
        }
    }

    @Override
    public void messageDialogDoneButtonClicked() {
        BaseFragment fragment = getCurrentFragment();
        fragment.messageDialogDoneButtonClicked();
    }

    @Override
    public void searchContractByPlateNumber(String plateNumber) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment.isVisible()) {
                ((ContractDashboardFragment) fragment).searchContractByPlate(plateNumber);
                break;
            }
        }
    }

    @Override
    public void groupCodeChanged(AvailabilityGroupCodeInformation groupCodeInformation) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment.isVisible()) {
                ((EquipmentListFragment) fragment).groupCodeChanged(groupCodeInformation);
                break;
            }


        }
    }

    @Override
    public void confirmButtonClicked() {
        BaseFragment fragment = getCurrentFragment();
        fragment.confirmationButtonClicked();
    }

    @Override
    public void confirmButtonClickedForActivity() {
        if (menuPosition == 0) {
            contractButtonClicked();
        }
        if (menuPosition == 1) {
            equipmentDashboardButtonClicked();
        }
        if (menuPosition == 2) {
            transferButtonClicked();
        }
    }

    private BaseFragment getCurrentFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment.isVisible() && fragment instanceof BaseFragment) {
                return (BaseFragment) fragment;
            }
        }

        return new BaseFragment();
    }

    public void show(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_layout, fragment, null)
                .addToBackStack(null)
                .commit();
    }

    public void prepareStepView(ArrayList<String> steps) {
        binding.stepView.setSteps(steps);
    }

    private void setNavbarButtonClickListener() {
        binding.contractButton.setOnClickListener(view -> {
            menuPosition = 0;
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                contractButtonClicked();
            } else {
                showConfirmationDialogForMainActivity(getString(R.string.dialog_button_cancel), getString(R.string.dialog_button_yes), "Bu sırada yapılacak menü değişikliği girilen verilerin kaybolmasına yol açacaktır, emin misiniz?");
            }
        });

        binding.whereIsMyCarButton.setOnClickListener(view -> {
            menuPosition = 1;
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                equipmentDashboardButtonClicked();
            } else {
                showConfirmationDialogForMainActivity(getString(R.string.dialog_button_cancel), getString(R.string.dialog_button_yes), "Bu sırada yapılacak menü değişikliği girilen verilerin kaybolmasına yol açacaktır, emin misiniz?");
            }
        });

        binding.transferButton.setOnClickListener(view -> {
            menuPosition = 2;
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                transferButtonClicked();
            } else {
                showConfirmationDialogForMainActivity(getString(R.string.dialog_button_cancel), getString(R.string.dialog_button_yes), "Bu sırada yapılacak menü değişikliği girilen verilerin kaybolmasına yol açacaktır, emin misiniz?");
            }
        });

//        binding.reportButton.setOnClickListener(view -> {
//            showReportMenu();
//            binding.contractButton.setBackgroundTintList(ContextCompat.getColorStateList(MainActivity.this, R.color.colorMainLightGray));
//            binding.transferButton.setBackgroundTintList(ContextCompat.getColorStateList(MainActivity.this, R.color.colorMainLightGray));
//            binding.reportButton.setBackgroundTintList(ContextCompat.getColorStateList(MainActivity.this, R.color.colorMainBlue));
//            binding.whereIsMyCarButton.setBackgroundTintList(ContextCompat.getColorStateList(MainActivity.this, R.color.colorMainLightGray));
//        });

        binding.logoutButton.setOnClickListener(view -> logoutApp());
    }

    private void contractButtonClicked() {
        showContractMenu();
        binding.contractButton.setBackgroundTintList(ContextCompat.getColorStateList(MainActivity.this, R.color.colorMainBlue));
        binding.transferButton.setBackgroundTintList(ContextCompat.getColorStateList(MainActivity.this, R.color.colorMainLightGray));
        //binding.reportButton.setBackgroundTintList(ContextCompat.getColorStateList(MainActivity.this, R.color.colorMainLightGray));
        binding.whereIsMyCarButton.setBackgroundTintList(ContextCompat.getColorStateList(MainActivity.this, R.color.colorMainLightGray));
    }

    private void transferButtonClicked() {
        showTransferMenu();
        binding.contractButton.setBackgroundTintList(ContextCompat.getColorStateList(MainActivity.this, R.color.colorMainLightGray));
        binding.transferButton.setBackgroundTintList(ContextCompat.getColorStateList(MainActivity.this, R.color.colorMainBlue));
        //binding.reportButton.setBackgroundTintList(ContextCompat.getColorStateList(MainActivity.this, R.color.colorMainLightGray));
        binding.whereIsMyCarButton.setBackgroundTintList(ContextCompat.getColorStateList(MainActivity.this, R.color.colorMainLightGray));
    }

    private void equipmentDashboardButtonClicked() {
        showWhereIsMycarMenu();
        binding.contractButton.setBackgroundTintList(ContextCompat.getColorStateList(MainActivity.this, R.color.colorMainLightGray));
        binding.transferButton.setBackgroundTintList(ContextCompat.getColorStateList(MainActivity.this, R.color.colorMainLightGray));
        //binding.reportButton.setBackgroundTintList(ContextCompat.getColorStateList(MainActivity.this, R.color.colorMainLightGray));
        binding.whereIsMyCarButton.setBackgroundTintList(ContextCompat.getColorStateList(MainActivity.this, R.color.colorMainBlue));
    }

    private void showContractMenu() {
        popAllStackedFragments();
        ContractDashboardFragment fragment = new ContractDashboardFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_layout, fragment, "").commit();
    }

    private void showWhereIsMycarMenu() {
        popAllStackedFragments();
        EquipmentDashboardFragment fragment = new EquipmentDashboardFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_layout, fragment, "").commit();
    }

    private void showTransferMenu() {
        popAllStackedFragments();
        TransferDashboardFragment fragment = new TransferDashboardFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_layout, fragment, "").commit();
    }

    private void logoutApp() {
        BaseLogoutDialog fragment = new BaseLogoutDialog();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.complete_layout, fragment, "")
                .addToBackStack(null)
                .commit();
    }

    private void showReportMenu() {
        popAllStackedFragments();
    }

    private void popAllStackedFragments() {
        hideProgressBar();
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public void hideProgressBar() {
        RelativeLayout progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
    }

    public void showProgressBar() {
        RelativeLayout progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideErrorDialog() {
        //getSupportFragmentManager().popBackStackImmediate();
        FragmentManager fragmentManager = getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            if (fragment instanceof BaseErrorDialog) {
                onBackPressed();
                break;
            }
        }
    }

    public void hideLoading() {
        //getSupportFragmentManager().popBackStackImmediate();
        FragmentManager fragmentManager = getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            if (fragment instanceof BaseLoadingDialog) {
                onBackPressed();
                break;
            }
        }
    }

    public void showLoading() {
        new Thread(() -> {
            BaseLoadingDialog loadingDialog = new BaseLoadingDialog();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.complete_layout, loadingDialog, null)
                    .addToBackStack(null)
                    .commit();
        }).start();
    }

    public void showMessageDialog(String message) {
        BaseErrorDialog baseErrorDialog = BaseErrorDialog.withMessageContent(message);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.complete_layout, baseErrorDialog, null)
                .addToBackStack(null)
                .commit();
    }

    public void showImagePreview(Bitmap bitmap) {
        BasePreviewImageFragment basePreviewImageFragment = BasePreviewImageFragment.withImageBitmap(bitmap);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.complete_layout, basePreviewImageFragment, null)
                .addToBackStack(null)
                .commit();
    }

    public void showImagePreview(DamageItem item) {
        BasePreviewImageFragment basePreviewImageFragment = BasePreviewImageFragment.withDamageItem(item);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.complete_layout, basePreviewImageFragment, null)
                .addToBackStack(null)
                .commit();
    }

    public void showEquipmentPartSelectionFragment(ArrayList<EquipmentPart> partList, boolean shouldHideDamageDocumentSelection) {
        new Thread(() -> {
            BaseEquipmentPartSelectionDialog baseEquipmentPartSelectionDialog = BaseEquipmentPartSelectionDialog.with(partList, shouldHideDamageDocumentSelection);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.complete_layout, baseEquipmentPartSelectionDialog, null)
                    .addToBackStack(null)
                    .commit();
        }).start();
    }

    public void showConfirmationDialog(String cancelButtonTitle, String okButtonTitle, String message) {
        BaseConfirmationDialog baseConfirmationDialog = BaseConfirmationDialog.with(cancelButtonTitle, okButtonTitle, message);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.complete_layout, baseConfirmationDialog, null)
                .addToBackStack(null)
                .commit();
    }

    public void showConfirmationDialogForMainActivity(String cancelButtonTitle, String okButtonTitle, String message) {
        BaseConfirmationDialog baseConfirmationDialog = BaseConfirmationDialog.forMainActivity(cancelButtonTitle, okButtonTitle, message);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.complete_layout, baseConfirmationDialog, null)
                .addToBackStack(null)
                .commit();
    }

    public void showKilometerDialog(Equipment selectedEquipment) {
        BaseKilometerDialog baseKilometerDialog = BaseKilometerDialog.withSelectedEquipment(selectedEquipment);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.complete_layout, baseKilometerDialog, null)
                .addToBackStack(null)
                .commit();
    }

    public void showInputDialog(String hint, Integer tag, String errorMessage) {
        BaseInputDialog baseInputDialog = BaseInputDialog.withEditTextHintAndTag(hint, tag, errorMessage);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.complete_layout, baseInputDialog, null)
                .addToBackStack(null)
                .commit();
    }

    public void showCreditCardFragment() {
        BaseCreditCardFragment baseCreditCardFragment = new BaseCreditCardFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.complete_layout, baseCreditCardFragment, null)
                .addToBackStack(null)
                .commit();
    }

    public void showDepositCreditCardFragment(ContractItem contractItem, double depositAmount) {
        BaseDepositCreditCardFragment baseDepositCreditCardFragment = new BaseDepositCreditCardFragment();
        baseDepositCreditCardFragment.depositAmount = depositAmount;
        baseDepositCreditCardFragment.contractItem = contractItem;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.complete_layout, baseDepositCreditCardFragment, null)
                .addToBackStack(null)
                .commit();
    }

    public void showPlateNumberDialog() {
        BasePlateNumberDialog basePlateNumberDialog = new BasePlateNumberDialog();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.complete_layout, basePlateNumberDialog, null)
                .addToBackStack(null)
                .commit();
    }

    public void showCarGroupChangeFragment(GroupCodeInformation groupCodeInformation,
                                           AvailabilityGroupCodeInformation updatedGroupCodeInformation,
                                           ContractItem selectedContract) {
        BaseGroupCodeChangeFragment basePlateNumberDialog = BaseGroupCodeChangeFragment.withSelectedContract(groupCodeInformation, updatedGroupCodeInformation, selectedContract);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.complete_layout, basePlateNumberDialog, null)
                .addToBackStack(null)
                .commit();
    }

    public void showExtraPaymentDialog(AdditionalProduct product) {
        BaseExtraPaymentDialog baseExtraPaymentDialog = BaseExtraPaymentDialog.with(product);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.complete_layout, baseExtraPaymentDialog, null)
                .addToBackStack(null)
                .commit();
    }

    public void hideSoftInputKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }
}
