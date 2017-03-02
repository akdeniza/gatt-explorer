package com.akdeniza.gatt_explorer.lib.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Akdeniz on 02/03/2017.
 */

public class Characteristic {

    @SerializedName("uuid")
    private String uuuid;

    @SerializedName("name")
    private String name;

    @SerializedName("format")
    private String format;

    @SerializedName("access")
    private String access;


    public String getUuuid() {
        return uuuid;
    }

    public String getName() {
        return name;
    }

    public String getFormat() {
        return format;
    }

    public String getAccess() {
        return access;
    }

    @Override
    public String toString() {
        return "Characteristic{" +
                "uuuid='" + uuuid + '\'' +
                ", name='" + name + '\'' +
                ", format='" + format + '\'' +
                ", access='" + access + '\'' +
                '}';
    }
}
