package com.akdeniza.gatt_explorer.utils;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;

/**
 * Created by Akdeniz on 05/01/2017.
 */

public class BluetoothHelper {

    private BluetoothAdapter adapter;
    private final Context context;
    private boolean isBluetoothEnabled;

    public BluetoothHelper(Context context) {
        this.context = context.getApplicationContext();
        adapter = BluetoothAdapter.getDefaultAdapter();
    }

    public boolean isBluetoothEnabled() {
        return adapter.isEnabled();
    }

    public void enableBluetooth() {
        if (adapter.isEnabled()) {
            adapter.enable();
        }
    }
}
