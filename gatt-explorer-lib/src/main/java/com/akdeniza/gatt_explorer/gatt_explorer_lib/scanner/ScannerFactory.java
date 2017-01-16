package com.akdeniza.gatt_explorer.gatt_explorer_lib.scanner;

import android.os.Build;

/**
 * Created by Akdeniz on 05/01/2017.
 */

public class ScannerFactory {

    private ScannerFactory() {
    }

    /**
     * Returns a scanner depending on Android API level of the device.
     * For API 21 and higher it returns a scanner using the newer API BluetoothScanner.
     * Anything lower it returns a scanner using the older API BluetoothAdapter
     *
     * @return Scanner
     */
    public static Scanner getScanner() {
        if (Build.VERSION.SDK_INT < 21) {
            return new ScannerV0();
        } else {
            return new ScannerV21();
        }
    }
}
