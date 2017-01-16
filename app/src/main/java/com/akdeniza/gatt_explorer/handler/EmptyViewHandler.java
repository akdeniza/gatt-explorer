package com.akdeniza.gatt_explorer.handler;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.akdeniza.gatt_explorer.gatt_explorer.R;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by Akdeniz on 11/01/2017.
 */

public class EmptyViewHandler extends RecyclerView.AdapterDataObserver {

    public static final int TEXT_NO_BLDEVICE = 0;
    public static final int TEXT_BLUETOOTH_DISABLED = 1;
    public static final int TEXT_LOCATION_DISABLED = 2;
    public static final int TEXT_SCANNER_PAUSE = 3;
    private View view;
    private TextView textEmptyView;
    private RecyclerView recyler;

    public EmptyViewHandler(RecyclerView recyler, View view) {
        this.view = view;
        this.textEmptyView = (TextView) view.findViewById(R.id.textEmptyView);
        this.recyler = recyler;
        updateEmptyView();
    }

    public void setEmptyViewState(int state) {

        switch (state) {
            case TEXT_BLUETOOTH_DISABLED:
                textEmptyView.setText(R.string.emptyview_bluetooth_off);
                break;
            case TEXT_NO_BLDEVICE:
                textEmptyView.setText(R.string.emptyview_no_device_found);
                break;
            case TEXT_LOCATION_DISABLED:
                textEmptyView.setText(R.string.emptyview_location_off);
                break;
            case TEXT_SCANNER_PAUSE:
                textEmptyView.setText(R.string.emptyview_scanner_pause);
            default:
                break;
        }
    }

    public void updateEmptyView() {
        if (recyler.getAdapter() != null) {
            boolean showEmptyView = recyler.getAdapter().getItemCount() == 0;
            recyler.setVisibility(showEmptyView ? GONE : VISIBLE);
            view.setVisibility(showEmptyView ? VISIBLE : GONE);
        }
    }


    @Override
    public void onChanged() {
        super.onChanged();
        updateEmptyView();
    }


}