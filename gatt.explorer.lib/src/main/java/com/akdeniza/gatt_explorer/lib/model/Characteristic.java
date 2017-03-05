package com.akdeniza.gatt_explorer.lib.model;

import android.bluetooth.BluetoothGattCharacteristic;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

/**
 * Created by Akdeniz on 02/03/2017.
 */

public class Characteristic {

    @SerializedName("uuid")
    private UUID uuuid;

    @SerializedName("name")
    private String name;

    @SerializedName("format")
    private String format;

    @SerializedName("access")
    private String access;

    private byte[] value;

    public Characteristic(BluetoothGattCharacteristic characteristic) {
        this.uuuid = characteristic.getUuid();
        this.name = null;
        this.format = null;
        this.access = null;
        this.value = characteristic.getValue();

    }

    public UUID getUuuid() {
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

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Characteristic{" +
                "uuuid='" + uuuid + '\'' +
                ", name='" + name + '\'' +
                ", format='" + format + '\'' +
                ", access='" + access + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
