package com.drp.shield.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dongruipeng
 * @Descrpition
 * @date 2019year 12month09day  15:12:05
 */
@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MappingProperties {
    /**
     * Name of the mapping
     */
    private String name;
    /**
     * Path for mapping incoming HTTP requests URIs.
     */
    private String host = "";
    /**
     * The uri that the destination includes
     */
    private List<String> uris = new ArrayList<>();
    /**
     * List of destination hosts where HTTP requests will be forwarded.
     */
    private List<String> destinations = new ArrayList<>();
    /**
     * Properties responsible for timeout while forwarding HTTP requests.
     */
    private TimeoutProperties timeout = new TimeoutProperties();

    /**
     * Custom properties placeholder.
     */
    private Map<String, Object> customConfiguration = new HashMap<>();

    public MappingProperties copy() {
        MappingProperties clone = new MappingProperties();
        clone.setName(name);
        clone.setHost(host);
        clone.setUris(uris == null ? null : new ArrayList<>(uris));
        clone.setDestinations(destinations == null ? null : new ArrayList<>(destinations));
        clone.setTimeout(timeout);
        clone.setCustomConfiguration(customConfiguration == null ? null : new HashMap<>(customConfiguration));
        return clone;
    }

    @Getter
    @Setter
    @ToString
    public class TimeoutProperties {
        /**
         * Connect timeout for HTTP requests forwarding.
         */
        private int connect = 2000;
        /**
         * Read timeout for HTTP requests forwarding.
         */
        private int read = 20000;
    }
}
