package com.creatifsoftware.rentgoservice.model;


import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by kerembalaban on 13.02.2019 at 00:41.
 */
public class Customer implements Serializable {
    public String customerId;
    public int customerType;
    public String customerCorporateId;
    public int segment;
    public String firstName;
    public String lastName;
    public String fullName;
    public String mobilePhone;
    public String email;
    public String drivingLicenseNumber;
    public File drivingLicenseFrontImage;
    public File drivingLicenseRearImage;
    //public Bitmap drivingLicenseFrontImageBitmap;
    //public Bitmap drivingLicenseRearImageBitmap;
    public ArrayList<CreditCard> cardList;
    public String priceCodeId;
    public int contractType;
    public int paymentMethod;

}
