package com.siebre.uaa.openapiapplication.entity;

public class OpenApiApplication {

    private Long id;

    private Long developerAccountId;

    private Long interfaceId;

    private String appId;

    private String appAuthToken;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDeveloperAccountId() {
        return developerAccountId;
    }

    public void setDeveloperAccountId(Long developerAccountId) {
        this.developerAccountId = developerAccountId;
    }

    public Long getInterfaceId() {
        return interfaceId;
    }

    public void setInterfaceId(Long interfaceId) {
        this.interfaceId = interfaceId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId == null ? null : appId.trim();
    }

    public String getAppAuthToken() {
        return appAuthToken;
    }

    public void setAppAuthToken(String appAuthToken) {
        this.appAuthToken = appAuthToken == null ? null : appAuthToken.trim();
    }
}