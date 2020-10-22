package com.creatifsoftware.filonova.di;

import com.creatifsoftware.filonova.viewmodel.AdditionalProductsViewModel;
import com.creatifsoftware.filonova.viewmodel.AdditionalServicesViewModel;
import com.creatifsoftware.filonova.viewmodel.CarsListViewModel;
import com.creatifsoftware.filonova.viewmodel.ContractInformationViewModel;
import com.creatifsoftware.filonova.viewmodel.ContractListViewModel;
import com.creatifsoftware.filonova.viewmodel.ContractSummaryViewModel;
import com.creatifsoftware.filonova.viewmodel.CreateTransferViewModel;
import com.creatifsoftware.filonova.viewmodel.DamageEntryViewModel;
import com.creatifsoftware.filonova.viewmodel.DeliveryCarInformationViewModel;
import com.creatifsoftware.filonova.viewmodel.DeliveryTransferSummaryViewModel;
import com.creatifsoftware.filonova.viewmodel.EquipmentListViewModel;
import com.creatifsoftware.filonova.viewmodel.FinePricesViewModel;
import com.creatifsoftware.filonova.viewmodel.LoginViewModel;
import com.creatifsoftware.filonova.viewmodel.RentalCarInformationViewModel;
import com.creatifsoftware.filonova.viewmodel.RentalContractSummaryViewModel;
import com.creatifsoftware.filonova.viewmodel.ReservationInformationViewModel;
import com.creatifsoftware.filonova.viewmodel.ReturnTransferSummaryViewModel;
import com.creatifsoftware.filonova.viewmodel.TransferListViewModel;

import dagger.Subcomponent;

@Subcomponent
public interface ViewModelSubComponent {
    CarsListViewModel carListViewModel();

    ContractListViewModel contractListViewModel();

    ContractInformationViewModel contractInformationViewModel();

    EquipmentListViewModel equipmentListViewModel();

    DamageEntryViewModel damageEntryViewModel();

    DeliveryCarInformationViewModel deliveryCarInformationViewModel();

    RentalCarInformationViewModel rentalCarInformationViewModel();

    RentalContractSummaryViewModel rentalContractSummaryViewModel();

    AdditionalProductsViewModel additionalProductsViewModel();

    AdditionalServicesViewModel additionalServicesViewModel();

    ContractSummaryViewModel deliveryContractSummaryViewModel();

    LoginViewModel loginViewModel();

    FinePricesViewModel finePricesViewModel();

    CreateTransferViewModel createTransferViewModel();

    TransferListViewModel transferListViewModel();

    DeliveryTransferSummaryViewModel deliveryTransferSummaryViewModel();

    ReturnTransferSummaryViewModel returnTransferSummaryViewModel();

    ReservationInformationViewModel reservationInformationViewModel();

    @Subcomponent.Builder
    interface Builder {
        ViewModelSubComponent build();
    }
    //ProjectViewModel projectViewModel();
}
