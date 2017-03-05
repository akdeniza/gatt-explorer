package com.akdeniza.gatt_explorer.lib.scanner;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Bluetooth scanner class for devices until API 21
 * @author Akdeniz on 05/01/2017.
 */

public class ScannerV0 implements Scanner, BluetoothAdapter.LeScanCallback {

    private BluetoothAdapter adapter;
    private static final String EXCEPTION_LOG_TAG = "ScannerV0";
    private ScanListener listener;


    public ScannerV0() {
        this.adapter = BluetoothAdapter.getDefaultAdapter();
    }

    /**
     * Starts Bluetooth LE scan
     */
    @Override
    public void startScan() {
        adapter.startLeScan(this);
    }

    /**
     * Stops ongoing Bluetooth LE scan
     */
    @Override
    public void stopScan() {
        adapter.stopLeScan(this);
    }

    @Override @NonNull
    public void setScanListener(ScanListener listener) {
        this.listener = listener;
    }

    /**
     * Callback reporting scan results to the ScanListener
     * @param bluetoothDevice that was found
     * @param i rssi value
     * @param bytes advertisment record
     */
    @Override
    public void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] bytes) {
        try {
            listener.onData(bluetoothDevice, i, bytes);
        } catch (NullPointerException e) {
            Log.e(EXCEPTION_LOG_TAG, e.toString());
        }

    }
}
