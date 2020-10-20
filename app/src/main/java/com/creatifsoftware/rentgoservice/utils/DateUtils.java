package com.creatifsoftware.rentgoservice.utils;

import android.annotation.SuppressLint;
import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {
    private static final SimpleDateFormat DAY_MONTH_YEAR_FORMAT = new TrTimeZoneDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat HOUR_MINUTE_FORMAT = new TrTimeZoneDateFormat("HH:mm");


    static boolean isSameDay(Date first, Date second) {
        return DAY_MONTH_YEAR_FORMAT.format(first).equals(DAY_MONTH_YEAR_FORMAT.format(second));
    }

    static String getYearMonthDay(Date date) {
        return DAY_MONTH_YEAR_FORMAT.format(date);
    }

    @SuppressLint("SimpleDateFormat")
    public static String convertTimestampToStringDate(long dateInMillis) {
        //Calendar cal = Calendar.getInstance(Locale.getDefault());
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        cal.setTimeInMillis(dateInMillis * 1000L);
        return DateFormat.format("dd/MM/yyyy", cal).toString();
    }

    @SuppressLint("SimpleDateFormat")
    public static String convertTimestampToStringTime(long timeInMillis) {
        //Calendar cal = Calendar.getInstance(Locale.getDefault());
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        cal.setTimeInMillis(timeInMillis * 1000L);
        return DateFormat.format("HH:mm", cal).toString();
    }

    @SuppressLint("SimpleDateFormat")
    public static String convertTimestampToStringTimeWithoutTimeZone(long timeInMillis) {
        //Calendar cal = Calendar.getInstance(Locale.getDefault());
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Calendar cal = Calendar.getInstance(timeZone);
        cal.setTimeInMillis(timeInMillis * 1000L);
        return DateFormat.format("HH:mm", cal).toString();
    }

    @SuppressLint("SimpleDateFormat")
    public static String convertTimestampToStringDateTime(long dateTimeInMillis) {
        String date = convertTimestampToStringDate(dateTimeInMillis);
        String time = convertTimestampToStringTime(dateTimeInMillis);

        return String.format(Locale.getDefault(), "%s - %s", date, time);
    }

    @SuppressLint("SimpleDateFormat")
    public static String convertTimestampToStringDateTimeWithoutTimezone(long dateTimeInMillis) {
        String date = convertTimestampToStringDate(dateTimeInMillis);
        String time = convertTimestampToStringTimeWithoutTimeZone(dateTimeInMillis);

        return String.format(Locale.getDefault(), "%s - %s", date, time);
    }

    @SuppressLint("SimpleDateFormat")
    public static String getDateWithMonthName(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return new SimpleDateFormat("dd MMMM", new Locale("tr")).format(cal.getTime());
    }

    @SuppressLint("SimpleDateFormat")
    public static String getDateWithMonthYearName(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return new SimpleDateFormat("dd MMMM yyyy", new Locale("tr")).format(cal.getTime());
    }
}
