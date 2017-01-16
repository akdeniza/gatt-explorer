package com.akdeniza.gatt_explorer.utils;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

import com.akdeniza.gatt_explorer.gatt_explorer.R;

import static android.provider.Settings.Secure.LOCATION_MODE;
import static android.provider.Settings.Secure.LOCATION_MODE_OFF;

/**
 * Created by Akdeniz on 05/01/2017.
 */

public class LocationHelper {

    private static final boolean LOCATION_NEEDED = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    private static final String EXCEPTION_LOG_TAG = "LocationHelper";
    public static final String SHARED_PREF_NEVERASK = "never ask";
    public static final int PERMISSION_LOCATION_REQUEST_CODE = 1;
    private Context context;

    public LocationHelper(Context context) {
        this.context = context;
    }

    public boolean checkLocationPermission() {
        if (LOCATION_NEEDED) {
            return (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
        }
        return true;
    }

    public boolean isLocationNeeded() {
        return LOCATION_NEEDED;
    }

    public boolean checkIsLocationTurnedOn() {
        if (LOCATION_NEEDED) {
            try {
                return Settings.Secure.getInt(context.getContentResolver(), LOCATION_MODE) != LOCATION_MODE_OFF;
            } catch (Settings.SettingNotFoundException e) {
                Log.e(EXCEPTION_LOG_TAG, e.toString());
                return false;
            }
        }
        return true;
    }


    public void requestLocationPermission(View view, Activity activity) {
        if (!checkLocationPermission()) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
            boolean neverAskAgain = preferences.getBoolean(SHARED_PREF_NEVERASK, false);
            if (neverAskAgain) {
                showPermissionDeniedSnackbar(view, activity);
            } else {
                showPermissionExplanationDialog(activity);
            }
        }
    }

    private void showPermissionExplanationDialog(final Activity activity) {
        new AlertDialog.Builder(context)
                .setCancelable(true)
                .setTitle(context.getString(R.string.locaton_request_title))
                .setMessage(context.getString(R.string.location_request_message))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showPermissionDialog(activity);
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        showPermissionDialog(activity);
                    }
                })
                .show();
    }

    private void showPermissionDialog(Activity activity) {
        if (!checkLocationPermission()) {
            ActivityCompat.requestPermissions(
                    activity,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_LOCATION_REQUEST_CODE);
        }
    }

    public void openLocationEnableSetting(Activity activity) {
        try {
            activity.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        } catch (ActivityNotFoundException e) {
            Log.e(EXCEPTION_LOG_TAG, e.toString());
        }
    }

    public void openLocationPermissionSetting(Activity activity) {
        try {
            final Intent i = new Intent();
            i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            i.addCategory(Intent.CATEGORY_DEFAULT);
            i.setData(Uri.parse("package:" + context.getPackageName()));
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            context.startActivity(i);
        } catch (ActivityNotFoundException e) {
            Log.e(EXCEPTION_LOG_TAG, e.toString());
        }

    }

    public void showPermissionDeniedSnackbar(View view, final Activity activity) {
        Snackbar snackbar = Snackbar
                .make(view, R.string.location_snackbar_message, Snackbar.LENGTH_LONG)
                .setAction(R.string.location_snackbar_settings, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openLocationPermissionSetting(activity);
                    }
                });
        snackbar.show();
    }

}
