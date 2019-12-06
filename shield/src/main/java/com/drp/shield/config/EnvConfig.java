package com.drp.shield.config;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author dongruipeng
 * @Descrpition
 * @date 2019year 12month06day  01:00:03
 */
@Data
@Component
@PropertySource(value = {"classpath:common.properties"})
public class EnvConfig {
    @Value("${applciation.server-name}")
    private String serverName;
}
