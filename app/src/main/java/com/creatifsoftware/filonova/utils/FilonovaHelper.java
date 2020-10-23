package com.creatifsoftware.filonova.utils;

/**
 * Created by hamurcuabi on 23,October,2020
 **/
public class FilonovaHelper {

    public static String getPercent(int total, int selected) {
        return "%" + (selected * 100) / total;
    }
}
