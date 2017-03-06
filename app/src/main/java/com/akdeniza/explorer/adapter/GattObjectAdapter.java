package com.akdeniza.explorer.adapter;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.akdeniza.gatt_explorer.gatt_explorer.R;
import com.akdeniza.gatt_explorer.lib.gatt.GattListener;
import com.akdeniza.gatt_explorer.lib.model.Characteristic;
import com.akdeniza.gatt_explorer.lib.model.Service;

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
                ((ServiceHolder) holder).bindService((Service) gattObjectList.get(position));
                break;

            case CASE_CHARACTERISTIC:
                ((CharacteristicHolder) holder).bindCharacteristic((Characteristic) gattObjectList.get(position));
                break;

            default:
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (gattObjectList.get(position) instanceof Service) {
            return CASE_SERVICE;
        } else if (gattObjectList.get(position) instanceof Characteristic) {
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
        private TextView serviceName;

        public ServiceHolder(View itemView) {
            super(itemView);

            serviceUuid = (TextView) itemView.findViewById(R.id.serviceUiidTextView);
            serviceName = (TextView) itemView.findViewById(R.id.serviceNameTextView);
        }

        public void bindService(Service service) {
            serviceUuid.setText(service.getUuuid().toString());
            serviceName.setText(service.getName() != null ? service.getName() : "Unknown Name");
        }
    }

    public class CharacteristicHolder extends RecyclerView.ViewHolder {

        private TextView characteristicUiid;
        private TextView characteristicName;
        private TextView characteristicValue;

        public CharacteristicHolder(View itemView) {
            super(itemView);
            characteristicUiid = (TextView) itemView.findViewById(R.id.characteristicUiidTextView);
            characteristicName = (TextView) itemView.findViewById(R.id.characteristicNameTextView);
            characteristicValue = (TextView) itemView.findViewById(R.id.characteristicValueTextView);

        }

        public void bindCharacteristic(Characteristic characteristic) {
            characteristicUiid.setText(characteristic.getUuuid().toString());
            characteristicName.setText(characteristic.getName() != null ? characteristic.getName() : "Unknown Name");
            if (characteristic.getValue() != null) {
                characteristicValue.setText(characteristic.getValue());
            } else if (characteristic.getValueInByte() != null) {
                characteristicValue.setText(characteristic.getValueInByte().toString());
            } else {
                characteristicValue.setText("NOT READABLE");
            }

        }
    }
}
