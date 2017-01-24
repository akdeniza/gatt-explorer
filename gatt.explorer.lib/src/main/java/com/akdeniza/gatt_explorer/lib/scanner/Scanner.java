package com.akdeniza.gatt_explorer.lib.scanner;

/**
 * Created by Akdeniz on 05/01/2017.
 */

public interface Scanner {

    void startScan();

    void stopScan();

    void setScanListener(ScanListener listener);
}
