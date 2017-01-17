package com.akdeniza.gatt_explorer.activity;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.akdeniza.gatt_explorer.gatt_explorer.R;
import com.akdeniza.gatt_explorer.model.Device;
import com.orhanobut.logger.Logger;

import java.util.List;

public class GattProfileActivity extends AppCompatActivity {

    public static final String INTENT_DEVICE_KEY = "leDevice";

    private BluetoothDevice leDevice;
    private BluetoothGatt bluetoothGatt;
    private BluetoothGattCallback bluetoothGattCallback;
    private List<BluetoothGattService> services;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gatt_profile);

        Intent i = getIntent();
        leDevice = i.getParcelableExtra(INTENT_DEVICE_KEY);

        Device device = new Device(leDevice.getAddress(), leDevice, 0);


        bluetoothGattCallback = new BtGattCallback();
        bluetoothGatt = leDevice.connectGatt(this, false, bluetoothGattCallback);


    }

    @Override
    protected void onStart() {
        super.onStart();
//        bluetoothGatt.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //TODO: Return to scanner activity?
//        bluetoothGatt.disconnect();

    }

    private class BtGattCallback extends BluetoothGattCallback {

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            Logger.d("onCharacteristicRead, Status: " + status);

        }

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Logger.d("Connected to GATT server");
                Logger.d("Attempting to start discovering services");
                bluetoothGatt.discoverServices();
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Logger.d("Disonnected from GATT server");
            }
        }


        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);

            services = gatt.getServices();
            

//            for (BluetoothGattService service : services) {
//                Logger.d("Discovered: " + service.getUuid() + "with " + service.getCharacteristics());
//                if (service.getUuid().toString().contains("fff0")) {
//                    String charactersticUiids = "";
//                    for (BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {
//                        charactersticUiids = charactersticUiids + "    " + characteristic.getUuid();
//                    }
//                    Logger.d(charactersticUiids);
//                }
//            }
//            Logger.d("onServicesDiscovered");
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);
            Logger.d("onDescriptorRead");
        }
    }
}
