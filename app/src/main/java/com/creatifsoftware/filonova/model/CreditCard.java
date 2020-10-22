package com.creatifsoftware.filonova.model;


import java.io.Serializable;

/**
 * Created by kerembalaban on 13.02.2019 at 00:43.
 */
public class CreditCard implements Serializable {
    public String creditCardId;
    public String creditCardNumber;
    public String binNumber;
    public String conversationId;
    public String externalId;
    public String cardUserKey; // yeni kart girişinde null olacak
    public String cardToken; // yeni kart girişinde null olacak
    public String cardHolderName;
    public String cvc;
    public int cardType;
    public int expireYear;
    public int expireMonth;
    public boolean isSelected;
}
