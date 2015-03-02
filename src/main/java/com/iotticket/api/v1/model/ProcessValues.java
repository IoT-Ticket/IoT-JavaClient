package com.iotticket.api.v1.model;

import com.google.gson.annotations.SerializedName;
import com.iotticket.api.v1.model.Datanode.DatanodeRead;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;


public class ProcessValues {


    @SerializedName("href")
    private URI Uri;

    @SerializedName("datanodeReads")
    private Collection<DatanodeRead> datanodeReads = new ArrayList<DatanodeRead>();

    public URI getUri() {
        return Uri;
    }

    public void setUri(URI uri) {
        Uri = uri;
    }

    public Collection<DatanodeRead> getDatanodeReads() {
        return datanodeReads;
    }


}
