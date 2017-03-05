package com.akdeniza.gatt_explorer.lib.scanner;

import android.bluetooth.BluetoothDevice;

/**
 * ScanListener interface for receiving scan results
 * Created by Akdeniz on 05/01/2017.
 */

public interface ScanListener {

    /**
     *
     * @param device that was found
     * @param rssi value of the found device
     * @param scanRecord advertisment record
     */
    void onData(BluetoothDevice device, int rssi, byte[] scanRecord);
}
