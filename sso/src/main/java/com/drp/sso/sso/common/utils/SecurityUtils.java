package com.drp.sso.sso.common.utils;

import cn.hutool.crypto.SecureUtil;

/**
 * @author dongruipeng
 * @Descrpition
 * @date 2019year 12month05day  15:49:11
 */
public class SecurityUtils {
    private static final String passwordKey = "password";

    public static String securityHmac(String content) {
        return SecureUtil.hmacMd5(passwordKey).digestHex(content);
    }
}
