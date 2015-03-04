package com.iotticket.api.v1.model;

import com.google.gson.annotations.SerializedName;
import com.iotticket.api.v1.validation.APIRequirement;
import com.iotticket.api.v1.validation.Validatable;

import java.net.URI;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

public class Device implements Validatable {


    @APIRequirement(maxLength = 100, nullable = false)
    private String name;

    @APIRequirement(maxLength = 100, nullable = false)
    private String manufacturer;

    @APIRequirement(maxLength = 100)
    private String type;

    @APIRequirement(maxLength = 255)
    private String description;

    @APIRequirement(maxLength = 50)
    private Collection<DeviceAttribute> attributes = new HashSet<DeviceAttribute>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Collection<DeviceAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(Collection<DeviceAttribute> attributes) {
        this.attributes = attributes;
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Device [");
        if (name != null) {
            builder.append("name=");
            builder.append(name);
            builder.append(", ");
        }
        if (manufacturer != null) {
            builder.append("manufacturer=");
            builder.append(manufacturer);
            builder.append(", ");
        }
        if (type != null) {
            builder.append("type=");
            builder.append(type);
            builder.append(", ");
        }
        if (description != null) {
            builder.append("description=");
            builder.append(description);
            builder.append(", ");
        }
        if (attributes != null) {
            builder.append("attributes=");
            builder.append(attributes);
        }
        builder.append("]");
        return builder.toString();
    }


    /**
     * Represents a registered device.
     */
    public static class DeviceDetails extends Device {

        @SerializedName("href")
        private URI uri;
        private String deviceId;
        private Date createdAt;

        public URI getUri() {
            return uri;
        }

        public void setUri(URI uri) {
            this.uri = uri;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public Date getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(Date createdAt) {
            this.createdAt = createdAt;
        }


        @Override
        public String toString() {
            return "DeviceDetails{" +
                    "uri='" + uri + '\'' +
                    ", deviceId='" + deviceId + '\'' +
                    ", createdAt='" + createdAt + '\'' + super.toString() +
                    '}';
        }
    }


    public static class DeviceList extends PagedResult<DeviceDetails> {

    }
}
