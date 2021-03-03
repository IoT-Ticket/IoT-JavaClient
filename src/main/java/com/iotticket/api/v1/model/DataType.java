package com.iotticket.api.v1.model;

import com.google.gson.annotations.SerializedName;

import java.util.Base64;

public enum DataType {

    @SerializedName("double")
    DoubleType,

    @SerializedName("long")
    LongType,

    @SerializedName("string")
    StringType,

    @SerializedName("binary")
    BinaryType,

    @SerializedName("boolean")
    BooleanType;


    public Object getTypeValue(String value) throws NumberFormatException {
        switch (this) {
            case DoubleType:
                return Double.parseDouble(value);
            case LongType:
                return Long.parseLong(value);
            case StringType:
                return value;
            case BinaryType:
                return Base64.getDecoder().decode(value);
            case BooleanType:
                return Boolean.parseBoolean(value);
            default:
                throw new IllegalArgumentException("Unknown datatype " + this);
        }
    }
}