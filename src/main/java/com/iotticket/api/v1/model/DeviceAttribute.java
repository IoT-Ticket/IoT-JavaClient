package com.iotticket.api.v1.model;


import com.iotticket.api.v1.validation.APIRequired;
import com.iotticket.api.v1.validation.Validatable;

public class DeviceAttribute implements Validatable {

    @APIRequired(maxLength = 255, nullable = false)
    private String key;

    @APIRequired(maxLength = 255, nullable = false)
    private String value;

    public DeviceAttribute(String key, String value) {
        setKey(key);
        setValue(value);
    }

    public DeviceAttribute() {

    }

    @Override
    public String toString() {
        return "Attribute{" + "key='" + key + '\'' + ", value='" + value + '\'' + '}';
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
