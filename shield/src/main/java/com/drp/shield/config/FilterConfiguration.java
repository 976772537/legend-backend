package com.drp.shield.config;

import com.drp.shield.core.filter.FaviconFilter;
import com.drp.shield.core.filter.HealthCheckFilter;
import com.drp.shield.core.filter.NakedDomainFilter;
import com.drp.shield.core.filter.ReverseProxyFilter;
import com.drp.shield.core.filter.SecurityFilter;
import com.drp.shield.core.http.RequestForwarder;
import com.drp.shield.core.trace.ProxyingTraceInterceptor;
import com.drp.shield.view.AssetLoader;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final AssetLoader assetLoader;
    private final ProxyingTraceInterceptor interceptor;
    private final ShieldProperties shieldProperties;
    private final RequestForwarder requestForwarder;

    @Autowired
    public FilterConfiguration(AssetLoader assetLoader,
                               ProxyingTraceInterceptor interceptor,
                               ShieldProperties shieldProperties,
                               RequestForwarder requestForwarder) {
        this.assetLoader = assetLoader;
        this.interceptor = interceptor;
        this.shieldProperties = shieldProperties;
        this.requestForwarder = requestForwarder;
    }

    @Bean
    public FilterRegistrationBean<HealthCheckFilter> healthCheckFilterRegistrationBean() {
        FilterRegistrationBean<HealthCheckFilter> registrationBean =
                new FilterRegistrationBean<>(new HealthCheckFilter());
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 70); // before faviconFilter
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<FaviconFilter> faviconFilterRegistrationBean() {
        FilterRegistrationBean<FaviconFilter> registrationBean =
                new FilterRegistrationBean<>(new FaviconFilter(assetLoader.getFaviconBytes()));
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 75); // before securityFilter
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<SecurityFilter> securityFilterRegistrationBean() {
        FilterRegistrationBean<SecurityFilter> registrationBean =
                new FilterRegistrationBean<>(new SecurityFilter());
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 80); // before NakedDomainFilter
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<NakedDomainFilter> nakedDomainFilterRegistrationBean() {
        FilterRegistrationBean<NakedDomainFilter> registrationBean =
                new FilterRegistrationBean<>(new NakedDomainFilter());
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 90); // before ReverseProxyFilter
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<ReverseProxyFilter> reverseProxyFilterFilterRegistrationBean() {
        final FilterRegistrationBean<ReverseProxyFilter> registrationBean =
                new FilterRegistrationBean<>(new ReverseProxyFilter(
                        interceptor,
                        shieldProperties,
                        requestForwarder
                ));
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 100);
        return registrationBean;
    }
}
