package com.akdeniza.explorer.activity;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.akdeniza.explorer.adapter.GATTObjectAdapter;
import com.akdeniza.explorer.model.Device;
import com.akdeniza.explorer.utils.RecyclerViewLine;
import com.akdeniza.gatt_explorer.gatt_explorer.R;
import com.akdeniza.gatt_explorer.lib.GATTExplorer;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GATTProfileActivity extends AppCompatActivity {

    public static final String INTENT_DEVICE_KEY = "leDevice";

    private BluetoothDevice leDevice;
    private BluetoothGatt bluetoothGatt;
    private GATTObjectAdapter adapter;
    private GATTExplorer GATTExplorer;

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
        adapter = new GATTObjectAdapter();
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new RecyclerViewLine(this, R.drawable.divider));
        recyclerView.setAdapter(adapter);

        //Exploring Gatt
        GATTExplorer = new GATTExplorer(this);
        GATTExplorer.discoverGATT(leDevice, adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //TODO: Return to scanner activity?

    }


}
