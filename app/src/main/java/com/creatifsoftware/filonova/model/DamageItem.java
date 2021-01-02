package com.creatifsoftware.filonova.model;

import android.graphics.Bitmap;

import com.creatifsoftware.filonova.utils.DateUtils;

import java.io.File;
import java.io.Serializable;

/**
 * Created by kerembalaban on 13.02.2019 at 00:49.
 */
public class DamageItem implements Serializable {
    public String damageId;
    public DamageDocument damageDocument;
    public DamageSize damageSize;
    public DamageType damageType;
    public EquipmentPart equipmentPart;
    public DamageInfo damageInfo;
    public double damageAmount;
    public File damagePhotoFile;
    public File damagePhotoFileDocument;
    public Bitmap damagePhotoBitmap;
    public String blobStoragePath;
    public String blobStoragePathDocument;
    public boolean isPriceCalculated;
    public boolean isRepaired = false;

    public String convertTimestampToDateTime(long timestamp) {
        return DateUtils.convertTimestampToStringDateTime(timestamp);
    }
}

