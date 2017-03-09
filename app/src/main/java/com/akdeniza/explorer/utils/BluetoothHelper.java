package com.akdeniza.explorer.utils;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.ArrayList;
import java.util.List;

import static android.bluetooth.BluetoothAdapter.ACTION_STATE_CHANGED;

/**
 * Bluetooth helper class to check the bluetooth status
 * @author Akdeniz on 05/01/2017.
 */

public class BluetoothHelper {


    private static final IntentFilter STATE_CHANGED = new IntentFilter(ACTION_STATE_CHANGED);
    private BluetoothAdapter adapter;
    private final Context context;
    private boolean bluetoothEnabled;
    private List<Listener> listeners;

    public BluetoothHelper(Context context) {
        this.context = context.getApplicationContext();
        adapter = BluetoothAdapter.getDefaultAdapter();
        listeners = new ArrayList<>();
    }

    /**
     * To check if bluetooth is enabled
     * @return if bluetooth is enabled
     */
    public boolean isBluetoothEnabled() {
        return adapter.isEnabled();
    }

    public void enableBluetooth() {
        if (!adapter.isEnabled()) {
            adapter.enable();
        }
    }

    /**
     * Add listener to get informed of the change of the bluetooth state
     * @param listener
     */
    public void addListener(Listener listener) {
        context.registerReceiver(broadcastReceiver, STATE_CHANGED);

        if (listeners.size() == 0) {
            bluetoothEnabled = isBluetoothEnabled();
            context.registerReceiver(broadcastReceiver, STATE_CHANGED);
        }

        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /**
     * Remove the given listener from the listener list
     * @param listener
     */
    public void removeListener(Listener listener) {
        listeners.remove(listener);

        if (listeners.size() == 0) {
            context.unregisterReceiver(broadcastReceiver);
        }
    }

    /**
     * Checks the bluetooth current state and notifies all the listener
     */
    private void checkBluetoothStateAndNotifyListeners() {
        boolean state = isBluetoothEnabled();
        if (state != bluetoothEnabled) {
            bluetoothEnabled = state;
            for (int i = 0, size = listeners.size(); i < size; i++) {
                listeners.get(i).onBluetoothStatusChange(bluetoothEnabled);
            }
        }
    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            checkBluetoothStateAndNotifyListeners();
        }
    };

    /**
     * Listener interface to get notified on the change of the bluetooth state
     */
    public interface Listener {
        void onBluetoothStatusChange(boolean isEnabled);
    }
}
