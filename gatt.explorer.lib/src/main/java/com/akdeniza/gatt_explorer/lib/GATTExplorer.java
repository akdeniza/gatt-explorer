package com.akdeniza.gatt_explorer.lib;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.content.Context;

import com.akdeniza.gatt_explorer.lib.gatt.BtGATTHandler;
import com.akdeniza.gatt_explorer.lib.gatt.GATTListener;
import com.akdeniza.gatt_explorer.lib.scanner.ScanListener;
import com.akdeniza.gatt_explorer.lib.scanner.Scanner;
import com.akdeniza.gatt_explorer.lib.scanner.ScannerFactory;


/**
 * Exploring and scanning for Bluetooth Low energy devices.
 * <p>
 * Uses the database at www.github/akdeniza/gatt-explorer-database/data to parse
 * service and characteristic information.
 *
 * @author Akdeniz on 04/01/2017.
 */

public class GATTExplorer {

    private Scanner scanner;
    private Context context;

    /**
     * Constructor to create an object of GATTExplorer
     *
     * @param context
     */
    public GATTExplorer(Context context) {
        this.context = context;
    }


    /**
     * Starts a Bluetooth LE scan. Scan results are returned to ScanListener
     * @param listener that should receive  the scan results
     */
    public void startScan(ScanListener listener) {
        scanner = ScannerFactory.getScanner();
        scanner.setScanListener(listener);
        scanner.startScan();
    }

    /**
     * Stops ongoing Bluetooth LE scan.
     */
    public void stopScan() {
        scanner.stopScan();
        scanner = null;
    }


    /**
     * Discoveres the Generic Attribute Profile of the given Bluetooth Devices.
     * Uses the database at www.github/akdeniza/gatt-explorer-database/data to parse
     * service and characteristic information.
     * If no file is in the database for the specific device the raw data will be returned
     *
     * @param device       that should be discovered
     * @param GATTListener that should receive the service and characteristic information
     */
    public void discoverGATT(BluetoothDevice device, GATTListener GATTListener) {
        BtGATTHandler bluetoothGattCallback = new BtGATTHandler(GATTListener, context);
        BluetoothGatt bluetoothGatt = device.connectGatt(context, false, bluetoothGattCallback);
    }

}
