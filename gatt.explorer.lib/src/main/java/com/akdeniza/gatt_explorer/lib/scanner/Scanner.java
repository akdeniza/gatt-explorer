package com.akdeniza.gatt_explorer.lib.scanner;


/**
 * Bluetooth LE scanner interface
 * @author Akdeniz
 */
public interface Scanner {

    /**
     * Starts the Bluetooth LE Scan
     */
    void startScan();

    /**
     * Stops any ongoing Bluetooth LE Scan
     */
    void stopScan();

    /**
     * Sets the ScanListener that should receive the scan results
     * @param listener
     */
    void setScanListener(ScanListener listener);
}
