package com.creatifsoftware.rentgoservice.utils;

import java.util.Locale;

public final class LocaleUtils {

    public static final Locale TR = new Locale("TR", "tr");
    public static final String LANGUAGE_EN = "en";
    public static final String LANGUAGE_TR = "tr";

    private LocaleUtils() {
        // no instance
    }

    public static String getLang() {
        String language = Locale.getDefault().getLanguage();
        if (LANGUAGE_TR.equals(language) || LANGUAGE_EN.equals(language)) {
            return language;
        } else {
            return LANGUAGE_EN;
        }
    }
}
