package com.creatifsoftware.rentgoservice.di;

import com.creatifsoftware.rentgoservice.view.fragment.EquipmentListFragment;
import com.creatifsoftware.rentgoservice.view.fragment.FinePriceFragment;
import com.creatifsoftware.rentgoservice.view.fragment.ReservationInformationFragment;
import com.creatifsoftware.rentgoservice.view.fragment.additionalProducts.AdditionalProductsFragment;
import com.creatifsoftware.rentgoservice.view.fragment.additionalProducts.AdditionalServicesFragment;
import com.creatifsoftware.rentgoservice.view.fragment.base.BaseConfirmationDialog;
import com.creatifsoftware.rentgoservice.view.fragment.base.BaseCreditCardFragment;
import com.creatifsoftware.rentgoservice.view.fragment.base.BaseDepositCreditCardFragment;
import com.creatifsoftware.rentgoservice.view.fragment.base.BaseEquipmentPartSelectionDialog;
import com.creatifsoftware.rentgoservice.view.fragment.base.BaseErrorDialog;
import com.creatifsoftware.rentgoservice.view.fragment.base.BaseExtraPaymentDialog;
import com.creatifsoftware.rentgoservice.view.fragment.base.BaseGroupCodeChangeFragment;
import com.creatifsoftware.rentgoservice.view.fragment.base.BaseInputDialog;
import com.creatifsoftware.rentgoservice.view.fragment.base.BaseKilometerDialog;
import com.creatifsoftware.rentgoservice.view.fragment.base.BaseLoadingDialog;
import com.creatifsoftware.rentgoservice.view.fragment.base.BaseLogoutDialog;
import com.creatifsoftware.rentgoservice.view.fragment.base.BasePlateNumberDialog;
import com.creatifsoftware.rentgoservice.view.fragment.base.BasePreviewImageFragment;
import com.creatifsoftware.rentgoservice.view.fragment.contractInformation.ContractInformationForDeliveryFragment;
import com.creatifsoftware.rentgoservice.view.fragment.contractInformation.ContractInformationForRentalFragment;
import com.creatifsoftware.rentgoservice.view.fragment.contractSummary.ContractSummaryForDeliveryFragment;
import com.creatifsoftware.rentgoservice.view.fragment.contractSummary.ContractSummaryForRentalFragment;
import com.creatifsoftware.rentgoservice.view.fragment.damageEntry.DamageEntryFragment;
import com.creatifsoftware.rentgoservice.view.fragment.damageEntry.DamageEntryFragmentForDelivery;
import com.creatifsoftware.rentgoservice.view.fragment.damageEntry.DamageEntryFragmentForRental;
import com.creatifsoftware.rentgoservice.view.fragment.damageEntry.DamageEntryFragmentForTransferDelivery;
import com.creatifsoftware.rentgoservice.view.fragment.damageEntry.DamageEntryFragmentForTransferReturn;
import com.creatifsoftware.rentgoservice.view.fragment.dashboards.ContractDashboardFragment;
import com.creatifsoftware.rentgoservice.view.fragment.dashboards.EquipmentDashboardFragment;
import com.creatifsoftware.rentgoservice.view.fragment.dashboards.TransferDashboardFragment;
import com.creatifsoftware.rentgoservice.view.fragment.equipmentInformation.EquipmentInformationFragment;
import com.creatifsoftware.rentgoservice.view.fragment.equipmentInformation.EquipmentInformationFragmentForDelivery;
import com.creatifsoftware.rentgoservice.view.fragment.equipmentInformation.EquipmentInformationFragmentForEquipmentDashboard;
import com.creatifsoftware.rentgoservice.view.fragment.equipmentInformation.EquipmentInformationFragmentForRental;
import com.creatifsoftware.rentgoservice.view.fragment.equipmentInformation.EquipmentInformationFragmentForTransfer;
import com.creatifsoftware.rentgoservice.view.fragment.transfers.CreateTransferFragment;
import com.creatifsoftware.rentgoservice.view.fragment.transfers.EquipmentListForTransferFragment;
import com.creatifsoftware.rentgoservice.view.fragment.transfers.RepairedDamageListFragment;
import com.creatifsoftware.rentgoservice.view.fragment.transfers.TransferInformationFragment;
import com.creatifsoftware.rentgoservice.view.fragment.transfers.TransferSummaryForReturnFragment;
import com.creatifsoftware.rentgoservice.view.fragment.transfers.TransferSummaryFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract BaseErrorDialog contributeBaseErrorDialog();

    @ContributesAndroidInjector
    abstract BaseGroupCodeChangeFragment contributeBaseGroupCodeChangeFragment();

    @ContributesAndroidInjector
    abstract DamageEntryFragmentForRental contributeDamageEntryFragmentForRental();

    @ContributesAndroidInjector
    abstract BaseExtraPaymentDialog contributeBaseExtraPaymentDialog();

    @ContributesAndroidInjector
    abstract BaseLoadingDialog contributeBaseLoadingDialog();

    @ContributesAndroidInjector
    abstract EquipmentListForTransferFragment contributeEquipmentListForTransferFragment();

    @ContributesAndroidInjector
    abstract CreateTransferFragment contributeCreateTransferFragment();

    @ContributesAndroidInjector
    abstract BaseEquipmentPartSelectionDialog contributeBaseEquipmentPartSelectionDialog();

    @ContributesAndroidInjector
    abstract BaseKilometerDialog contributeBaseKilometerDialog();

    @ContributesAndroidInjector
    abstract BasePlateNumberDialog contributeBasePlateNumberDialog();

    @ContributesAndroidInjector
    abstract BaseInputDialog contributeBaseInputDialog();

    @ContributesAndroidInjector
    abstract BasePreviewImageFragment contributeBasePreviewImageFragment();

    @ContributesAndroidInjector
    abstract TransferDashboardFragment transferDashboardFragment();

    @ContributesAndroidInjector
    abstract BaseLogoutDialog baseLogoutDialog();

    @ContributesAndroidInjector
    abstract EquipmentDashboardFragment contributeCarsDashboardFragment();

    @ContributesAndroidInjector
    abstract ContractDashboardFragment contributeContractDashboardFragment();

    @ContributesAndroidInjector
    abstract ContractInformationForDeliveryFragment contributeContractInformationFragment();

    @ContributesAndroidInjector
    abstract EquipmentListFragment contributeEquipmentListFragment();

    @ContributesAndroidInjector
    abstract DamageEntryFragment contributeDamageEntryFragment();

    @ContributesAndroidInjector
    abstract EquipmentInformationFragment contributeDeliveryCarInformationFragment();

    @ContributesAndroidInjector
    abstract AdditionalProductsFragment contributeAdditionalProductsFragment();

    @ContributesAndroidInjector
    abstract AdditionalServicesFragment contributeAdditionalServicesFragment();

    @ContributesAndroidInjector
    abstract ContractInformationForRentalFragment contributeRentalContractInformationFragment();

    @ContributesAndroidInjector
    abstract EquipmentInformationFragmentForDelivery contributeEquipmentInformationFragmentForDelivery();

    @ContributesAndroidInjector
    abstract EquipmentInformationFragmentForRental contributeRentalCarInformationFragment();

    @ContributesAndroidInjector
    abstract EquipmentInformationFragmentForEquipmentDashboard contributeEquipmentInformationFragmentForEquipmentDashboard();

    @ContributesAndroidInjector
    abstract ContractSummaryForDeliveryFragment contributeDeliveryContractSummaryFragment();

    @ContributesAndroidInjector
    abstract ContractSummaryForRentalFragment contributeContractSummaryForRentalFragment();

    @ContributesAndroidInjector
    abstract FinePriceFragment contributeFinePriceFragment();

    @ContributesAndroidInjector
    abstract TransferInformationFragment contributeTransferInformationFragment();

    @ContributesAndroidInjector
    abstract DamageEntryFragmentForTransferReturn contributeDamageEntryFragmentForTransfer();

    @ContributesAndroidInjector
    abstract DamageEntryFragmentForTransferDelivery contributeDamageEntryFragmentForTransferDelivery();

    @ContributesAndroidInjector
    abstract DamageEntryFragmentForDelivery contributeDamageEntryFragmentForDelivery();

    @ContributesAndroidInjector
    abstract EquipmentInformationFragmentForTransfer contributeEquipmentInformationFragmentForTransfer();

    @ContributesAndroidInjector
    abstract TransferSummaryFragment contributeTransferSummaryFragment();

    @ContributesAndroidInjector
    abstract TransferSummaryForReturnFragment contributeTransferSummaryForReturnFragment();

    @ContributesAndroidInjector
    abstract BaseConfirmationDialog contributeBaseConfirmationDialog();

    @ContributesAndroidInjector
    abstract ReservationInformationFragment contributeReservationInformationFragment();

    @ContributesAndroidInjector
    abstract BaseCreditCardFragment contributeBaseCreditCardFragment();

    @ContributesAndroidInjector
    abstract BaseDepositCreditCardFragment contributeBaseDepositCreditCardFragment();

    @ContributesAndroidInjector
    abstract RepairedDamageListFragment contributeRepairedDamageListFragment();
}
