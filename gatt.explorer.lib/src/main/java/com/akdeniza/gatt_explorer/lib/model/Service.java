package com.akdeniza.gatt_explorer.lib.model;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *  GATT Service
 *  @author Akdeniz on 02/03/2017.
 */

public class Service {

    @SerializedName("uuid")
    private UUID uuuid;

    @SerializedName("name")
    private String name;

    @SerializedName("characteristics")
    private List<Characteristic> characteristics;

    /**
     * Constructor to create an service object
     * @param uuid of the service
     * @param name of the service
     * @param characteristics of the service
     */
    public Service(UUID uuid, String name, List<Characteristic> characteristics) {
        this.uuuid = uuid;
        this.name = name;
        this.characteristics = characteristics;
    }

    /**
     * Constructor to create an service object
     * @param service to be used
     */
    public Service(BluetoothGattService service) {
        this.uuuid = service.getUuid();
        this.name = null;
        this.characteristics = toCharacteristic(service.getCharacteristics());
    }

    /**
     * Parses all the characteristics from the class BluetoothGattCharacteristic into  com.akdeniza.gatt_explorer.lib.model.characteristic
     * @param bluetoothGattCharacteristics list to be parsed
     * @return
     */
    public List<Characteristic> toCharacteristic(List<BluetoothGattCharacteristic> bluetoothGattCharacteristics) {
        List<Characteristic> characteristics = new ArrayList<>();
        for (BluetoothGattCharacteristic chara : bluetoothGattCharacteristics) {
            characteristics.add(new Characteristic(chara));
        }
        return characteristics;
    }

    /**
     * Getter for the UUID
     * @return the UUID
     */
    public UUID getUuuid() {
        return uuuid;
    }

    /**
     * Getter for the Name
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the characteristics
     * @return the characteristics
     */
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
