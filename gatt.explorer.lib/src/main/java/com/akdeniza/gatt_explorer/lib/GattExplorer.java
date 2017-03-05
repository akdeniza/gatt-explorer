package com.akdeniza.gatt_explorer.lib;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.content.Context;

import com.akdeniza.gatt_explorer.lib.gatt.BtGattHandler;
import com.akdeniza.gatt_explorer.lib.gatt.GattListener;
import com.akdeniza.gatt_explorer.lib.scanner.ScanListener;
import com.akdeniza.gatt_explorer.lib.scanner.Scanner;
import com.akdeniza.gatt_explorer.lib.scanner.ScannerFactory;


/**
 * Exploring and scanning for Bluetooth Low energy devices.
 *
 * Uses the database at www.github/akdeniza/gatt-explorer-database/data to parse
 * service and characteristic information.
 *
 * @author Akdeniz on 04/01/2017.
 */

public class GattExplorer {

    private Scanner scanner;
    private Context context;

    /**
     * Constructor to create an object of GattExplorer
     * @param context
     */
    public GattExplorer(Context context) {
        this.context = context.getApplicationContext();
        this.scanner = ScannerFactory.getScanner();
    }


    public void onStart() {
        scanner = ScannerFactory.getScanner();

    }

    public void onStop() {

    }

    /**
     * Starts a Bluetooth LE scan. Scan results are returned to ScanListener
     */
    public void startScan() {
        scanner.startScan();
    }

    /**
     * Stops ongoing Bluetooth LE scan.
     */
    public void stopScan() {
        scanner.stopScan();
    }

    /**
     * Sets the ScanListener that receives the scan results
     * @param listener receives scan results
     */
    public void setScanResultListener(ScanListener listener) {
        scanner.setScanListener(listener);
    }

    //region "explorer part"

    /**
     * Discoveres the Generic Attribute Profile of the given Bluetooth Devices.
     * Uses the database at www.github/akdeniza/gatt-explorer-database/data to parse
     * service and characteristic information.
     * If no file is in the database for the specific device the raw data will be returned
     * @param device that should be discovered
     * @param gattListener that should receive the service and characteristic information
     */
    public void discoverGatt(BluetoothDevice device, GattListener gattListener) {
        BtGattHandler bluetoothGattCallback = new BtGattHandler(gattListener, context);
        BluetoothGatt bluetoothGatt = device.connectGatt(context, false, bluetoothGattCallback);
    }

    public void connectToDevice() {

    }

    public void disconnectFromDevice() {

    }

    public void checkIfJsonIsAvailable() {

    }

    public void localBackup() {

    }

    public void discoverDevice() {

    }
    //endregion
}
