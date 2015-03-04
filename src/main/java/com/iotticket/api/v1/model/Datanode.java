package com.iotticket.api.v1.model;

import com.google.gson.annotations.SerializedName;
import com.iotticket.api.v1.validation.APIRequirement;

import javax.xml.bind.DatatypeConverter;
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
        private Long timestampMiliseconds;


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
            String encode = DatatypeConverter.printBase64Binary(bytes);
            setDataType(DataType.BinaryType);
            setInternal(encode);
        }


        public Long getTimestampMiliseconds() {
            return timestampMiliseconds;
        }

        public void setTimestampMiliseconds(Long timestampMiliseconds) {
            this.timestampMiliseconds = timestampMiliseconds;
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
        private long timestampMilliSecond = UNSET;
        private transient DataType dataType;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public long getTimestampMilliSecond() {
            return timestampMilliSecond;
        }

        public void setTimestampMilliSecond(long timestampMilliSecond) {
            this.timestampMilliSecond = timestampMilliSecond;
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
