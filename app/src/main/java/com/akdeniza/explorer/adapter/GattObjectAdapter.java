package com.akdeniza.explorer.adapter;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.akdeniza.gatt_explorer.gatt_explorer.R;
import com.akdeniza.gatt_explorer.lib.gatt.GattListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akdeniz on 17/01/2017.
 */

public class GattObjectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements GattListener {

    private static final int CASE_SERVICE = 0;
    private static final int CASE_CHARACTERISTIC = 1;
    private List<Object> gattObjectList = new ArrayList<>();

    public GattObjectAdapter() {
        hasStableIds();
    }

    public void setGattAdapterObjectList(final List<Object> list) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                gattObjectList = list;
                if (Looper.myLooper() == Looper.getMainLooper()) {
                    notifyDataSetChanged();
                }
            }
        });


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case CASE_SERVICE:
                view = LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.gatt_recyclerview_service_row, parent, false);
                return new ServiceHolder(view);

            case CASE_CHARACTERISTIC:
                view = LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.gatt_recyclerview_characteristic_row, parent, false);
                return new CharacteristicHolder(view);

            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case CASE_SERVICE:
                ((ServiceHolder) holder).bindService((BluetoothGattService) gattObjectList.get(position));
                break;

            case CASE_CHARACTERISTIC:
                ((CharacteristicHolder) holder).bindCharacteristic((BluetoothGattCharacteristic) gattObjectList.get(position));
                break;

            default:
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (gattObjectList.get(position) instanceof BluetoothGattService) {
            return CASE_SERVICE;
        } else if (gattObjectList.get(position) instanceof BluetoothGattCharacteristic) {
            return CASE_CHARACTERISTIC;
        }

        return -1;
    }

    @Override
    public int getItemCount() {
        return gattObjectList.size();
    }

    @Override
    public long getItemId(int position) {
        return gattObjectList.get(position).hashCode();
    }

    @Override
    public void onData(List<Object> gattObjects) {
        setGattAdapterObjectList(gattObjects);
    }

    public class ServiceHolder extends RecyclerView.ViewHolder {

        private TextView serviceUuid;
        private TextView serviceValue;

        public ServiceHolder(View itemView) {
            super(itemView);

            serviceUuid = (TextView) itemView.findViewById(R.id.serviceUiidTextView);
            serviceValue = (TextView) itemView.findViewById(R.id.serviceValueTextView);
        }

        public void bindService(BluetoothGattService bluetoothGattService) {
            serviceUuid.setText(bluetoothGattService.getUuid().toString());
            serviceValue.setText("Value1");
        }
    }

    public class CharacteristicHolder extends RecyclerView.ViewHolder {

        private TextView characteristicUiid;
        private TextView characteristicValue;

        public CharacteristicHolder(View itemView) {
            super(itemView);
            characteristicUiid = (TextView) itemView.findViewById(R.id.characteristicUiidTextView);
            characteristicValue = (TextView) itemView.findViewById(R.id.characteristicValueTextView);

        }

        public void bindCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
            characteristicUiid.setText(bluetoothGattCharacteristic.getUuid().toString());
            if (bluetoothGattCharacteristic.getValue() != null) {
                characteristicValue.setText(bluetoothGattCharacteristic.getValue().toString());
            } else {
                characteristicValue.setText("Value2");
            }

        }
    }
}
