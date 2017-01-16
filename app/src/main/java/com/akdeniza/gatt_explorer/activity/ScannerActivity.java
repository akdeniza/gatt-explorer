package com.akdeniza.gatt_explorer.activity;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.akdeniza.gatt_explorer.adapter.BluetoothDeviceAdapter;
import com.akdeniza.gatt_explorer.gatt_explorer.R;
import com.akdeniza.gatt_explorer.gatt_explorer_lib.GattExplorer;
import com.akdeniza.gatt_explorer.handler.EmptyViewHandler;
import com.akdeniza.gatt_explorer.presenter.DeviceListPresenter;
import com.akdeniza.gatt_explorer.utils.BluetoothHelper;
import com.akdeniza.gatt_explorer.utils.LocationHelper;
import com.akdeniza.gatt_explorer.utils.RecyclerViewLine;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.akdeniza.gatt_explorer.utils.LocationHelper.PERMISSION_LOCATION_REQUEST_CODE;
import static com.akdeniza.gatt_explorer.utils.LocationHelper.SHARED_PREF_NEVERASK;

public class ScannerActivity extends AppCompatActivity implements
        FloatingActionButton.OnClickListener, BluetoothHelper.Listener, LocationHelper.Listener {

    private GattExplorer gattExplorer;
    private BluetoothHelper bluetoothHelper;
    private LocationHelper locationHelper;
    private boolean scannerOnPlay = false;
    private BluetoothDeviceAdapter adapter;
    private DeviceListPresenter deviceListPresenter;
    private EmptyViewHandler emptyViewHandler;

    @BindView(R.id.playPauseButton)
    FloatingActionButton playPauseButton;

    @BindView(R.id.deviceRecycler)
    RecyclerView recyclerView;

    @BindView(R.id.emptyRecyclerView)
    View emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        ButterKnife.bind(this);

        gattExplorer = new GattExplorer();
        locationHelper = new LocationHelper(this);
        bluetoothHelper = new BluetoothHelper(this);
        playPauseButton.setOnClickListener(this);


        deviceListPresenter = new DeviceListPresenter();

        //Recyclerview and Adapter
        adapter = new BluetoothDeviceAdapter();
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new RecyclerViewLine(this, R.drawable.divider));
        recyclerView.setAdapter(adapter);

        emptyViewHandler = new EmptyViewHandler(recyclerView, emptyView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        bluetoothHelper.addListener(this);
        locationHelper.addListener(this);
        setEmptyView();
        gattExplorer.setScanResultListener(deviceListPresenter);
        deviceListPresenter.onStart(adapter);
        adapter.registerAdapterDataObserver(emptyViewHandler);
        if (isScanningPossible() && scannerOnPlay) {
            gattExplorer.startScan();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        bluetoothHelper.removeListener(this);
        locationHelper.removeListener(this);
        adapter.unregisterAdapterDataObserver(emptyViewHandler);
        if (bluetoothHelper.isBluetoothEnabled()) {
            gattExplorer.stopScan();
        }
        deviceListPresenter.onStop();

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.playPauseButton) {
            if (scannerOnPlay) {

                scannerOnPlay = false;
                playPauseButton.setImageResource(android.R.drawable.ic_media_play);
                if (bluetoothHelper.isBluetoothEnabled()) {
                    deviceListPresenter.setUIUpdateState(false);
                    //TODO: Remove this
                    //For testing purpose
                    deviceListPresenter.removeAllFromList();

                }
            } else {
                if (locationHelper.checkLocationPermission()) {
                    scannerOnPlay = true;
                    playPauseButton.setImageResource(android.R.drawable.ic_media_pause);
                    if (isScanningPossible()) {
                        gattExplorer.startScan();
                        deviceListPresenter.setUIUpdateState(true);
                    }


                } else {
                    locationHelper.requestLocationPermission(view, this);
                }

            }
        }

    }

    @Override
    @android.support.annotation.RequiresApi(api = Build.VERSION_CODES.M)
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_LOCATION_REQUEST_CODE) {
            if (locationHelper.isLocationNeeded()) {
                if (!(shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION))) {
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                    preferences.edit().putBoolean(SHARED_PREF_NEVERASK, true).commit();
                }
            }

            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                locationHelper.showPermissionDeniedSnackbar(findViewById(R.id.activity_scanner), this);
            }
        }
    }

    public boolean isScanningPossible() {
        return locationHelper.checkLocationPermission() && locationHelper.IsLocationTurnedOn()
                && bluetoothHelper.isBluetoothEnabled();
    }

    @Override
    public void onBluetoothStatusChange(boolean isEnabled) {
        setEmptyView();
        if (isEnabled) {
            gattExplorer.onStart();
            deviceListPresenter.onStart(adapter);
            gattExplorer.setScanResultListener(deviceListPresenter);
            if (locationHelper.IsLocationTurnedOn() && locationHelper.IsLocationTurnedOn() && scannerOnPlay) {
                gattExplorer.startScan();
                deviceListPresenter.setUIUpdateState(true);
            }
        } else {
            if (scannerOnPlay) {
                gattExplorer.stopScan();
                deviceListPresenter.setUIUpdateState(false);
            }
            deviceListPresenter.removeAllFromList();
        }

    }

    @Override
    public void onLocationStatusChange(boolean isEnabled) {
        setEmptyView();
        if (isEnabled) {
            if (bluetoothHelper.isBluetoothEnabled() && locationHelper.checkLocationPermission() && scannerOnPlay) {
                deviceListPresenter.onStart(adapter);
                gattExplorer.startScan();
                deviceListPresenter.setUIUpdateState(true);
            }
        } else {
            gattExplorer.stopScan();
            deviceListPresenter.setUIUpdateState(false);
            deviceListPresenter.removeAllFromList();
        }
    }

    public void setEmptyView() {
        if (bluetoothHelper.isBluetoothEnabled()) {
            if (locationHelper.IsLocationTurnedOn()) {
                if (scannerOnPlay) {
                    emptyViewHandler.setEmptyViewState(EmptyViewHandler.TEXT_NO_BLDEVICE);
                } else {
                    emptyViewHandler.setEmptyViewState(EmptyViewHandler.TEXT_SCANNER_PAUSE);
                }
            } else {
                emptyViewHandler.setEmptyViewState(EmptyViewHandler.TEXT_LOCATION_DISABLED);
            }
        } else {
            emptyViewHandler.setEmptyViewState(EmptyViewHandler.TEXT_BLUETOOTH_DISABLED);
        }
    }
}
