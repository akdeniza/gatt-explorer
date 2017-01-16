package com.akdeniza.gatt_explorer.gatt_explorer_lib.scanner;

/**
 * Created by Akdeniz on 05/01/2017.
 */

public interface Scanner {

    void startScan();

    void stopScan();

    void setScanListener(ScanListener listener);
}
