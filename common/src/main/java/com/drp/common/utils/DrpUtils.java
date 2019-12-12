package com.drp.common.utils;

import java.util.List;

/**
 * @author dongruipeng
 * @Descrpition
 * @date 2019year 12month12day  15:06:09
 */
public class DrpUtils {

    public static<T> T isNotNull(T obj, String msg) {
        if (obj == null) {
            throw new NullPointerException(msg + "=> No corresponding data is obtained");
        } else {
            return obj;
        }
    }

    public static List arrayIsNotEmpty(List arr, String msg) {
        return arrayIsEmpty(arr, msg);
    }

    private static List arrayIsEmpty(List arr, String msg) {
        isNotNull(arr, msg);
        if(arr.size() == 0) {
            throw new ArrayIndexOutOfBoundsException(msg + "=> The array length is empty");
        }
        return arr;
    }
}
