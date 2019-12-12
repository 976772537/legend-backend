package com.drp.shield.config;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author dongruipeng
 * @Descrpition
 * @date 2019year 12month06day  01:00:03
 */
@Getter
@Component
@PropertySource(value = {"classpath:security.properties"})
public class SecurityConfig {
    @Value("${server.name}")
    private String serverName;
    @Value("${server.favicon}")
    private String faviconPath;
    @Value("${server.permit-paths}")
    private List<String> permitPaths;

}
