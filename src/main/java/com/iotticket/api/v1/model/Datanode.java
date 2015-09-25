package com.iotticket.api.v1.model;

import com.google.gson.annotations.SerializedName;
import com.iotticket.api.v1.validation.APIRequirement;
import org.apache.commons.codec.binary.Base64;

import java.net.URI;
import java.util.Collection;
import java.util.LinkedList;


public class Datanode extends DatanodeBase {

    @SerializedName("href")
    private URI uri;

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }


    public static class DatanodeWriteValue extends DatanodeBase {


        @SerializedName("v")
        @APIRequirement(nullable = false)
        private String value;


        @SerializedName("ts")
        private Long timestampMilliseconds;


        public String getValue() {
            return value;
        }

        public void setValue(Object value) {
            if (value == null) {
                this.value = null;
                return;
            }
            if (value instanceof Double) {
                setDoubleValue((Double) value);

            } else if (value instanceof Integer) {
                setIntegerValue((Integer) value);
            } else if (value instanceof Long) {
                setLongValue((Long) value);

            } else if (value instanceof byte[]) {
                setByteValue((byte[]) value);
            } else if (value instanceof Boolean) {
                setBooleanValue((Boolean) value);

            } else if (value instanceof Float) {
                setFloat((Float) value);
            } else setInternal(String.valueOf(value));

        }

        private void setFloat(Float f) {
            setDataType(DataType.DoubleType);
            setInternal(Float.toString(f));
        }

        public void setDoubleValue(double d) {
            setDataType(DataType.DoubleType);
            setInternal(Double.toString(d));
        }

        private void setInternal(String value) {
            this.value = value;
        }

        private void setIntegerValue(int i) {
            setDataType(DataType.LongType);
            setInternal(Integer.toString(i));
        }

        public void setLongValue(long l) {
            setDataType(DataType.LongType);
            setInternal(Long.toString(l));
        }

        public void setBooleanValue(boolean bool) {
            setDataType(DataType.BooleanType);
            setInternal(Boolean.toString(bool));
        }

        public void setByteValue(byte[] bytes) {
            String encode = Base64.encodeBase64String(bytes);
            setDataType(DataType.BinaryType);
            setInternal(encode);
        }


        public Long getTimestampMilliseconds() {
            return timestampMilliseconds;
        }

        public void setTimestampMilliseconds(Long timestampMilliseconds) {
            this.timestampMilliseconds = timestampMilliseconds;
        }
    }

    public static class DataNodeList extends PagedResult<Datanode> {
    }

    public static class DatanodeRead extends DatanodeBase {


        @SerializedName("values")
        private Collection<DatanodeReadValue> datanodeReadValues = new LinkedList<DatanodeReadValue>();


        public Collection<DatanodeReadValue> getDatanodeReadValues() {
            return datanodeReadValues;
        }


    }

    public static class DatanodeReadValue {

        public static final long UNSET = Long.MIN_VALUE;


        @SerializedName("v")
        private String value;

        @SerializedName("ts")
        private long timestampMilliSeconds = UNSET;
        private transient DataType dataType;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public long getTimestampMilliSeconds() {
            return timestampMilliSeconds;
        }

        public void setTimestampMilliSeconds(long timestampMilliSeconds) {
            this.timestampMilliSeconds = timestampMilliSeconds;
        }

        public Object getConvertedValue() {

            if (value == null) return null;

            return dataType.getTypeValue(value);

        }


        public void setDataType(DataType dataType) {
            this.dataType = dataType;
        }
    }
}