package com.drp.shield.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

/**
 * @author dongruipeng
 * @Descrpition
 * @date 2019year 12month07day  15:58:10
 */
@Getter
@Setter
@ConfigurationProperties("shield")
public class ShieldProperties {
    /**
     * shield servlet filter order.
     */
    private int filterOrder = HIGHEST_PRECEDENCE + 100;
    /**
     * Enable programmatic mapping or not,
     * false only in dev environment, in dev we use mapping via configuration file
     */
    private boolean enableProgrammaticMapping = true;
    /**
     * Properties responsible for collecting metrics during HTTP requests forwarding.
     */
    @NestedConfigurationProperty
    private MetricsProperties metrics = new MetricsProperties();
    /**
     * Properties responsible for tracing HTTP requests proxying processes.
     */
    @NestedConfigurationProperty
    private TracingProperties tracing = new TracingProperties();
    /**
     * List of proxy mappings.
     */
    @NestedConfigurationProperty
    private List<MappingProperties> mappings = new ArrayList<>();

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class TracingProperties {
        /**
         * Flag for enabling and disabling tracing HTTP requests proxying processes.
         */
        private boolean enabled;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class MetricsProperties {
        private String namesPrefix = "shield";
    }

}
