package com.drp.common.utils;

import cn.hutool.core.io.FileTypeUtil;
import com.drp.common.exception.MyAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author dongruipeng
 * @Descrpition
 * @date 2019year 12month12day  15:06:09
 */
public class DrpUtils {

    public static <T> T isNotNull(T obj, String msg) {
        if (obj == null) {
            throw new NullPointerException(msg + " => No corresponding data is obtained");
        } else {
            return obj;
        }
    }

    public static List arrayIsNotEmpty(List arr, String msg) {
        return arrayIsEmpty(arr, msg);
    }

    private static List arrayIsEmpty(List arr, String msg) {
        isNotNull(arr, msg);
        if (arr.size() == 0) {
            throw new ArrayIndexOutOfBoundsException(msg + " => The array length is empty");
        }
        return arr;
    }

    public static String getIpAddr(HttpHeaders headers) {
        String ip = getHeader("x-forwarded-for", headers);
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = getHeader("Proxy-Client-IP", headers);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = getHeader("WL-Proxy-Client-IP", headers);
        }
        if (ip != null && !ip.isEmpty() && ip.indexOf(",") > 0) {
            ip = ip.split(",")[0];
        }
        return ip;
    }

    public static String getHeader(String name, HttpHeaders headers) {
        List<String> value = headers.get(name);
        return CollectionUtils.isEmpty(value) ? null : value.get(0);
    }

    public static String verifyImageType(InputStream inputStream) {
        final String type = FileTypeUtil.getType(inputStream);
        if (!type.equals("jpg") && !type.equals("png")) {
            throw new MyAccessException("wrong file type");
        }
        return type;
    }


    public static String fullFilePath(String headImage) {
        //TODO schema:// + domain + headImage
        return headImage;
    }

    public static void transMultipartFileTo(MultipartFile multipartFile,
                                            String filename,
                                            String filePath) throws IOException {
        final File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        final Path path = Paths.get(filePath, filename);
        multipartFile.transferTo(path);
    }
}
