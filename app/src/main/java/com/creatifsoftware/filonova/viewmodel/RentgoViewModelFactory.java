package com.creatifsoftware.filonova.viewmodel;


import androidx.annotation.NonNull;
import androidx.collection.ArrayMap;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.creatifsoftware.filonova.di.ViewModelSubComponent;

import java.util.Map;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RentgoViewModelFactory implements ViewModelProvider.Factory {
    private final ArrayMap<Class, Callable<? extends ViewModel>> creators;

    @Inject
    public RentgoViewModelFactory(ViewModelSubComponent viewModelSubComponent) {
        creators = new ArrayMap<>();
        // View models cannot be injected directly because they won't be bound to the owner's view model scope.
        creators.put(ContractListViewModel.class, viewModelSubComponent::contractListViewModel);
        creators.put(CarsListViewModel.class, viewModelSubComponent::carListViewModel);
        creators.put(ContractInformationViewModel.class, viewModelSubComponent::contractInformationViewModel);
        creators.put(EquipmentListViewModel.class, viewModelSubComponent::equipmentListViewModel);
        creators.put(DamageEntryViewModel.class, viewModelSubComponent::damageEntryViewModel);
        creators.put(DeliveryCarInformationViewModel.class, viewModelSubComponent::deliveryCarInformationViewModel);
        creators.put(AdditionalProductsViewModel.class, viewModelSubComponent::additionalProductsViewModel);
        creators.put(LoginViewModel.class, viewModelSubComponent::loginViewModel);
        creators.put(AdditionalServicesViewModel.class, viewModelSubComponent::additionalServicesViewModel);
        creators.put(RentalCarInformationViewModel.class, viewModelSubComponent::rentalCarInformationViewModel);
        creators.put(RentalContractSummaryViewModel.class, viewModelSubComponent::rentalContractSummaryViewModel);
        creators.put(ContractSummaryViewModel.class, viewModelSubComponent::deliveryContractSummaryViewModel);
        creators.put(FinePricesViewModel.class, viewModelSubComponent::finePricesViewModel);
        creators.put(CreateTransferViewModel.class, viewModelSubComponent::createTransferViewModel);
        creators.put(TransferListViewModel.class, viewModelSubComponent::transferListViewModel);
        creators.put(DeliveryTransferSummaryViewModel.class, viewModelSubComponent::deliveryTransferSummaryViewModel);
        creators.put(ReturnTransferSummaryViewModel.class, viewModelSubComponent::returnTransferSummaryViewModel);
        creators.put(ReservationInformationViewModel.class, viewModelSubComponent::reservationInformationViewModel);
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        Callable<? extends ViewModel> creator = creators.get(modelClass);
        if (creator == null) {
            for (Map.Entry<Class, Callable<? extends ViewModel>> entry : creators.entrySet()) {
                if (modelClass.isAssignableFrom(entry.getKey())) {
                    creator = entry.getValue();
                    break;
                }
            }
        }
        if (creator == null) {
            throw new IllegalArgumentException("Unknown model class " + modelClass);
        }
        try {
            return (T) creator.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}