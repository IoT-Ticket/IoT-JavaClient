package com.iotticket.api.v1.model;




public class DataPathUtil {

    public static String getFullPath(String datanodeName, String datanodePath){
        if(datanodeName==null)throw new IllegalArgumentException("Datanode name can not be null");
        if(datanodePath==null){
            return "/"+datanodeName;
        }
        return "/"+datanodePath+"/"+datanodeName;
    }
}
