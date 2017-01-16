package com.akdeniza.gatt_explorer.gatt_explorer_lib.scanner;

import android.bluetooth.BluetoothDevice;

/**
 * Created by Akdeniz on 05/01/2017.
 */

public interface ScanListener {

    void onData(BluetoothDevice device, int rssi, byte[] scanRecord);
}
