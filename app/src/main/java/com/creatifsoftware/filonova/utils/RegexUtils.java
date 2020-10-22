package com.creatifsoftware.filonova.utils;

import android.text.TextUtils;

public final class RegexUtils {

    /**
     * Creates turkish regex for given string. Only works for lowercase characters.
     *
     * @param string Input string
     * @return Turkish regex for given strings
     */

    public static final RegexUtils instance = new RegexUtils();

    private RegexUtils() {
        //no instance
    }

    public String createTurkishRegexFor(String string) {
        if (TextUtils.isEmpty(string)) {
            return string;
        }

        StringBuilder regexBuilder = new StringBuilder();
        for (char c : string.toCharArray()) {
            if (c == 'i' || c == 'ı') {
                regexBuilder.append("[iı]");
            } else if (c == 's' || c == 'ş') {
                regexBuilder.append("[sş]");
            } else if (c == 'o' || c == 'ö') {
                regexBuilder.append("[oö]");
            } else if (c == 'c' || c == 'ç') {
                regexBuilder.append("[cç]");
            } else if (c == 'g' || c == 'ğ') {
                regexBuilder.append("[gğ]");
            } else if (c == 'u' || c == 'ü') {
                regexBuilder.append("[uü]");
            } else {
                regexBuilder.append(String.format("[%s]", c));
            }
        }

        String str = regexBuilder.toString();
        //str = str.replaceAll("[\\[\\]]", "");

        return str;
    }

    public String visaCardRegex() {
        return "^4[0-9]{6,}$";
    }

    public String masterCardCardRegex() {
        return "^5[1-5][0-9]{5,}$";
    }

    public String amexCardCardRegex() {
        return "^3[47][0-9]{5,}$";
    }
}
