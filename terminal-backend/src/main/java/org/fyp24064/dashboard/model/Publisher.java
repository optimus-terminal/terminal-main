package org.fyp24064.dashboard.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Publisher {
    @JsonProperty("name")
    private String name;
    @JsonProperty("homepage_url")
    private String homepageUrl;
    @JsonProperty("logo_url")
    private String logoUrl;
    @JsonProperty("favicon_url")
    private String faviconUrl;
}