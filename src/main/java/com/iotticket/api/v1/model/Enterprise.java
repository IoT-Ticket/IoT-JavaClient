package com.iotticket.api.v1.model;

import com.google.gson.annotations.SerializedName;

import java.net.URI;

public class Enterprise {

    @SerializedName("href")
    private URI uri;

    private String name;
    private String resourceId;
    private boolean hasSubEnterprises;

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public boolean getHasSubEnterprises() {
        return hasSubEnterprises;
    }

    public void setHasSubEnterprises(boolean hasSubEnterprises) {
        this.hasSubEnterprises = hasSubEnterprises;
    }

    public static class EnterpriseList extends PagedResult<Enterprise> {

    }

}
