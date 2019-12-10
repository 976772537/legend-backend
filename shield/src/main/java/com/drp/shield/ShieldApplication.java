package com.drp.shield;

import com.drp.shield.config.ShieldProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author dongruipeng
 * @Descrpition
 * @date 2019year 12month05day  19:32:18
 */
@EnableConfigurationProperties({ShieldProperties.class})
@SpringBootApplication(scanBasePackages = {"com.drp"})
public class ShieldApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShieldApplication.class, args);
    }

}
