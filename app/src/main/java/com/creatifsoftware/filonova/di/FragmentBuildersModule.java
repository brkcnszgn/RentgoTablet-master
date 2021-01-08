package com.creatifsoftware.filonova.di;

import com.creatifsoftware.filonova.view.fragment.EquipmentListFragment;
import com.creatifsoftware.filonova.view.fragment.FinePriceFragment;
import com.creatifsoftware.filonova.view.fragment.ReservationInformationFragment;
import com.creatifsoftware.filonova.view.fragment.additionalProducts.AdditionalProductsFragment;
import com.creatifsoftware.filonova.view.fragment.additionalProducts.AdditionalServicesFragment;
import com.creatifsoftware.filonova.view.fragment.additionalphotos.AdditionaPhotoFragment;
import com.creatifsoftware.filonova.view.fragment.additionalphotos.AdditionaPhotoRentalFragment;
import com.creatifsoftware.filonova.view.fragment.base.BaseConfirmationDialog;
import com.creatifsoftware.filonova.view.fragment.base.BaseCreditCardFragment;
import com.creatifsoftware.filonova.view.fragment.base.BaseDepositCreditCardFragment;
import com.creatifsoftware.filonova.view.fragment.base.BaseEquipmentPartSelectionDialog;
import com.creatifsoftware.filonova.view.fragment.base.BaseErrorDialog;
import com.creatifsoftware.filonova.view.fragment.base.BaseExtraPaymentDialog;
import com.creatifsoftware.filonova.view.fragment.base.BaseGroupCodeChangeFragment;
import com.creatifsoftware.filonova.view.fragment.base.BaseInputDialog;
import com.creatifsoftware.filonova.view.fragment.base.BaseKilometerDialog;
import com.creatifsoftware.filonova.view.fragment.base.BaseLoadingDialog;
import com.creatifsoftware.filonova.view.fragment.base.BaseLogoutDialog;
import com.creatifsoftware.filonova.view.fragment.base.BasePlateNumberDialog;
import com.creatifsoftware.filonova.view.fragment.base.BasePreviewImageFragment;
import com.creatifsoftware.filonova.view.fragment.contractInformation.ContractInformationForDeliveryFragment;
import com.creatifsoftware.filonova.view.fragment.contractInformation.ContractInformationForRentalFragment;
import com.creatifsoftware.filonova.view.fragment.contractSummary.ContractSummaryForDeliveryFragment;
import com.creatifsoftware.filonova.view.fragment.contractSummary.ContractSummaryForRentalFragment;
import com.creatifsoftware.filonova.view.fragment.damageEntry.DamageEntryFragment;
import com.creatifsoftware.filonova.view.fragment.damageEntry.DamageEntryFragmentForDelivery;
import com.creatifsoftware.filonova.view.fragment.damageEntry.DamageEntryFragmentForRental;
import com.creatifsoftware.filonova.view.fragment.damageEntry.DamageEntryFragmentForTransferDelivery;
import com.creatifsoftware.filonova.view.fragment.damageEntry.DamageEntryFragmentForTransferReturn;
import com.creatifsoftware.filonova.view.fragment.dashboards.ContractDashboardFragment;
import com.creatifsoftware.filonova.view.fragment.dashboards.EquipmentDashboardFragment;
import com.creatifsoftware.filonova.view.fragment.dashboards.TransferDashboardFragment;
import com.creatifsoftware.filonova.view.fragment.equipmentInformation.EquipmentInformationFragment;
import com.creatifsoftware.filonova.view.fragment.equipmentInformation.EquipmentInformationFragmentForDelivery;
import com.creatifsoftware.filonova.view.fragment.equipmentInformation.EquipmentInformationFragmentForEquipmentDashboard;
import com.creatifsoftware.filonova.view.fragment.equipmentInformation.EquipmentInformationFragmentForRental;
import com.creatifsoftware.filonova.view.fragment.equipmentInformation.EquipmentInformationFragmentForTransfer;
import com.creatifsoftware.filonova.view.fragment.transfers.CreateTransferFragment;
import com.creatifsoftware.filonova.view.fragment.transfers.EquipmentListForTransferFragment;
import com.creatifsoftware.filonova.view.fragment.transfers.RepairedDamageListFragment;
import com.creatifsoftware.filonova.view.fragment.transfers.TransferInformationFragment;
import com.creatifsoftware.filonova.view.fragment.transfers.TransferSummaryForReturnFragment;
import com.creatifsoftware.filonova.view.fragment.transfers.TransferSummaryFragment;

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

    @ContributesAndroidInjector
    abstract AdditionaPhotoFragment contributeAdditionaPhotoFragment();

    @ContributesAndroidInjector
    abstract AdditionaPhotoRentalFragment contributeAdditionaPhotoRentalFragment();
}
