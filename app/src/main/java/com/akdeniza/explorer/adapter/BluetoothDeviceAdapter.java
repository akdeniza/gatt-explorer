package com.akdeniza.explorer.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.akdeniza.explorer.activity.GATTProfileActivity;
import com.akdeniza.explorer.presenter.DeviceListPresenter;
import com.akdeniza.gatt_explorer.gatt_explorer.R;
import com.akdeniza.explorer.model.Device;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akdeniz on 09/01/2017.
 */

public class BluetoothDeviceAdapter extends RecyclerView.Adapter<BluetoothDeviceAdapter.DeviceHolder>
        implements DeviceListPresenter.DataChangedListener {

    private List<Device> devices = new ArrayList<>();


    public BluetoothDeviceAdapter() {
        setHasStableIds(true);
    }

    @Override
    public BluetoothDeviceAdapter.DeviceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.scanner_recyclerview_device_row, parent, false);
        return new DeviceHolder(view);
    }

    @Override
    public void onBindViewHolder(BluetoothDeviceAdapter.DeviceHolder holder, int position) {
        Device device = devices.get(position);
        holder.bindDevice(device);
    }

    @Override
    public int getItemCount() {
        return devices.size();
    }

    @Override
    public void onDevicesChanged(List<Device> devices) {
        this.devices = devices;
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return Math.abs(devices.get(position).getAddress().hashCode() - 1);
    }

    public class DeviceHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView deviceImage;
        private TextView addressTextView;
        private TextView rssiTextView;
        private Device device;

        public DeviceHolder(View itemView) {
            super(itemView);

            deviceImage = (ImageView) itemView.findViewById(R.id.deviceImageView);
            addressTextView = (TextView) itemView.findViewById(R.id.addressTextView);
            rssiTextView = (TextView) itemView.findViewById(R.id.rssiTextView);

            itemView.setOnClickListener(this);
        }

        void bindDevice(Device device) {
            this.device = device;
            addressTextView.setText("Address: " + device.getAddress());
            rssiTextView.setText("Rssi:" + device.getRssi());

        }

        @Override
        public void onClick(View view) {
            Intent i = new Intent(view.getContext(), GATTProfileActivity.class);
            i.putExtra(GATTProfileActivity.INTENT_DEVICE_KEY, device.getBluetoothDevice());
            view.getContext().startActivity(i);
        }

    }
}
