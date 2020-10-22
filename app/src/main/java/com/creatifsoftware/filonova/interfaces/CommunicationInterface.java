package com.creatifsoftware.filonova.interfaces;

import com.creatifsoftware.filonova.model.AdditionalProduct;
import com.creatifsoftware.filonova.model.AvailabilityGroupCodeInformation;
import com.creatifsoftware.filonova.model.CreditCard;
import com.creatifsoftware.filonova.model.DamageItem;

/**
 * Created by kerembalaban on 13.03.2019 at 01:43.
 */
public interface CommunicationInterface {
    void onKilometerSelected(String kilometer);

    void searchContractByPlateNumber(String plateNumber);

    void addCustomExtraPayment(AdditionalProduct item);

    void groupCodeChanged(AvailabilityGroupCodeInformation groupCodeInformation);

    void messageDialogDoneButtonClicked();

    void getInputValue(String input, Integer tag);

    void addDamageItem(DamageItem damageItem);

    void addCreditCard(CreditCard creditCard);

    void addDepositCard(CreditCard creditCard);

    void confirmButtonClicked();

    void confirmButtonClickedForActivity();
}