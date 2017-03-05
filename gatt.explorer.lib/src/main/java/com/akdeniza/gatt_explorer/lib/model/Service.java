package com.akdeniza.gatt_explorer.lib.model;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Akdeniz on 02/03/2017.
 */

public class Service {

    @SerializedName("uuid")
    private UUID uuuid;

    @SerializedName("name")
    private String name;

    @SerializedName("characteristics")
    private List<Characteristic> characteristics;

    public Service(UUID uuid, String name, List<Characteristic> characteristics) {
        this.uuuid = uuid;
        this.name = name;
        this.characteristics = characteristics;
    }

    public Service(BluetoothGattService service) {
        this.uuuid = service.getUuid();
        this.name = null;
        this.characteristics = toCharacteristic(service.getCharacteristics());
    }

    public List<Characteristic> toCharacteristic(List<BluetoothGattCharacteristic> bluetoothGattCharacteristics) {
        List<Characteristic> characteristics = new ArrayList<>();
        for (BluetoothGattCharacteristic chara : bluetoothGattCharacteristics) {
            characteristics.add(new Characteristic(chara));
        }
        return characteristics;
    }

    public UUID getUuuid() {
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
