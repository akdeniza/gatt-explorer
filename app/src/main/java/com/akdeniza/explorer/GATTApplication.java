package com.akdeniza.explorer;

import android.app.Application;

import com.akdeniza.gatt_explorer.gatt_explorer.BuildConfig;
import com.orhanobut.logger.Logger;

/**
 * Created by Akdeniz on 17/01/2017.
 */

public class GATTApplication extends Application {

    private static final String LOGGER_TAG = "GATTApplication";

    /**
     * Only add logging in debug mode
     */
    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Logger.init(LOGGER_TAG);
        }

    }
}
