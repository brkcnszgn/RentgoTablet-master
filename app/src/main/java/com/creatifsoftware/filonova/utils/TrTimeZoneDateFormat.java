package com.creatifsoftware.filonova.utils;


import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

public final class TrTimeZoneDateFormat extends SimpleDateFormat {

    private static final String TURKEY_TIME_ZONE = "GMT+03:00";

    public TrTimeZoneDateFormat(@NonNull String pattern) {
        super(pattern);
        setTimeZone(TimeZone.getTimeZone(TURKEY_TIME_ZONE));
    }
}
