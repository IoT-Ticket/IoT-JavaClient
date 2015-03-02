package com.iotticket.api.v1.model;

import com.iotticket.api.v1.validation.APIRequired;
import com.iotticket.api.v1.validation.Validatable;

public abstract class DatanodeBase implements Validatable {


    @APIRequired(maxLength = 100, nullable = false)
    private String name;

    @APIRequired(maxLength = 10)
    private String unit;

    private DataType dataType;

    @APIRequired(maxLength = 1000, regexPattern = "(\\/[a-zA-Z0-9]+){1,10}")
    private String path;

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        if (path != null && !path.isEmpty() && !path.startsWith("/")) {
            this.path = "/" + path;
        } else {
            this.path = path;
        }
    }


}
