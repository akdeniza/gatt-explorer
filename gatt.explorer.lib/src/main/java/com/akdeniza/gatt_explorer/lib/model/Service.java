package com.akdeniza.gatt_explorer.lib.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Akdeniz on 02/03/2017.
 */

public class Service {

    @SerializedName("uuid")
    private String uuuid;

    @SerializedName("name")
    private String name;


    @SerializedName("characteristics")
    private List<Characteristic> characteristics;

    public String getUuuid() {
        return uuuid;
    }

    public String getName() {
        return name;
    }

    public List<Characteristic> getCharacteristics() {
        return characteristics;
    }

    @Override
    public String toString() {
        return "Service{" +
                "uuuid='" + uuuid + '\'' +
                ", name='" + name + '\'' +
                ", characteristics=" + characteristics +
                '}';
    }
}
