package com.akdeniza.gatt_explorer.lib.model;

import android.bluetooth.BluetoothGattCharacteristic;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

/**
 * GATT characteristic
 * @author Akdeniz on 02/03/2017.
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

    private byte[] valueInByte;

    private String value;

    /**
     * Constructor
     * @param characteristic
     */
    public Characteristic(BluetoothGattCharacteristic characteristic) {
        this.uuuid = characteristic.getUuid();
        this.name = null;
        this.format = null;
        this.access = null;
        this.valueInByte = characteristic.getValue();
        this.value = characteristic.getValue() != null ? characteristic.getValue().toString() : null;

    }

    public void setValueInByte(byte[] valueInByte) {
        this.valueInByte = valueInByte;
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

    public byte[] getValueInByte() {
        return valueInByte;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Characteristic{" +
                "uuuid='" + uuuid + '\'' +
                ", name='" + name + '\'' +
                ", format='" + format + '\'' +
                ", access='" + access + '\'' +
                ", valueInByte='" + valueInByte + '\'' +
                '}';
    }
}
