package com.drp.shield.config;

import com.drp.shield.core.filter.SecurityFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * @author dongruipeng
 * @Descrpition
 * @date 2019year 12month05day  23:00:11
 */
@Configuration
public class FilterConfiguration {
    @Bean
    public FilterRegistrationBean<SecurityFilter> securityFilterFilterRegistrationBean(){
        FilterRegistrationBean<SecurityFilter> registrationBean =
                new FilterRegistrationBean<>(new SecurityFilter());
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 80); // before NakedDomainFilter
        return registrationBean;
    }
}
