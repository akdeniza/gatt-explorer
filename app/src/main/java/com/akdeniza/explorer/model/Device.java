package com.akdeniza.explorer.model;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattService;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Akdeniz on 09/01/2017.
 */

public class Device implements Comparable {

    @Getter private String address;
    @Getter @Setter private int rssi;
    @Getter private BluetoothDevice bluetoothDevice;
    @Getter @Setter private long lastUpdate;
    List<BluetoothGattService> services = new ArrayList<>();

    public Device(String address, BluetoothDevice bluetoothDevice, int rssi) {
        this.address = address;
        this.bluetoothDevice = bluetoothDevice;
        this.rssi = rssi;
        this.lastUpdate = System.currentTimeMillis();
    }



    @Override
    public int compareTo(Object o) {
        if (o instanceof Device) {
            Device device = (Device) o;
            if (device.getRssi() > rssi) {
                return 1;
            } else if (device.getRssi() < rssi) {
                return -1;
            }
        }
        return 0;
    }
}
