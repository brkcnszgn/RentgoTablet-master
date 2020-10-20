package com.creatifsoftware.rentgoservice.di;

import com.creatifsoftware.rentgoservice.viewmodel.AdditionalProductsViewModel;
import com.creatifsoftware.rentgoservice.viewmodel.AdditionalServicesViewModel;
import com.creatifsoftware.rentgoservice.viewmodel.CarsListViewModel;
import com.creatifsoftware.rentgoservice.viewmodel.ContractInformationViewModel;
import com.creatifsoftware.rentgoservice.viewmodel.ContractListViewModel;
import com.creatifsoftware.rentgoservice.viewmodel.ContractSummaryViewModel;
import com.creatifsoftware.rentgoservice.viewmodel.CreateTransferViewModel;
import com.creatifsoftware.rentgoservice.viewmodel.DamageEntryViewModel;
import com.creatifsoftware.rentgoservice.viewmodel.DeliveryCarInformationViewModel;
import com.creatifsoftware.rentgoservice.viewmodel.DeliveryTransferSummaryViewModel;
import com.creatifsoftware.rentgoservice.viewmodel.EquipmentListViewModel;
import com.creatifsoftware.rentgoservice.viewmodel.FinePricesViewModel;
import com.creatifsoftware.rentgoservice.viewmodel.LoginViewModel;
import com.creatifsoftware.rentgoservice.viewmodel.RentalCarInformationViewModel;
import com.creatifsoftware.rentgoservice.viewmodel.RentalContractSummaryViewModel;
import com.creatifsoftware.rentgoservice.viewmodel.ReservationInformationViewModel;
import com.creatifsoftware.rentgoservice.viewmodel.ReturnTransferSummaryViewModel;
import com.creatifsoftware.rentgoservice.viewmodel.TransferListViewModel;

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
