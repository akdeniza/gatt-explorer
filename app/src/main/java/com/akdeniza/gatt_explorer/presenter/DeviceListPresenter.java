package com.akdeniza.gatt_explorer.presenter;

import android.bluetooth.BluetoothDevice;
import android.os.Handler;
import android.os.Looper;

import com.akdeniza.gatt_explorer.gatt_explorer_lib.scanner.ScanListener;
import com.akdeniza.gatt_explorer.model.Device;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Akdeniz on 09/01/2017.
 */

public class DeviceListPresenter implements ScanListener {

    private static final int DEVICE_DELAYED_CLEAR_TIME = 650;
    private static final int DEVICE_LIST_UPDATE_TIME = 1250;
    private static final int SCANNER_UPDATE_BUFFER = 5000;
    private static final int DEVICE_EXIT_TIMER = 10000;

    private List<Device> devices = new ArrayList<>();
    private Handler handler = new Handler(Looper.getMainLooper());
    private boolean updateUI = false;
    private long lastUpdate;
    private DataChangedListener listener;

    public void onStart(DataChangedListener listener) {
        this.listener = listener;
    }

    public void onStop() {
        handler.removeCallbacks(delayedCleanListRunnable);
        this.listener = null;
    }


    /**
     * Calls a runnable to run if not already the code on the main thread.
     * Reason for this is on Android 4 the bluetooth scanner runs sometimes on non-main threads
     * and therefore the recyclerview can't be updated without going back to this.
     */
    @Override
    public void onData(BluetoothDevice device, int rssi, byte[] scanRecord) {
        new MainRunnable(device, rssi, scanRecord);
    }

    private void onDataMainThread(BluetoothDevice device, int rssi, byte[] scanRecord) {
        String address = device.getAddress();
        int pos = checkIfDeviceIsAlreadyInList(address);

        if (pos != -1) {
            updateDeviceValues(pos, rssi);
        } else {
            Device dev = new Device(address, device, rssi);
            addDeviceToList(dev);
        }
    }

    /**
     * Checks if the device with the given address already is added in the list.
     *
     * @param address the address of the device
     * @return Returns the position if given device is in list otherwise returns a -1.
     */
    private int checkIfDeviceIsAlreadyInList(String address) {
        for (int i = 0; i < devices.size(); i++) {
            if (devices.get(i).getAddress().equals(address)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Updates the values of the given device using the position.
     *
     * @param position of the device in the devices list
     * @param rssi     of the received signal
     */
    private void updateDeviceValues(int position, int rssi) {
        long now = System.currentTimeMillis();
        Device device = devices.get(position);
        //TODO: if(device != null)
        if (now - device.getLastUpdate() > SCANNER_UPDATE_BUFFER) {
            device.setRssi(rssi);
            device.setLastUpdate(now);
            updateList();
        }
    }

    private void addDeviceToList(Device device) {
        devices.add(device);
        updateList();
    }

    private void clearList() {
        boolean didAnyChange = false;
        long now = System.currentTimeMillis();

        List<Device> found = new ArrayList<>();
        for (Device dev : devices) {
            if (now - dev.getLastUpdate() > DEVICE_EXIT_TIMER) {
                found.add(dev);
                didAnyChange = true;
            }
        }
        if (didAnyChange) {
            devices.removeAll(found);
        }
    }

    /**
     * Updates the list after DEVICE_LIST_UPDATE_TIME ms has passed and
     * removes all devices with a too high lastUpdate value.
     */
    private void updateList() {
        long now = System.currentTimeMillis();
        if (now - lastUpdate > DEVICE_LIST_UPDATE_TIME) {
            lastUpdate = now;
            if (listener != null && updateUI) {
                clearList();
                Collections.sort(devices);
                listener.onDevicesChanged(devices);
            }
        }
    }

    public void removeAllFromList() {
        handler.postDelayed(delayedCleanListRunnable, DEVICE_DELAYED_CLEAR_TIME);
    }

    public void setUIUpdateState(Boolean state) {
        this.updateUI = state;
    }

    private Runnable delayedCleanListRunnable = new Runnable() {
        @Override
        public void run() {
            devices.clear();
            if (listener != null) {
                listener.onDevicesChanged(devices);
            }
        }
    };

    public interface DataChangedListener {

        void onDevicesChanged(List<Device> devices);
    }

    private final class MainRunnable implements Runnable {

        private BluetoothDevice device;
        private int rssi;
        private byte[] scanRecord;

        private MainRunnable(BluetoothDevice device, int rssi, byte[] scanRecord) {
            this.device = device;
            this.rssi = rssi;
            this.scanRecord = scanRecord;


            if (Looper.myLooper() == Looper.getMainLooper()) {
                run();
            } else {
                handler.post(this);
            }

        }

        @Override
        public void run() {
            onDataMainThread(device, rssi, scanRecord);
        }
    }
}
