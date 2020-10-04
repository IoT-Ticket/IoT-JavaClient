package com.iotticket.api.v1.model;

import com.google.gson.annotations.SerializedName;

import javax.xml.bind.DatatypeConverter;

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
                return DatatypeConverter.parseBase64Binary(value);
            case BooleanType:
                return Boolean.parseBoolean(value);
            default:
                throw new IllegalArgumentException("Unknown datatype " + this);
        }
    }
}