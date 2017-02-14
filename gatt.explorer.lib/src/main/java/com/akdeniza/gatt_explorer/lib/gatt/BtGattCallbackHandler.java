package com.akdeniza.gatt_explorer.lib.gatt;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akdeniz on 14/02/2017.
 */

public class BtGattCallbackHandler extends BluetoothGattCallback {

    private int counter = 0;
    private List<Object> gattObjects = new ArrayList<>();
    private GattListener gattListener;
    private List<BluetoothGattCharacteristic> characteristics;
    private BluetoothGatt bluetoothGatt;


    public BtGattCallbackHandler(GattListener gattListener) {
        this.gattListener = gattListener;
    }

    public void setGattListener(GattListener listener) {
        this.gattListener = listener;

    }

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        super.onConnectionStateChange(gatt, status, newState);
        this.bluetoothGatt = gatt;
        if (newState == BluetoothProfile.STATE_CONNECTED) {
            Logger.d("Connected to GATT server");
            Logger.d("Attempting to start discovering services");

            gatt.discoverServices();
        } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
            Logger.d("Disonnected from GATT server");
        }
    }


    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        super.onServicesDiscovered(gatt, status);

        List<BluetoothGattService> services = gatt.getServices();

        for (BluetoothGattService service : services) {
            gattObjects.add(service);
            characteristics = service.getCharacteristics();
            for (BluetoothGattCharacteristic characteristic : characteristics) {
                gattObjects.add(characteristic);
            }
        }

        //adapter.setGattAdapterObjectList(gattObjects);
        requestCharatericsValues();

    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic
            characteristic) {
        super.onCharacteristicChanged(gatt, characteristic);
    }

    @Override
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic
            btCharacter, int status) {
        super.onCharacteristicRead(gatt, btCharacter, status);
        if (gattObjects.get(counter - 1) instanceof BluetoothGattCharacteristic) {
            BluetoothGattCharacteristic bluetoothCharacteristic = (BluetoothGattCharacteristic) gattObjects.get(counter - 1);
            if (btCharacter.getUuid().equals(bluetoothCharacteristic.getUuid())) {
                ((BluetoothGattCharacteristic) gattObjects.get(counter - 1)).setValue(btCharacter.getValue());
            }
        }
        Logger.d("onCharacteristicRead, Status: " + status);
        requestCharatericsValues();

    }

    private void requestCharatericsValues() {
        if (counter < gattObjects.size()) {
            counter++;
            if (gattObjects.get(counter - 1) instanceof BluetoothGattCharacteristic) {
                BluetoothGattCharacteristic bluetoothGattCharacteristic = (BluetoothGattCharacteristic) gattObjects.get(counter - 1);
                bluetoothGattCharacteristic.getPermissions();
                bluetoothGattCharacteristic.getWriteType();
                //TODO: Use access value from JSON
                if ((counter - 1) != 17) {
                    bluetoothGatt.readCharacteristic(bluetoothGattCharacteristic);
                } else {
                    requestCharatericsValues();
                }

            } else {
                requestCharatericsValues();
            }

        } else {
            bluetoothGatt.disconnect();
            gattListener.onData(gattObjects);

        }

    }
}
