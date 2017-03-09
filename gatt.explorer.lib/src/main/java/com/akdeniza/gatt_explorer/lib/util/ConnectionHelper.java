package com.akdeniza.gatt_explorer.lib.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 *  Connection helper checking if the device has a connection to the internet
 *  @author Akdeniz on 02/03/2017.
 */

public class ConnectionHelper {

    /**
     * Checks if the devices is connected to the internet
     * @param context
     * @return boolean
     */
    public boolean isConnectedToInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null){
            return networkInfo.isConnectedOrConnecting();
        }
        return false;
    }
}
