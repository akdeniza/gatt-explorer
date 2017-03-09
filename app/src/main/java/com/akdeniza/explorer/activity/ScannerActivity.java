package com.akdeniza.explorer.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.akdeniza.explorer.handler.EmptyViewHandler;
import com.akdeniza.explorer.presenter.DeviceListPresenter;
import com.akdeniza.explorer.utils.BluetoothHelper;
import com.akdeniza.explorer.utils.LocationHelper;
import com.akdeniza.explorer.utils.RecyclerViewLine;
import com.akdeniza.explorer.adapter.BluetoothDeviceAdapter;
import com.akdeniza.gatt_explorer.gatt_explorer.R;
import com.akdeniza.gatt_explorer.lib.GATTExplorer;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScannerActivity extends AppCompatActivity implements
        FloatingActionButton.OnClickListener, BluetoothHelper.Listener, LocationHelper.Listener {

    private GATTExplorer GATTExplorer;
    private BluetoothHelper bluetoothHelper;
    private LocationHelper locationHelper;
    private boolean scannerOnPlay = false;
    private BluetoothDeviceAdapter adapter;
    private DeviceListPresenter deviceListPresenter;
    private EmptyViewHandler emptyViewHandler;
    private Dialog bluetoothDialog;
    private Dialog locationDialog;

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
        this.setTitle(getResources().getString(R.string.device_list));

        GATTExplorer = new GATTExplorer(this);
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
        deviceListPresenter.onStart(adapter);
        adapter.registerAdapterDataObserver(emptyViewHandler);
        if (isScanningPossible() && scannerOnPlay) {
            GATTExplorer.startScan(deviceListPresenter);
        }
        displayDialogsIfNeeded();
    }

    @Override
    protected void onStop() {
        super.onStop();
        bluetoothHelper.removeListener(this);
        locationHelper.removeListener(this);
        adapter.unregisterAdapterDataObserver(emptyViewHandler);
        if (bluetoothHelper.isBluetoothEnabled()) {
            GATTExplorer.stopScan();
        }
        deviceListPresenter.onStop();
        dismissDialogs();
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
                    if (isScanningPossible()) {
                        scannerOnPlay = true;
                        playPauseButton.setImageResource(android.R.drawable.ic_media_pause);
                        GATTExplorer.startScan(deviceListPresenter);
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
        if (requestCode == LocationHelper.PERMISSION_LOCATION_REQUEST_CODE) {
            if (locationHelper.isLocationNeeded()) {
                if (!(shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION))) {
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                    preferences.edit().putBoolean(LocationHelper.SHARED_PREF_NEVERASK, true).commit();
                }
            }

            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                locationHelper.showPermissionDeniedSnackbar(findViewById(R.id.activity_scanner), this);
            }
        }
    }

    private boolean isScanningPossible() {
        return locationHelper.checkLocationPermission() && locationHelper.IsLocationTurnedOn()
                && bluetoothHelper.isBluetoothEnabled();
    }

    /**
     * Indicates the change of the bluetooth status change. Will stop or start the bluetooth scanner accordingly
     * @param isEnabled
     */
    @Override
    public void onBluetoothStatusChange(boolean isEnabled) {
        setEmptyView();
        if (isEnabled) {
            deviceListPresenter.onStart(adapter);
            if (locationHelper.IsLocationTurnedOn() && locationHelper.IsLocationTurnedOn() && scannerOnPlay) {
                GATTExplorer.startScan(deviceListPresenter);
                deviceListPresenter.setUIUpdateState(true);
            }
            if (bluetoothDialog != null) {
                bluetoothDialog.dismiss();
            }
        } else {
            if (scannerOnPlay) {
                GATTExplorer.stopScan();
                deviceListPresenter.setUIUpdateState(false);
            }
            deviceListPresenter.removeAllFromList();
            displayDialogsIfNeeded();
        }

    }

    /**
     * Indicates the change of the location status change. Will stop or start the bluetooth scanner accordingly
     * @param isEnabled
     */
    @Override
    public void onLocationStatusChange(boolean isEnabled) {
        setEmptyView();
        if (isEnabled) {
            if (locationDialog != null) {
                locationDialog.dismiss();
            }
            if (bluetoothHelper.isBluetoothEnabled() && locationHelper.checkLocationPermission() && scannerOnPlay) {
                deviceListPresenter.onStart(adapter);
                GATTExplorer.startScan(deviceListPresenter);
                deviceListPresenter.setUIUpdateState(true);
            }
        } else {
            GATTExplorer.stopScan();
            deviceListPresenter.setUIUpdateState(false);
            deviceListPresenter.removeAllFromList();
            displayDialogsIfNeeded();
        }
    }

    /**
     * Sets the emptyView according to the ongoing state
     */
    private void setEmptyView() {
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

    private void bluetoothEnableDialog() {
        bluetoothDialog = new AlertDialog.Builder(this)
                .setMessage(R.string.bluetooth_dialog_message)
                .setPositiveButton(R.string.bluetooth_dialog_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        bluetoothHelper.enableBluetooth();
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton(R.string.dialog_negative, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        finish();
                    }
                })
                .setCancelable(false)
                .show();
    }

    private void locationEnableDialog() {
        locationDialog = new AlertDialog.Builder(this)
                .setMessage(R.string.location_dialog_message)
                .setPositiveButton(R.string.location_dialog_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        locationHelper.openLocationEnableSetting(ScannerActivity.this);
                    }
                })
                .setNegativeButton(R.string.dialog_negative, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        finish();
                    }
                })
                .setCancelable(false)
                .show();

    }

    /**
     * Check if any scan permission or if bluetooth or the lcoation is disabled and requests these
     * with the dialogs of these
     */
    private void displayDialogsIfNeeded() {
        if (bluetoothHelper.isBluetoothEnabled()) {
            if (locationHelper.IsLocationTurnedOn()) {

            } else {
                locationEnableDialog();
            }
        } else {
            if (locationHelper.IsLocationTurnedOn()) {
                bluetoothEnableDialog();
            } else {
                locationEnableDialog();
                bluetoothEnableDialog();
            }
        }
    }

    /**
     * dismisses all the dialogs on this activity
     */
    public void dismissDialogs() {
        if (bluetoothDialog != null) {
            bluetoothDialog.dismiss();
        }
        if (locationDialog != null) {
            locationDialog.dismiss();
        }
    }


}
