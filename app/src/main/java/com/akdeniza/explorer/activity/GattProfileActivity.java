package com.akdeniza.explorer.activity;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.akdeniza.explorer.adapter.GattObjectAdapter;
import com.akdeniza.explorer.model.Device;
import com.akdeniza.explorer.utils.RecyclerViewLine;
import com.akdeniza.gatt_explorer.gatt_explorer.R;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GattProfileActivity extends AppCompatActivity {

    public static final String INTENT_DEVICE_KEY = "leDevice";

    private BluetoothDevice leDevice;
    private BluetoothGatt bluetoothGatt;
    private BluetoothGattCallback bluetoothGattCallback;
    private List<Object> gattObjects = new ArrayList<>();
    private List<BluetoothGattCharacteristic> characteristics;
    private GattObjectAdapter adapter;

    @BindView(R.id.gattRecyclerView)
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gatt_profile);
        ButterKnife.bind(this);

        //Get Device from Intent
        Intent i = getIntent();
        leDevice = i.getParcelableExtra(INTENT_DEVICE_KEY);
        Device device = new Device(leDevice.getAddress(), leDevice, 0);

        //Recyclerview and Adapter
        adapter = new GattObjectAdapter();
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new RecyclerViewLine(this, R.drawable.divider));
        recyclerView.setAdapter(adapter);

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

        private int counter = 0;


        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Logger.d("Connected to GATT server");
                Logger.d("Attempting to start discovering services");

                bluetoothGatt.discoverServices();
                //gatt.
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
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic btCharacter, int status) {
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
                adapter.setGattAdapterObjectList(gattObjects);
            }

        }
    }
}
