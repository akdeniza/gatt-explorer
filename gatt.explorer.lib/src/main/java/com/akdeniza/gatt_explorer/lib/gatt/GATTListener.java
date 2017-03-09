package com.akdeniza.gatt_explorer.lib.gatt;

import java.util.List;

/**
 *  The interface to define to listener that should receive the GATT result
 *  @author Akdeniz on 14/02/2017.
 */

public interface GATTListener {

    /**
     * The results of the GATT discovery
     * @param gattObjects list of the GATT objects
     */
    void onData(List<Object> gattObjects);
}
