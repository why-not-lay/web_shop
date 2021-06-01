package com.web.web_shop.Configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * StorageProperties
 */
@ConfigurationProperties("storage")
public class StorageProperties {

    private String location;

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }
}
