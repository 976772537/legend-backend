package com.drp.common.utils;

import cn.hutool.captcha.generator.MathGenerator;

import java.util.HashMap;

/**
 * @author dongruipeng
 * @Descrpition
 * @date 2019year 12month12day  19:30:36
 */
//TODO temp ==> redis
public class Cache {
    private static HashMap<String, String> codeMap = new HashMap<>();

    public static boolean verifyCode(String key, String vlaue) {
        if (!codeMap.containsKey(key)) {
            return false;
        }

        final boolean flag = new MathGenerator().verify(codeMap.get(key), vlaue);
        if (flag) {
            codeMap.remove(key);
        }
        return flag;
    }

    public static HashMap<String, String> getCodeMap() {
        return codeMap;
    }
}
