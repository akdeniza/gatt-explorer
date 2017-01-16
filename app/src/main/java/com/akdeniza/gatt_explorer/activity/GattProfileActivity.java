package com.akdeniza.gatt_explorer.activity;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.akdeniza.gatt_explorer.gatt_explorer.R;

public class GattProfileActivity extends AppCompatActivity {

    public static final String INTENT_DEVICE_KEY = "leDevice";

    private BluetoothDevice leDevice;
    private BluetoothGatt bluetoothGatt;
    private BluetoothGattCallback bluetoothGattCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gatt_profile);

        Intent i = getIntent();
        leDevice = i.getParcelableExtra(INTENT_DEVICE_KEY);
        Log.d("z1", "Device: " + leDevice.getAddress());

        bluetoothGattCallback = new BtGattCallback();
        bluetoothGatt = leDevice.connectGatt(this, false, bluetoothGattCallback);

    }


    private class BtGattCallback extends BluetoothGattCallback {

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
        }

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);
        }
    }
}
