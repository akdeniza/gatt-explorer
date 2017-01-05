package com.akdeniza.gatt_explorer.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.akdeniza.gatt_explorer.gatt_explorer.R;
import com.akdeniza.gatt_explorer.gatt_explorer_lib.GATTExplorer;

public class ScannerActivity extends AppCompatActivity {

    private GATTExplorer gattExplorer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
