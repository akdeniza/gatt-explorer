package com.akdeniza.gatt_explorer.lib.gatt;

import java.util.List;

/**
 * Created by Akdeniz on 14/02/2017.
 */

public interface GattListener {

    void onData(List<Object> gattObjects);
}
