package com.akdeniza.gatt_explorer.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.akdeniza.gatt_explorer.gatt_explorer.R;
import com.akdeniza.gatt_explorer.gatt_explorer_lib.GATTExplorer;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScannerActivity extends AppCompatActivity implements
        FloatingActionButton.OnClickListener {

    private GATTExplorer gattExplorer;
    private boolean scannerOnPlay = true;

    @BindView(R.id.playPauseButton)
    FloatingActionButton playPauseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        ButterKnife.bind(this);

        playPauseButton.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        gattExplorer.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
//        gattExplorer.onStop();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.playPauseButton) {
            if (scannerOnPlay) {
                scannerOnPlay = false;
                playPauseButton.setImageResource(android.R.drawable.ic_media_pause);
//                gattExplorer.stopScan();
            } else {
                scannerOnPlay = true;
                playPauseButton.setImageResource(android.R.drawable.ic_media_play);
//                gattExplorer.startScan();
            }
        }

    }
}
