package com.akdeniza.gatt_explorer.lib.gatt;

import android.app.Activity;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;

import com.akdeniza.gatt_explorer.lib.REST.GitHubClient;
import com.akdeniza.gatt_explorer.lib.REST.GitHubInterface;
import com.akdeniza.gatt_explorer.lib.model.Characteristic;
import com.akdeniza.gatt_explorer.lib.model.GitHubReponse;
import com.akdeniza.gatt_explorer.lib.model.Service;
import com.akdeniza.gatt_explorer.lib.parser.CharacteristicParserHandler;
import com.akdeniza.gatt_explorer.lib.util.ConnectionHelper;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.bluetooth.BluetoothGatt.GATT_SUCCESS;

/**
 * Created by Akdeniz on 14/02/2017.
 */

public class BtGATTHandler extends BluetoothGattCallback {

    private int counter = 0;
    private List<Object> gattObjects = new ArrayList<>();
    private List<Object> databaseReponse = new ArrayList<>();
    private GATTListener GATTListener;
    private List<BluetoothGattCharacteristic> characteristics;
    private BluetoothGatt bluetoothGatt;
    private String serviceAndCharacteristicUiids = "";
    private Context context;
    private Boolean firstTimeDisconnect;


    /**
     * @param GATTListener
     * @param context
     */
    public BtGATTHandler(GATTListener GATTListener, Context context) {
        this.GATTListener = GATTListener;
        this.context = context;
        this.firstTimeDisconnect = true;
    }

    /**
     * @param listener
     */
    public void setGATTListener(GATTListener listener) {
        this.GATTListener = listener;

    }

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        super.onConnectionStateChange(gatt, status, newState);
        this.bluetoothGatt = gatt;
        if (newState == BluetoothProfile.STATE_CONNECTED) {
            firstTimeDisconnect = false;
            Logger.d("Connected to GATT server");
            Logger.d("Attempting to start discovering services");

            gatt.discoverServices();
        } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
            Logger.d("Disonnected from GATT server");
            if (firstTimeDisconnect) {
                Logger.d("Can't connect to device");
                ((Activity) (context)).finish();
            }
        }
    }


    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        super.onServicesDiscovered(gatt, status);

        if (status == GATT_SUCCESS) {

            List<BluetoothGattService> services = gatt.getServices();

            for (BluetoothGattService service : services) {
                gattObjects.add(service);
                characteristics = service.getCharacteristics();
                serviceAndCharacteristicUiids = serviceAndCharacteristicUiids + service.getUuid().toString();
                for (BluetoothGattCharacteristic characteristic : characteristics) {
                    gattObjects.add(characteristic);

                    serviceAndCharacteristicUiids = serviceAndCharacteristicUiids + characteristic.getUuid().toString();
                }
                Logger.d("GATT Service: " + service.getUuid());
            }

            Logger.d("Hash: " + serviceAndCharacteristicUiids.hashCode() + " from: " + serviceAndCharacteristicUiids);
            requestGATTFromDatabase("" + serviceAndCharacteristicUiids.hashCode());
        } else {
            //TODO: Show error
        }

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
        btCharacter.getPermissions();

        //Skip any other object than BluetoothGattCharacteristic
        if (gattObjects.get(counter - 1) instanceof BluetoothGattCharacteristic) {
            BluetoothGattCharacteristic bluetoothCharacteristic = (BluetoothGattCharacteristic) gattObjects.get(counter - 1);
            if (btCharacter.getUuid().equals(bluetoothCharacteristic.getUuid())) {
                ((BluetoothGattCharacteristic) gattObjects.get(counter - 1)).setValue(btCharacter.getValue());
                if (!databaseReponse.isEmpty()) {
                    ((Characteristic) databaseReponse.get(counter - 1)).setValueInByte(btCharacter.getValue());
                }
            }
        }
        requestCharateristicsValues();

    }

    private void requestCharateristicsValues() {
        if (counter < gattObjects.size()) {
            counter++;

            //Making sure to read only characteristics and skip the services in the object list
            if (gattObjects.get(counter - 1) instanceof BluetoothGattCharacteristic) {
                BluetoothGattCharacteristic bluetoothGattCharacteristic = (BluetoothGattCharacteristic) gattObjects.get(counter - 1);

                //Only read if the characteristic is readable otherwise skip the characteristic
                if (bluetoothGattCharacteristic.getProperties() == 2 || bluetoothGattCharacteristic.getProperties() == 10) {
                    bluetoothGatt.readCharacteristic(bluetoothGattCharacteristic);
                } else {
                    requestCharateristicsValues();
                }

            } else {
                requestCharateristicsValues();
            }

        } else {
            parseCharacteristicIfNeededInfoIsAvailable();
        }

    }

    private void requestGATTFromDatabase(String hash) {
        ConnectionHelper connectionHelper = new ConnectionHelper();
        if (connectionHelper.isConnectedToInternet(context)) {
            GitHubInterface api = GitHubClient.getGitHubClient().create(GitHubInterface.class);

            Call<GitHubReponse> call = api.getGATTJsonFromHash(hash);
            call.enqueue(new Callback<GitHubReponse>() {
                @Override
                public void onResponse(Call<GitHubReponse> call, Response<GitHubReponse> response) {
                    Logger.d("Api request successful");

                    //if the requested data exists use the data to fll the databaseResponse
                    if (response.body() != null) {

                        List<Service> services = response.body().getServices();
                        for (Service service : services) {
                            List<Characteristic> characteristics = service.getCharacteristics();
                            databaseReponse.add(service);
                            for (Characteristic characteristic : characteristics) {
                                databaseReponse.add(characteristic);
                            }
                        }
                    }

                    requestCharateristicsValues();

                }

                @Override
                public void onFailure(Call<GitHubReponse> call, Throwable t) {
                    Logger.d("Api request failed: " + t.toString());
                    requestCharateristicsValues();
                }
            });

        } else {
            requestCharateristicsValues();
        }
    }

    /**
     * Parses all the objects of the class BluetoothGattService and BluetoothGattCharacteristic
     * to com.akdeniza.gatt_explorer.lib.model.Service and com.akdeniza.gatt_explorer.lib.model.Characteristic
     *
     * @param gattObjects the list that should be parsed
     * @return returns the list with objects from the classes service and characteristic
     */
    private List<Object> fromGattObjectToLibraryModel(List<Object> gattObjects) {
        if (gattObjects.get(0) instanceof BluetoothGattService) {
            List<Object> newList = new ArrayList<>();
            for (int i = 0; i < gattObjects.size(); i++) {
                Object o = gattObjects.get(i);
                if (gattObjects.get(i) instanceof BluetoothGattService) {

                    newList.add(new Service((BluetoothGattService) gattObjects.get(i)));

                } else if (gattObjects.get(i) instanceof BluetoothGattCharacteristic) {
                    newList.add(new Characteristic((BluetoothGattCharacteristic) gattObjects.get(i)));
                }
            }
            return newList;
        } else {
            return gattObjects;
        }
    }

    private void parseCharacteristicIfNeededInfoIsAvailable() {

        CharacteristicParserHandler characteristicParserHandler = new CharacteristicParserHandler();

        bluetoothGatt.disconnect();

        //If no data from the database was obtainable then pass the raw data
        if (databaseReponse.isEmpty()) {
            GATTListener.onData(fromGattObjectToLibraryModel(gattObjects));
        } else {
            databaseReponse = characteristicParserHandler.parseAllCharacteristics(databaseReponse);
            GATTListener.onData(databaseReponse);
        }

    }
}
