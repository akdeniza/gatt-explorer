package com.akdeniza.gatt_explorer.lib.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Akdeniz on 02/03/2017.
 */

public class GitHubReponse {

    @SerializedName("gatthash")
    private String gatthash;

    @SerializedName("services")
    private List<Service> services;


    public List<Service> getServices() {
        return services;
    }

    public String getGattHash() {
        return gatthash;
    }
}
