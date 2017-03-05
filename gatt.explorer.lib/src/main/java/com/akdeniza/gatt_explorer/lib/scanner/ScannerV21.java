package com.akdeniza.gatt_explorer.lib.scanner;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.os.Build;
import android.util.Log;

import static android.bluetooth.le.ScanSettings.SCAN_MODE_BALANCED;

/**
 * Bluetooth scanner class for devices above API 21
 * @author Akdeniz on 05/01/2017.
 */


@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class ScannerV21 implements Scanner {

    private static final String EXCEPTION_LOG_TAG = "ScannerV21";
    private BluetoothAdapter adapter;
    private BluetoothLeScanner bluetoothLeScanner;
    private ScanListener listener;
    private ScanSettings scanSettings;

    public ScannerV21() {
        this.adapter = BluetoothAdapter.getDefaultAdapter();
        this.bluetoothLeScanner = adapter.getBluetoothLeScanner();
        scanSettings = new ScanSettings.Builder().setScanMode(SCAN_MODE_BALANCED).build();
    }

    /**
     * Starts Bluetooth LE scan
     * @throws NullPointerException when bluetooth is disabled
     */
    @Override
    public void startScan() throws NullPointerException {
        try {
            bluetoothLeScanner.startScan(null, scanSettings, scanCallback);
        } catch (NullPointerException e) {
            Log.e(EXCEPTION_LOG_TAG, e.toString());
        }
    }

    /**
     * Stops ongoing Bluetooth LE scans
     */
    @Override
    public void stopScan() {
        try {
            bluetoothLeScanner.stopScan(scanCallback);
        } catch (NullPointerException e) {
            Log.e(EXCEPTION_LOG_TAG, e.toString());
        }
    }

    /**
     * Sets the scan result listener
     * @param listener that should receive scan results
     */
    @Override
    public void setScanListener(ScanListener listener) {
        this.listener = listener;

    }

    private final ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            try {
                listener.onData(result.getDevice(), result.getRssi(), result.getScanRecord().getBytes());
            } catch (NullPointerException e) {
                Log.e(EXCEPTION_LOG_TAG, e.toString());
            }

        }
    };
}
