package com.creatifsoftware.rentgoservice.model;


import java.io.Serializable;

/**
 * Created by kerembalaban on 9.04.2019 at 01:43.
 */
public class HgsItem implements Serializable {
    public long entryDateTime;
    public long exitDateTime;
    public String entryLocation;
    public String exitLocation;
    public String description;
    public double amount;
}
